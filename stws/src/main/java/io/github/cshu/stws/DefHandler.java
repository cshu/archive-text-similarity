package io.github.cshu.stws;

import org.springframework.web.socket.handler.*;
import org.springframework.web.socket.*;

public class DefHandler extends TextWebSocketHandler {

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		var sid = session.getId();
		session.sendMessage(new TextMessage(sid));
	}
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		System.out.println(message.getPayload());
	}
	//@Override
	//public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	//	System.out.println(message.getPayload());
	//}
}
