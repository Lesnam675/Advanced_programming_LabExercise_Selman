package com.aastu.poker.models;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String name;

    private final List<Card> hand =
            new ArrayList<>();

    private int chips = 1000;

    public Player(String name) {

        this.name = name;
    }

    public void addCard(Card card) {

        hand.add(card);
    }

    public List<Card> getHand() {

        return hand;
    }

    public String getName() {

        return name;
    }

    public int getChips() {

        return chips;
    }

    public void addChips(int amount) {

        chips += amount;
    }

    public void removeChips(int amount) {

        chips -= amount;
    }
}