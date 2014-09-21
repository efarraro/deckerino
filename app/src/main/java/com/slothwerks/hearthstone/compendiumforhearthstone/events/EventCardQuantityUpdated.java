package com.slothwerks.hearthstone.compendiumforhearthstone.events;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

/**
 * Created by Eric on 9/20/2014.
 */
public class EventCardQuantityUpdated {

    protected Card mCard;
    protected int mQuantity;

    public EventCardQuantityUpdated(Card card, int quantity) {
        mCard = card;
        mQuantity = quantity;
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card card) {
        mCard = card;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }
}
