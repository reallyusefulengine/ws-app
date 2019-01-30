package com.jw.ws.junk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileTest {

	public static void main(String[] args) throws IOException {
		
		File file = new File("C:\\Users\\iqresource\\Desktop\\employee.txt");
		PrintWriter out = new PrintWriter(file, "UTF-8");
		Path basePath = Paths.get("C:\\springProjects");
		try( Stream<Path> entries = Files.walk(basePath)) {
			//Stream.of(entries).forEach(out::println);
			entries.forEach(out::println);
	        
		}

		
		
		out.close();

	}

}
