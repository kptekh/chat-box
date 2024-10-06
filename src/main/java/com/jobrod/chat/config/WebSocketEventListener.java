package com.jobrod.chat.config;

import com.jobrod.chat.model.ChatMessage;
import com.jobrod.chat.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations sendingOperations;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String)accessor.getSessionAttributes().get("username");
        if(Objects.nonNull(username)){
            log.info("User {} ",username+" disconnected.");
            var chatMessage = ChatMessage.builder().messageType(MessageType.LEAVE).sender(username)
                    .build();
            sendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }


}
