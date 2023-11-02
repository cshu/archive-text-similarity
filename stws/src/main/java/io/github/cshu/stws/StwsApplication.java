package io.github.cshu.stws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import org.springframework.web.servlet.config.annotation.*;

import org.springframework.kafka.annotation.*;
import org.springframework.kafka.config.*;
import org.apache.kafka.clients.admin.*;

@SpringBootApplication
public class StwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StwsApplication.class, args);
	}

@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer () {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*")
                    .allowedOrigins("*");
        }
    };
}

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("finished")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(id = "finListen", topics = "finished")
    public void listen(String in) {
        System.out.println(in);
    }
}
