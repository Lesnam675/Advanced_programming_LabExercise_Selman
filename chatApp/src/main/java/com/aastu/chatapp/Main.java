package com.aastu.chatapp;

import com.aastu.chatapp.client.ChatUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        ChatUI chatUI = new ChatUI();

        chatUI.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}