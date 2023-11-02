package io.github.cshu.similartext;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;


@Component
public class SimHandler {

  public Mono<ServerResponse> newtext(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue("{}"));
  }
}
