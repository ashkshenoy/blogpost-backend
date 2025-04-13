package com.example.techblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EnableJpaRepositories("com.example.techblog.repository")
@EntityScan("com.example.techblog.model")
@ComponentScan(basePackages = "com.example.techblog")

public class TechblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechblogApplication.class, args);
	}

}
