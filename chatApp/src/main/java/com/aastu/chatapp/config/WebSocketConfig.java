package com.aastu.chatapp.config;

import com.aastu.chatapp.server.ChatSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry registry
    ) {

        registry.addHandler(
                new ChatSocketHandler(),
                "/chat"
        ).setAllowedOrigins("*");
    }
}