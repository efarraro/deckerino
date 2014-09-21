package com.slothwerks.hearthstone.compendiumforhearthstone.models;

import com.slothwerks.hearthstone.compendiumforhearthstone.data.CardManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Eric on 9/18/2014.
 */
public class Deck {

    protected ArrayList<CardQuantityPair> mCards;

    public Deck() {
        mCards = new ArrayList<CardQuantityPair>();
    }

    public ArrayList<CardQuantityPair> getCards() {
        return mCards;
    }

    /**
     * Returns true if we can add the card to the deck (based on business logic) or false otherwise
     */
    public boolean canAddToDeck(Card card) {

        // can add 1 copy if card is Legendary, and 2 otherwise
        if((getCountForCard(card) == 0 && card.getElite()) ||
            getCountForCard(card) < 2 && !card.getElite()) {

            return true;
        }

        // business logic prevented the card from being added to the deck
        return false;
    }

    /**
     * Returns the current card count for the specified card, in this deck
     */
    public int getCountForCard(Card card) {
        int count = 0;

        for(CardQuantityPair pair : mCards) {
            if(pair.getCard().getId().equals(card.getId())) {
                return pair.getQuantity();
            }
        }

        return 0;
    }

    public void addToDeck(Card card) {

        if(!canAddToDeck(card))
            return;

        // find the card/quantity pair, and increment the quantity
        CardQuantityPair pair = null;
        for(CardQuantityPair p : mCards) {
            if(p.getCard().getId().equals(card.getId())) {
                pair = p;
                break;
            }
        }

        // add a new entry (if null), or increment the quantity of the existing entry
        if(pair == null)
            mCards.add(new CardQuantityPair(card, 1));
        else
            pair.setQuantity(pair.getQuantity() + 1);

        // by default, sort by cost
        Collections.sort(mCards, new Card.CostComparator());

    }

    /**
     * Removes all quantity of the specified card from the deck
     */
    public void removeAllCopies(Card card) {

        // remove from the main deck list
        ArrayList<CardQuantityPair> toRemove = new ArrayList<CardQuantityPair>();

        for(CardQuantityPair pair : mCards) {
            if(pair.getCard().getId().equals(card.getId()))
                toRemove.add(pair);
        }

        for(CardQuantityPair pair : toRemove) {
            mCards.remove(pair);
        }
    }
}
