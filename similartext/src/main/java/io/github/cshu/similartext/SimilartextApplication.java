package io.github.cshu.similartext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SimilartextApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SimilartextApplication.class, args);
	}

}
