package com.slothwerks.hearthstone.compendiumforhearthstone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

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

    public DeckDbAdapter(Context context) {
        super(context);
    }

    public long createEmptyDeck(PlayerClass playerClass) {


        ContentValues values = new ContentValues();
        values.put(CLASS, playerClass.toString());

        return mDb.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllDecks() {
        return mDb.rawQuery("select * from " + TABLE_NAME, null);
    }

    public void updateDeckName(String name) {

    }

    public void updateCardData(String cardData) {

    }
}
