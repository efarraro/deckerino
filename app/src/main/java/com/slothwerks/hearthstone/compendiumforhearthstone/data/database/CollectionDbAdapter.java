package com.slothwerks.hearthstone.compendiumforhearthstone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

/**
 * Created by Eric on 9/11/2014.
 */
public class CollectionDbAdapter extends DbAdapter {

    public static final String ROW_ID = "_id";
    public static final String CARD_ID = "card_id";
    public static final String QUANTITY = "quantity";

    public static final String TABLE_NAME = "collection";

    public CollectionDbAdapter(Context context) {

        super(context);
    }

    public CollectionDbAdapter(Context context, SQLiteDatabase db)
    {
        super(context);
        mDb = db;
    }

    public void insertCard(Card card)
    {
        ContentValues values = new ContentValues();
        values.put(CARD_ID, card.getId());
        values.put(QUANTITY, 0);

        mDb.insert(TABLE_NAME, null, values);
    }

    public Cursor getCollection()
    {
        return mDb.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /*public void updateQuantity(Card card, int quantity) {
        // TODO test
        ContentValues values = new ContentValues();

        values.put(CARD_ID, "123");

        mDb.insert(CollectionDbAdapter.TABLE_NAME, null, values);
    }*/
}
