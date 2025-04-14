package com.newspeed19;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NewSpeed19Application {
	public static void main(String[] args) {
		SpringApplication.run(NewSpeed19Application.class, args);
	}

}
