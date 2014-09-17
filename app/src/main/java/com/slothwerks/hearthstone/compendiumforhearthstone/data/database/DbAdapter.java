package com.slothwerks.hearthstone.compendiumforhearthstone.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.slothwerks.hearthstone.compendiumforhearthstone.data.CardManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Eric on 9/10/2014.
 */
public abstract class DbAdapter {

    // reference http://stackoverflow.com/questions/4063510/multiple-table-sqlite-db-adapters-in-android
    public static final String DATABASE_NAME = "Compendium";
    public static final int DATABASE_VERSION = 1;

    protected final Context mContext;
    protected SQLiteDatabase mDb;
    protected SQLiteOpenHelper mDbHelper;


    private static final String CREATE_TABLE_CARDS =
            "create table cards (" + CardDbAdapter.ROW_ID + " TEXT primary key, " +
                    CardDbAdapter.FLAVOR + " TEXT, " +
                    CardDbAdapter.SET + " TEXT, " +
                    CardDbAdapter.TEXT + " TEXT, " +
                    CardDbAdapter.CLASS + " TEXT, " +
                    CardDbAdapter.RARITY + " TEXT, " +
                    CardDbAdapter.NAME + " TEXT);";

    private static final String CREATE_TABLE_COLLECTION =
            "create table collection (_id integer primary key autoincrement, " +
                    CollectionDbAdapter.QUANTITY + " INTEGER, " +
                    CollectionDbAdapter.CARD_ID + " STRING, " +
                    "FOREIGN KEY (" + CollectionDbAdapter.CARD_ID + ") REFERENCES " +
                    CardDbAdapter.TABLE_NAME + "(" + CardDbAdapter.ROW_ID + ") ON DELETE CASCADE" +
                    ");";


    protected static class DatabaseHelper extends SQLiteOpenHelper
    {
        protected  Context mContext;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d("test", "creating databases");

            db.execSQL(CREATE_TABLE_CARDS);
            db.execSQL(CREATE_TABLE_COLLECTION);

            // load the database up with the list of cards
            loadCardDatabase(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // TODO upgrade code
        }

        /**
         * Performs the initial creation of the database, based on the JSON resource
         */
        protected void loadCardDatabase(SQLiteDatabase db)
        {
            //db.beginTransaction();

            CardDbAdapter cardAdapter = new CardDbAdapter(mContext, db);
            CollectionDbAdapter collectionAdapter = new CollectionDbAdapter(mContext, db);

            // TODO move the loading/reading of JSON into cardDbAdapter
            List<Card> cards = CardManager.getInstance(mContext).getAllCards();
            for(Card c : cards)
            {
                cardAdapter.insertCard(c);
                collectionAdapter.insertCard(c);
            }

            //db.endTransaction();
        }
    }

    public DbAdapter(Context context) {

        mContext = context;
        mDbHelper = new DatabaseHelper(context);
    }

    public DbAdapter open() throws SQLException {

        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {

        if(mDb != null && mDb.isOpen())
            mDb.close();
    }
}
