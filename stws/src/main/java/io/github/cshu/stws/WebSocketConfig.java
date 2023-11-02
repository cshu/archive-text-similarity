package io.github.cshu.stws;

import org.springframework.web.socket.config.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.socket.*;

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
