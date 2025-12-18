package com.booklend.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.booklend.backend")
public class BooklendBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooklendBackendApplication.class, args);
	}
}
