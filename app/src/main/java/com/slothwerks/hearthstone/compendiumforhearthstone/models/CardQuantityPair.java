package com.slothwerks.hearthstone.compendiumforhearthstone.models;

/**
 * Created by Eric on 9/20/2014.
 */
public class CardQuantityPair {

    protected Card mCard;
    protected int mQuantity;

    public CardQuantityPair(Card card, int quantity) {
        mCard = card;
        mQuantity = quantity;
    }

    public void updateQuantity(int quantity) {
        mQuantity = Math.max(0, mQuantity + quantity);
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
