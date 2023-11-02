package io.github.cshu.stws;

import org.springframework.web.socket.handler.*;
import org.springframework.web.socket.*;

public class DefHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		// ...
	}

}
