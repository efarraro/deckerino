package com.slothwerks.hearthstone.compendiumforhearthstone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardSet;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Eric on 9/10/2014.
 */
public class CardDbAdapter extends DbAdapter {

    public static final String ROW_ID = "_id";
    public static final String NAME = "name";
    public static final String RACE = "race";
    public static final String SET = "card_set";
    public static final String ELITE = "elite";
    public static final String CLASS = "class";
    public static final String TYPE = "type";
    public static final String RARITY = "rarity";
    public static final String COST = "cost";
    public static final String ATTACK = "attack";
    public static final String HEALTH = "health";
    public static final String FLAVOR = "flavor";
    public static final String ARTIST = "artist";
    public static final String COLLECTIBLE = "collectible";
    public static final String HOWTOGET = "howtoget";
    public static final String HOWTOGETGOLD = "howtogetgold";
    public static final String MECHANICS = "mechanics";

    public static final String TABLE_NAME = "cards";

    protected ArrayList<Card> mCards;

    public CardDbAdapter(Context context) {

        super(context);
    }

    public CardDbAdapter(Context context, SQLiteDatabase db)
    {
        super(context);
        mDb = db;
    }

    public long insertCard(Card card){
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROW_ID, card.getId());
        initialValues.put(NAME, card.getName());
        initialValues.put(SET, card.getSet().toString());
        initialValues.put(FLAVOR, card.getFlavor());

        return mDb.insert(TABLE_NAME, null, initialValues);
    }

    public Cursor getAllCards() {

        return this.mDb.rawQuery("select * from cards", null);
    }

    public Cursor cardCursorById(String id)
    {
        Cursor cursor = mDb.rawQuery(
                "select * from " + TABLE_NAME +" where " + ROW_ID + " = ?",
                new String[]{ id });

        cursor.moveToFirst();

        return cursor;
    }

    public Cursor findCardsLike(String query)
    {
        Cursor cursor = mDb.rawQuery(
                "select * from " + TABLE_NAME + " where " + NAME + " like ? ",
                new String[] { "%" + query + "%" }
        );

        return cursor;
    }

    public Card cardById(String id)
    {
        Card card = null;

        try {
            open();

            card = cursorToCard(cardCursorById(id));

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return card;
    }

    public static Card cursorToCard(Cursor c)
    {
        Card card = new Card();

        card.setId(c.getString(c.getColumnIndex(ROW_ID)));
        card.setName(c.getString(c.getColumnIndex(NAME)));
        card.setSet(Utility.stringToCardSet(c.getString(c.getColumnIndex(SET))));
        card.setFlavor(c.getString(c.getColumnIndex(FLAVOR)));

        return card;
    }
}
