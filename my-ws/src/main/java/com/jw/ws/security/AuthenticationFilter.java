package com.jw.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jw.ws.SpringApplicationContext;
import com.jw.ws.service.UserService;
import com.jw.ws.shared.dto.UserDto;
import com.jw.ws.ui.model.request.UserLoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	public AuthenticationFilter(AuthenticationManager authernticationManager) {
		this.authernticationManager = authernticationManager;
	}

	private final AuthenticationManager authernticationManager;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			UserLoginRequestModel creds = new ObjectMapper().
					readValue(req.getInputStream(), UserLoginRequestModel.class);
			
			return authernticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					creds.getEmail(),
					creds.getPassword(),
					new ArrayList<>()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, 
											HttpServletResponse res, 
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		
		String username = ((User) auth.getPrincipal()).getUsername(); 
		String token = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
				.compact();
		UserService userService = (UserService)SpringApplicationContext.getBean("userServiceImp");
		UserDto userDto = userService.getUser(username);
		res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		res.addHeader("userId", userDto.getUserId());

	}

	

	
	
}
