package com.example.blissful.blissful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BlissfulApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlissfulApplication.class, args);
	}

}
