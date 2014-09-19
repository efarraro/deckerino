package com.slothwerks.hearthstone.compendiumforhearthstone.events;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

/**
 * Created by Eric on 9/18/2014.
 */

/**
 * An event raised by EventBus when a card is tapped in one of the list views
 */
public class EventCardTapped {

    protected Card mCard;

    public EventCardTapped(Card card) {
        mCard = card;
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card card) {
        mCard = card;
    }
}
