package com.slothwerks.hearthstone.compendiumforhearthstone.events;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;

/**
 * Created by Eric on 9/18/2014.
 */
public class EventDeckUpdated {

    protected final Deck mDeck;

    public EventDeckUpdated(Deck deck) {
        mDeck = deck;
    }

    public Deck getDeck() {
        return mDeck;
    }
}
