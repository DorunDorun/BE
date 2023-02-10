package shop.dodotalk.dorundorun.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*").withSockJS();
    }

    @EventListener
    public void connectEvent(SessionConnectEvent sessionConnectEvent){
        System.out.println(sessionConnectEvent);
        System.out.println("연결 성공 감지!_!#!#!#!@#!@@#!@!#!$!@");
        //return "redirect:chat/message";
    }
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        System.out.println(sessionDisconnectEvent.getSessionId());
        System.out.println("연결 끊어짐 감지!~!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}