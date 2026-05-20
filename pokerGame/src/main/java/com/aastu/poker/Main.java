package com.aastu.poker;

import com.aastu.poker.game.PokerGame;
import com.aastu.poker.models.Card;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private PokerGame game;

    private HBox playerCardsBox;

    private HBox botCardsBox;

    private HBox communityCardsBox;

    private Label resultLabel;

    private Label playerChipsLabel;

    private Label botChipsLabel;

    private Label potLabel;

    private boolean revealBotCards =
            false;

    @Override
    public void start(Stage stage) {

        game = new PokerGame();

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0f0f1a, #1b1b2f);"
        );
        VBox table =
                new VBox(12);

        table.setAlignment(
                Pos.CENTER
        );

        table.setPadding(
                new Insets(10)
        );

        table.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-border-color: rgba(255,255,255,0.08);" +
                        "-fx-border-width: 2;"
        );

        Label title =
                createHeader(
                        "♠ TEXAS HOLD'EM ♠"
                );

        resultLabel =
                createInfoLabel(
                        "PLACE YOUR BET"
                );

        potLabel =
                createInfoLabel(
                        "POT: $0"
                );

        playerChipsLabel =
                createInfoLabel("");

        botChipsLabel =
                createInfoLabel("");

        updateMoneyLabels();

        VBox info =
                new VBox(8);

        info.setAlignment(
                Pos.CENTER
        );

        info.getChildren().addAll(
                title,
                resultLabel,
                potLabel,
                createChipStack(),
                playerChipsLabel,
                botChipsLabel
        );

        botCardsBox =
                new HBox(18);

        botCardsBox.setAlignment(
                Pos.CENTER
        );

        communityCardsBox =
                new HBox(18);

        communityCardsBox.setAlignment(
                Pos.CENTER
        );

        playerCardsBox =
                new HBox(18);

        playerCardsBox.setAlignment(
                Pos.CENTER
        );

        HBox controls =
                createControls();

        table.getChildren().addAll(
                info,
                botCardsBox,
                communityCardsBox,
                controls,
                playerCardsBox
        );

        root.setCenter(table);

        renderHands();

        Scene scene =
                new Scene(
                        root,
                        980,
                        580
                );

        stage.setTitle(
                "Poker Royale"
        );

        stage.setMinWidth(950);

        stage.setMinHeight(650);

        stage.setScene(scene);

        stage.show();
    }

    private HBox createControls() {

        HBox box =
                new HBox(15);

        box.setAlignment(
                Pos.CENTER
        );

        Button bet50 =
                createModernButton(
                        "BET 50"
                );

        Button bet100 =
                createModernButton(
                        "BET 100"
                );

        Button flop =
                createModernButton(
                        "FLOP"
                );

        Button turn =
                createModernButton(
                        "TURN"
                );

        Button river =
                createModernButton(
                        "RIVER"
                );

        Button showdown =
                createModernButton(
                        "SHOWDOWN"
                );

        Button reset =
                createModernButton(
                        "RESET"
                );

        bet50.setOnAction(e -> {

            makeBet(50);
        });

        bet100.setOnAction(e -> {

            makeBet(100);
        });

        flop.setOnAction(e -> {

            game.dealFlop();

            renderCommunityCards();
        });

        turn.setOnAction(e -> {

            game.dealTurn();

            renderCommunityCards();
        });

        river.setOnAction(e -> {

            game.dealRiver();

            renderCommunityCards();
        });

        showdown.setOnAction(e -> {

            revealBotCards = true;

            renderHands();

            resultLabel.setText(
                    game.determineWinner()
            );

            updateMoneyLabels();
        });

        reset.setOnAction(e -> {

            game = new PokerGame();

            revealBotCards = false;

            renderHands();

            renderCommunityCards();

            updateMoneyLabels();

            potLabel.setText(
                    "POT: $0"
            );

            resultLabel.setText(
                    "NEW ROUND"
            );
        });

        box.getChildren().addAll(
                bet50,
                bet100,
                flop,
                turn,
                river,
                showdown,
                reset
        );

        return box;
    }

    private HBox createChipStack() {

        HBox chips =
                new HBox(10);

        chips.setAlignment(
                Pos.CENTER
        );

        chips.getChildren().addAll(
                createChip("#ff4d6d"),
                createChip("#ffd60a"),
                createChip("#00c2ff")
        );

        return chips;
    }

    private StackPane createChip(
            String color
    ) {

        StackPane chip =
                new StackPane();

        chip.setPrefSize(40, 40);

        chip.setStyle(
                "-fx-background-color: " +
                        color + ";" +
                        "-fx-background-radius: 100;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 3;"
        );

        return chip;
    }

    private void makeBet(int amount) {

        game.placeBet(
                game.getPlayer(),
                amount
        );

        game.placeBet(
                game.getBot(),
                amount
        );

        potLabel.setText(
                "POT: $" +
                        game.getPot()
        );

        updateMoneyLabels();
    }

    private void updateMoneyLabels() {

        playerChipsLabel.setText(
                "PLAYER: $" +
                        game.getPlayer()
                                .getChips()
        );

        botChipsLabel.setText(
                "BOT: $" +
                        game.getBot()
                                .getChips()
        );
    }

    private void renderHands() {

        playerCardsBox.getChildren().clear();

        botCardsBox.getChildren().clear();

        for (Card card :
                game.getPlayer().getHand()) {

            playerCardsBox.getChildren().add(
                    createCard(card, false)
            );
        }

        for (Card card :
                game.getBot().getHand()) {

            botCardsBox.getChildren().add(
                    createCard(
                            card,
                            !revealBotCards
                    )
            );
        }
    }

    private void renderCommunityCards() {

        communityCardsBox.getChildren().clear();

        for (Card card :
                game.getCommunityCards()) {

            communityCardsBox.getChildren().add(
                    createCard(card, false)
            );
        }
    }

    private StackPane createCard(
            Card card,
            boolean hidden
    ) {

        StackPane pane =
                new StackPane();

        pane.setPrefSize(75, 110);
        pane.setStyle(
                "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;"
        );

        addHoverAnimation(pane);

        if (hidden) {

            pane.setStyle(
                    "-fx-background-color: linear-gradient(to bottom right, #3a0ca3, #4361ee);" +
                            "-fx-border-color: #4cc9f0;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 18;" +
                            "-fx-border-radius: 18;"
            );

            Label back =
                    new Label("♠");

            back.setStyle(
                    "-fx-text-fill: white;" +
                            "-fx-font-size: 42px;"
            );

            pane.getChildren().add(back);

            return pane;
        }

        pane.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-border-color: #ced4da;" +
                        "-fx-border-width: 2;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;"
        );

        VBox content =
                new VBox(6);

        content.setAlignment(
                Pos.CENTER
        );

        String suit =
                getSuitSymbol(card);

        String color =
                suit.equals("♥")
                        || suit.equals("♦")
                        ? "#ff4d6d"
                        : "#111";

        Label rank =
                new Label(
                        card.getRank()
                );

        rank.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " +
                        color + ";"
        );

        Label suitLabel =
                new Label(suit);

        suitLabel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-text-fill: " +
                        color + ";"
        );

        content.getChildren().addAll(
                rank,
                suitLabel
        );

        pane.getChildren().add(content);

        return pane;
    }

    private void addHoverAnimation(
            StackPane pane
    ) {

        ScaleTransition enter =
                new ScaleTransition(
                        Duration.millis(150),
                        pane
                );

        enter.setToX(1.08);

        enter.setToY(1.08);

        ScaleTransition exit =
                new ScaleTransition(
                        Duration.millis(150),
                        pane
                );

        exit.setToX(1);

        exit.setToY(1);

        pane.setOnMouseEntered(e -> {

            enter.playFromStart();
        });

        pane.setOnMouseExited(e -> {

            exit.playFromStart();
        });
    }

    private String getSuitSymbol(
            Card card
    ) {

        return switch (
                card.getSuit()
                ) {

            case "Hearts" -> "♥";

            case "Diamonds" -> "♦";

            case "Clubs" -> "♣";

            default -> "♠";
        };
    }

    private Label createHeader(
            String text
    ) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 34px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #4cc9f0;"
        );

        return label;
    }

    private Label createInfoLabel(
            String text
    ) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f1f1f1;"
        );

        return label;
    }

    private Button createModernButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.setStyle(
                "-fx-background-color: linear-gradient(to right, #4361ee, #4cc9f0);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 10 18 10 18;"
        );

        button.setOnMouseEntered(e -> {

            button.setStyle(
                    "-fx-background-color: linear-gradient(to right, #4895ef, #72efdd);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 10 18 10 18;"
            );
        });

        button.setOnMouseExited(e -> {

            button.setStyle(
                    "-fx-background-color: linear-gradient(to right, #4361ee, #4cc9f0);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 10 18 10 18;"
            );
        });

        return button;
    }

    public static void main(String[] args) {
        launch();
    }
}