package com.aastu.chatapp.client;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.aastu.chatapp.network.ChatWebSocketClient;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import com.aastu.chatapp.model.ChatMessage;

public class ChatUI {

    private VBox messagesBox;
    private TextField messageField;
    private Button sendButton;

    private ChatWebSocketClient client;
    private ObjectMapper mapper = new ObjectMapper();

    private String username;

    private void sendMessage() {

        try {

            String text = messageField.getText();

            if(text.isEmpty()){
                return;
            }

            Map<String, String> messageData = new HashMap<>();

            messageData.put("sender", username);

            messageData.put("content", text);

            String json = mapper.writeValueAsString(messageData);

            client.send(json);

            messageField.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start(Stage stage) {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Username");

        dialog.setHeaderText("Enter your username");

        dialog.setContentText("Username:");

        username = dialog.showAndWait().orElse("Unknown");

        messagesBox = new VBox(10);

        ScrollPane scrollPane = new ScrollPane(messagesBox);

        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(350);

        ListView<String> usersList = new ListView<>();

        usersList.getItems().add(username);

        usersList.setPrefWidth(120);

        messageField = new TextField();
        messageField.setPromptText("Type message...");

        sendButton = new Button("Send");

        sendButton.setOnAction(e -> sendMessage());

        messageField.setOnAction(e -> sendMessage());

        HBox bottomBox = new HBox(10, messageField, sendButton);

        BorderPane root = new BorderPane();

        root.setCenter(scrollPane);

        root.setRight(usersList);

        root.setBottom(bottomBox);

        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 500, 400);

        stage.setTitle("Chat App");

        stage.setScene(scene);

        try {

            client = new ChatWebSocketClient(
                    new URI("ws://localhost:8080/chat")
            );

            client.connect();
            client.setMessageHandler(message -> {

                try {

                    ChatMessage msg =
                            mapper.readValue(message, ChatMessage.class);

                    Label messageLabel = new Label(msg.getContent());

                    messageLabel.setWrapText(true);

                    HBox messageContainer = new HBox();

                    if(msg.getSender().equals(username)){

                        messageContainer.setStyle("-fx-alignment: center-right;");

                        messageLabel.setStyle(
                                "-fx-background-color: lightgreen;" +
                                        "-fx-padding: 10;" +
                                        "-fx-background-radius: 10;"
                        );

                    }else{

                        messageContainer.setStyle("-fx-alignment: center-left;");

                        messageLabel.setStyle(
                                "-fx-background-color: lightblue;" +
                                        "-fx-padding: 10;" +
                                        "-fx-background-radius: 10;"
                        );
                    }

                    messageContainer.getChildren().add(messageLabel);

                    messagesBox.getChildren().add(messageContainer);

                    scrollPane.layout();

                    scrollPane.setVvalue(1.0);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.show();
    }
}