package com.jw.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.jw.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto user);
	UserDto getUser(String email);

}
