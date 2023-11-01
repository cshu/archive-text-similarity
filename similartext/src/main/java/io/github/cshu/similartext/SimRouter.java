package io.github.cshu.similartext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class SimRouter {

  @Bean
  public RouterFunction<ServerResponse> route(SimHandler simHandler) {

    return RouterFunctions
      .route(POST("/newtext").and(accept(MediaType.TEXT_HTML)), simHandler::newtext);
  }
}