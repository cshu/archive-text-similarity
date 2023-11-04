package io.github.cshu.stws;

import org.springframework.web.socket.config.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.socket.*;
import org.springframework.messaging.simp.config.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(defHandler(), "/defHandler").setAllowedOrigins("*");
  }

  @Bean
  public WebSocketHandler defHandler() {
    return new DefHandler();
  }
}

//// @Configuration
//// @EnableWebSocket
//// @EnableWebSocketMessageBroker
//// @EnableScheduling
//// public class WebSocketConfig implements
// /*WebSocketConfigurer,*/WebSocketMessageBrokerConfigurer {
////
//////  @Override
//////  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//////    registry.addHandler(defHandler(), "/defHandler").setAllowedOrigins("*");
//////  }
//////
//////  @Bean
//////  public WebSocketHandler defHandler() {
//////    return new DefHandler();
//////  }
////
////  @Override
////  public void configureMessageBroker(MessageBrokerRegistry config) {
////    //config.enableSimpleBroker("/topic");
////    config.setApplicationDestinationPrefixes("/app").enableSimpleBroker("/message");
////    //config.setUserDestinationPrefix("/defHandler");
////  }
////  @Override
////  public void registerStompEndpoints(StompEndpointRegistry registry) {
////    registry.addEndpoint("/socket").allowCredentials(false).setAllowedOrigins("*").withSockJS();
////  }
//// }
