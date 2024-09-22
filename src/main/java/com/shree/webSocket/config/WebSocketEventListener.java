package com.shree.webSocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.shree.webSocket.dto.ChatMessage;
import com.shree.webSocket.enums.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	@Autowired
	private final SimpMessageSendingOperations messageSendingOperations;

	@EventListener
	public void webSocketDisconnectListener(SessionDisconnectEvent disconnectEvent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
		String userName = (String) headerAccessor.getSessionAttributes().get("userName");
		if(null != userName) {
			log.info("User {} disconnected!", userName);
			var chatMessage = ChatMessage.builder()
					.type(MessageType.LEAVE)
					.sender(userName)
					.build();
			messageSendingOperations.convertAndSend("/topic/public", chatMessage);
		}
	}
	
}
