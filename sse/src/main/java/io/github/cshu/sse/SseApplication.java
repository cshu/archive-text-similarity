package io.github.cshu.sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import org.springframework.web.servlet.config.annotation.*;
import org.springframework.beans.factory.annotation.*;

import org.springframework.kafka.annotation.*;
import org.springframework.kafka.config.*;
import org.apache.kafka.clients.admin.*;

@SpringBootApplication
public class SseApplication {

  public static void main(String[] args) {
    SpringApplication.run(SseApplication.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowCredentials(false)
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowedOrigins("*");
      }
    };
  }

  @Bean
  public NewTopic topic() {
    return TopicBuilder.name("finished").partitions(10).replicas(1).build();
  }

  @KafkaListener(id = "finListen", topics = "finished")
  public void listen(String in) {
    System.out.println("fin "+in);
    Util.sendResultToUser(in);
    // messagingTemplate.convertAndSendToUser(in, "/defHandler", "FINISHED",
    // headerAccessor.getMessageHeaders());
  }
}
