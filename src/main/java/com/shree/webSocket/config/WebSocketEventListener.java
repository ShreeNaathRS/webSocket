package com.shree.webSocket.config;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.shree.webSocket.dto.ChatMessage;
import com.shree.webSocket.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	@Getter
	@Setter
	@AllArgsConstructor
	class NativeHeader {
		private String userName;
	}
	
	@Autowired
	private final SimpMessageSendingOperations messageSendingOperations;

	@EventListener
	public void webSocketDisconnectListener(SessionDisconnectEvent disconnectEvent) {
//		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
//		String userName = getUserNameFromStompHeaderAccessor(headerAccessor);
//		if(null != userName) {
//			log.info("User {} disconnected!", userName);
//			var chatMessage = ChatMessage.builder()
//					.type(MessageType.LEAVE)
//					.sender(userName)
//					.build();
//			messageSendingOperations.convertAndSend("/topic/public", chatMessage);
//		}
	}
	
	@EventListener
	public void webSocketDisconnectListener(SessionConnectedEvent connectedEvent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(connectedEvent.getMessage());
		String userName = getUserNameFromStompHeaderAccessor(headerAccessor);
		if(null != userName) {
			log.info("User {} connected!", userName);
			var chatMessage = ChatMessage.builder()
					.type(MessageType.JOIN)
					.sender(userName)
					.content(userName + " has joined the chat room")
					.build();
			messageSendingOperations.convertAndSend("/topic/public", chatMessage);
		}
	}
	
	private String getUserNameFromStompHeaderAccessor(StompHeaderAccessor headerAccessor) {
		GenericMessage<?> message = (GenericMessage<?>) headerAccessor.getHeader("simpConnectMessage");
		MessageHeaders messageHeaders = message.getHeaders();
		if(messageHeaders.containsKey("nativeHeaders")) {
			Map<String, ArrayList<String>> header = (Map<String, ArrayList<String>>) messageHeaders.get("nativeHeaders");
			if(header.containsKey("userName")) {
				return (String) header.get("userName").get(0);
			}
		}
		return null;
	}
	
}
