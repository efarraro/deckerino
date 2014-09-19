package com.slothwerks.hearthstone.compendiumforhearthstone.models;

import java.util.ArrayList;

/**
 * Created by Eric on 9/18/2014.
 */
public class Deck {

    protected ArrayList<Card> mCards;

    public Deck() {
        mCards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return mCards;
    }

    public boolean addToDeck(Card card) {
        // TODO business logic
        mCards.add(card);

        return true;
    }
}
