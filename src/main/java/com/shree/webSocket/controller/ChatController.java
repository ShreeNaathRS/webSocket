package com.shree.webSocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.shree.webSocket.dto.ChatMessage;

@Controller
public class ChatController {
	
	@MessageMapping("/chat/send")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		return chatMessage;
	}
	
	@MessageMapping("/chat/add")
	@SendTo("topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor msgHeaderAccessor) {
		msgHeaderAccessor.getSessionAttributes().put("userName", chatMessage.getSender());
		return chatMessage;
	}
	
}
