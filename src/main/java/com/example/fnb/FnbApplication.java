package com.example.fnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(FnbApplication.class, args);
	}

}
