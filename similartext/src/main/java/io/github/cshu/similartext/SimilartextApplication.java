package io.github.cshu.similartext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.context.annotation.Bean;


import org.springframework.kafka.annotation.*;
import org.springframework.kafka.config.*;
import org.apache.kafka.clients.admin.*;

@SpringBootApplication
public class SimilartextApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SimilartextApplication.class, args);
	}

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("new")
                .partitions(10)
                .replicas(1)
                .build();
    }

}
