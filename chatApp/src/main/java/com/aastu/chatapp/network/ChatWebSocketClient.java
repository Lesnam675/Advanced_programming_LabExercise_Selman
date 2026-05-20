package com.aastu.chatapp.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.Consumer;

public class ChatWebSocketClient extends WebSocketClient {

    private Consumer<String> messageHandler;

    public ChatWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public void setMessageHandler(Consumer<String> handler) {
        this.messageHandler = handler;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String message) {

        System.out.println("Received: " + message);

        if(messageHandler != null){

            Platform.runLater(() -> {
                messageHandler.accept(message);
            });
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}