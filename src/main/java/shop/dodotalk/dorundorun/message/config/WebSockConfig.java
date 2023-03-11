package shop.dodotalk.dorundorun.message.config;


import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import shop.dodotalk.dorundorun.message.handler.StompHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler; // jwt 인증
    private final List<StompSession> sessions = new CopyOnWriteArrayList<>();
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(50 * 1024 * 1024);
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @EventListener
    public void connectEvent(SessionConnectEvent sessionConnectEvent){
        log.info("1111111111111111111111111111111111111111");
        log.info("socket 연결 성공");
        log.info(String.valueOf(sessionConnectEvent.getUser()));
        log.info(String.valueOf(sessionConnectEvent));
        sessions.add((StompSession) sessionConnectEvent);
        log.info(String.valueOf(sessions.size()));
        log.info("1111111111111111111111111111111111111111");
    }
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        log.info("2222222222222222222222222222222222222222222222");
        log.info("socket 연결 끊어짐");
        log.info(String.valueOf(sessionDisconnectEvent.getUser()));
        log.info(String.valueOf(sessionDisconnectEvent));
        sessions.remove((StompSession) sessionDisconnectEvent);
        log.info(String.valueOf(sessions.size()));
        log.info("2222222222222222222222222222222222222222222222");
    }

//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String sessionId = event.getSessionId();
//        System.out.println("------------------------연결끊김--------------------");
//        System.out.println("sessionId : " + sessionId);
//        System.out.println("------------------------연결끊김--------------------");
//        // 연결이 끊어진 클라이언트의 구독 정보를 모두 해제합니다.
//        // 세션 종료 이벤트 처리 코드
//    }
}