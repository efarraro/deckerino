package com.slothwerks.hearthstone.compendiumforhearthstone.data.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import java.sql.SQLException;

/**
 * Created by Eric on 9/22/2014.
 */
public class DeckDbAdapter extends DbAdapter {

    public static final String TABLE_NAME = "decks";

    public static final String ROW_ID = "_id";
    public static final String CLASS = "class";
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String CARD_DATA = "card_data";
    public static final String CREATED = "created";
    public static final String LAST_MODIFIED = "last_modified";

    public DeckDbAdapter(Context context) {
        super(context);
    }

    public static final String TAG = "DeckDbAdapter";

    public long createEmptyDeck(PlayerClass playerClass, String deckName) {

        ContentValues values = new ContentValues();
        values.put(CLASS, playerClass.toString());
        values.put(CREATED, System.currentTimeMillis());
        values.put(LAST_MODIFIED, System.currentTimeMillis());

        // set a default deck name
        values.put(NAME, deckName);

        return mDb.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllDecks() {
        return mDb.rawQuery(
                "select * from " + TABLE_NAME + " order by " + LAST_MODIFIED + " DESC", null);
    }

    public void updateCardData(Deck deck) {
        updateCardData(deck.getId(), deck.toDeckerinoFormat());
    }

    public void updateCardData(long deckId, String cardData) {

        ContentValues values = new ContentValues();
        values.put(CARD_DATA, cardData);
        values.put(LAST_MODIFIED, System.currentTimeMillis());

        mDb.update(TABLE_NAME, values, ROW_ID + "= ?", new String[] { Long.toString(deckId) });
    }

    public void deleteDeck(Deck deck) throws SQLException {
        try {
            open();
            mDb.delete(TABLE_NAME, ROW_ID + " = ?", new String[] { Long.toString(deck.getId()) });

        } catch(SQLException e) {
            throw e;
        } finally {
            close();
        }
    }

    public void updateDeckName(Deck deck) throws SQLException {

        try {
            open();

            ContentValues values = new ContentValues();
            values.put(NAME, deck.getName());
            mDb.update(
                    TABLE_NAME, values, ROW_ID + "= ?", new String[]{Long.toString(deck.getId())});

        } catch(SQLException e) {
            throw e;
        } finally {
         close();
        }
    }

    public Deck getDeckById(long id) {

        if(id == -1)
            return new Deck();

        try {
            open();
            Cursor cursor = mDb.rawQuery(
                    "select * from " + TABLE_NAME + " where " + ROW_ID + " = ?",
                    new String[] { Long.toString(id) });

            cursor.moveToFirst();

            return Deck.fromCursor(mContext, cursor, false);

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }
}
