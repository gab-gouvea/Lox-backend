package br.com.lox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoxApplication.class, args);
	}

}
