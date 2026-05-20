package com.aastu.poker.game;

import com.aastu.poker.models.Card;
import com.aastu.poker.models.Deck;
import com.aastu.poker.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerGame {

    private final Deck deck;

    private final Player player;

    private final Player bot;

    private final List<Card> communityCards =
            new ArrayList<>();

    private int pot = 0;

    public PokerGame() {

        deck = new Deck();

        deck.shuffle();

        player = new Player("You");

        bot = new Player("Bot");

        dealInitialCards();
    }

    private void dealInitialCards() {

        for (int i = 0; i < 2; i++) {

            player.addCard(
                    deck.drawCard()
            );

            bot.addCard(
                    deck.drawCard()
            );
        }
    }

    public void placeBet(
            Player player,
            int amount
    ) {

        if (player.getChips() >= amount) {

            player.removeChips(amount);

            pot += amount;
        }
    }

    public int getPot() {

        return pot;
    }

    public void dealFlop() {

        if (!communityCards.isEmpty()) {
            return;
        }

        for (int i = 0; i < 3; i++) {

            communityCards.add(
                    deck.drawCard()
            );
        }
    }

    public void dealTurn() {

        if (communityCards.size() == 3) {

            communityCards.add(
                    deck.drawCard()
            );
        }
    }

    public void dealRiver() {

        if (communityCards.size() == 4) {

            communityCards.add(
                    deck.drawCard()
            );
        }
    }

    public String determineWinner() {

        int playerScore =
                calculateScore(player);

        int botScore =
                calculateScore(bot);

        if (playerScore > botScore) {

            player.addChips(pot);

            return "PLAYER WINS $" + pot;

        } else if (botScore > playerScore) {

            bot.addChips(pot);

            return "BOT WINS $" + pot;

        } else {

            player.addChips(pot / 2);

            bot.addChips(pot / 2);

            return "DRAW";
        }
    }

    private int calculateScore(
            Player player
    ) {

        List<Card> allCards =
                new ArrayList<>();

        allCards.addAll(
                player.getHand()
        );

        allCards.addAll(
                communityCards
        );

        Map<String, Integer> rankCount =
                new HashMap<>();

        for (Card card : allCards) {

            rankCount.put(
                    card.getRank(),
                    rankCount.getOrDefault(
                            card.getRank(),
                            0
                    ) + 1
            );
        }

        int pairs = 0;

        int three = 0;

        int four = 0;

        for (int count :
                rankCount.values()) {

            if (count == 2) {
                pairs++;
            }

            if (count == 3) {
                three++;
            }

            if (count == 4) {
                four++;
            }
        }

        if (four == 1) {
            return 7;
        }

        if (three == 1 && pairs >= 1) {
            return 6;
        }

        if (three == 1) {
            return 3;
        }

        if (pairs >= 2) {
            return 2;
        }

        if (pairs == 1) {
            return 1;
        }

        return 0;
    }

    public String getHandRank(
            Player player
    ) {

        int score =
                calculateScore(player);

        return switch (score) {

            case 7 -> "FOUR OF A KIND";

            case 6 -> "FULL HOUSE";

            case 3 -> "THREE OF A KIND";

            case 2 -> "TWO PAIR";

            case 1 -> "ONE PAIR";

            default -> "HIGH CARD";
        };
    }

    public Player getPlayer() {
        return player;
    }

    public Player getBot() {
        return bot;
    }

    public List<Card> getCommunityCards() {

        return communityCards;
    }
}