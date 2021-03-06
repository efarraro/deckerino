package com.slothwerks.hearthstone.compendiumforhearthstone.models;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.slothwerks.hearthstone.compendiumforhearthstone.data.CardManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.IDeckerino;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Constants;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Eric on 9/18/2014.
 */
public class Deck {

    public static final String TAG = "Deck";

    protected long mId;
    protected PlayerClass mPlayerClass;
    protected String mName;

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
            getCountForCard(card) < 2 && !card.getElite() &&
            getCardCount() < 30) {

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


    /**
     * Gets the card count (taking into account quantity of each card, including multiple)
     */
    public int getCardCount() {
        int count = 0;
        for(CardQuantityPair pair : mCards) {
            count += pair.getQuantity();
        }

        return count;
    }

    public void addToDeck(Card card, int quantity) {
        Assert.assertTrue("Quantity must be greater than 0", quantity > 0);

        // not the most efficient approach, but adding the cards 1 by 1 is simpler
        for(int i = 0; i <  quantity; i++) {

            if (!canAddToDeck(card))
                return;

            // find the card/quantity pair, and increment the quantity
            CardQuantityPair pair = null;
            for (CardQuantityPair p : mCards) {
                if (p.getCard().getId().equals(card.getId())) {
                    pair = p;
                    break;
                }
            }

            // add a new entry (if null), or increment the quantity of the existing entry
            if (pair == null)
                mCards.add(new CardQuantityPair(card, 1));
            else
                pair.setQuantity(pair.getQuantity() + 1);
        }

        // by default, sort by cost
        Collections.sort(mCards, new Card.CostComparator());
    }

    public void addToDeck(Card card) {

        addToDeck(card, 1);
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

    /**
     * Converts the deck into Deckerino format
     */
    public String toDeckerinoFormat() {

        if(getCardCount() == 0)
            return null;

        StringBuffer buffer = new StringBuffer("http://deckerino.com");

        // v1.0/2.5.4.2/druid##E_103^2/D_103^4
        //http://deckerino.com?v=1.0&patch=2.5.4.2&class=druid&E_103=2

        // add Deckerion version, Hearthstone data version, and player class
        buffer.append("?v=" + IDeckerino.VERSION + "&");
        buffer.append("patch=" + Constants.HEARTHSTONE_DATA_VERSION + "&");
        buffer.append("class=" + mPlayerClass.toString() + "&");

        for(CardQuantityPair pair : mCards) {
            buffer.append(pair.getCard().getId());
            buffer.append("=");
            buffer.append(Integer.toString(pair.getQuantity()));
            buffer.append("&");
        }

        return buffer.toString();
    }

    /**
     * Returns a deck based on a Deckerino-format string
     * @param context Context; required to access card database
     * @param deckerinoString A string representing the deck, in Deckerino format
     * @return
     */
    public static Deck fromDeckerinoFormat(Context context, String deckerinoString) throws SQLException {
        Deck deck = new Deck();

        if(deckerinoString == null)
            return deck;

        Log.d(TAG, "Parsing Deckerino format: " + deckerinoString);

        CardDbAdapter adapter = null;
        try {

            adapter = (CardDbAdapter) new CardDbAdapter(context).open();

            Uri uri = Uri.parse(deckerinoString);
            String version = uri.getQueryParameter("v");
            String patch = uri.getQueryParameter("patch");

            String name = uri.getQueryParameter("name");
            if(name != null && name != "")
                deck.setName(name);

            // TODO what if class is invalid?
            String playerClass = uri.getQueryParameter("class");
            deck.setPlayerClass(PlayerClass.valueOf(playerClass));

            Set<String> queryParams = uri.getQueryParameterNames();
            for (String s : queryParams) {
                // ignore version/header params
                if (s.equals("v") || s.equals("patch") || s.equals("class")) continue;

                // expected that card ID has an underscore '_'
                if (!s.contains("_")) continue;

                Card card = adapter.cardById(s);
                deck.addToDeck(card, Integer.parseInt(uri.getQueryParameter(s)));
            }
        } finally {

                if(adapter != null) {
                    adapter.close();
                }
            }

            /*
        String[] tokens = deckerinoString.split("##");
        String header = tokens[0];
        // for now, header is unused



        try {

            adapter = (CardDbAdapter)new CardDbAdapter(context).open();

            String[] cardStrings = tokens[1].split("/");
            for (String cardString : cardStrings) {
                tokens = cardString.split("\\^");
                String cardId = tokens[0];
                int quantity = Integer.parseInt(tokens[1]);

                Card card = adapter.cardById(cardId);

                deck.addToDeck(card, quantity);
            }
        } finally {

            if(adapter != null) {
                adapter.close();
            }
        }
*/
        return deck;
    }

    public static Deck fromCursor(Context context, Cursor cursor, boolean skipCardData)
            throws SQLException {

        Deck deck = new Deck();
        if(!skipCardData)
                deck = Deck.fromDeckerinoFormat(
                    context, cursor.getString(cursor.getColumnIndex(DeckDbAdapter.CARD_DATA)));
        deck.setPlayerClass(
                PlayerClass.valueOf(cursor.getString(cursor.getColumnIndex(DeckDbAdapter.CLASS))));
        deck.setName(cursor.getString(cursor.getColumnIndex(DeckDbAdapter.NAME)));
        deck.setId(cursor.getLong(cursor.getColumnIndex(DeckDbAdapter.ROW_ID)));

        return deck;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public PlayerClass getPlayerClass() {
        return mPlayerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        mPlayerClass = playerClass;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
