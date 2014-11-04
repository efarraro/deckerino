package com.slothwerks.hearthstone.compendiumforhearthstone.data.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.slothwerks.hearthstone.compendiumforhearthstone.data.CardManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDatabaseReady;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Constants;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/10/2014.
 */
public abstract class DbAdapter {

    // reference http://stackoverflow.com/questions/4063510/multiple-table-sqlite-db-adapters-in-android
    public static final String DATABASE_NAME = "Compendium";
    public static final int DATABASE_VERSION = 1;
    public static final String TAG = "DbAdapter";

    protected final Context mContext;
    protected SQLiteDatabase mDb;
    protected DatabaseHelper mDbHelper;

    private static final String CREATE_TABLE_CARDS =
            "create table cards (" + CardDbAdapter.ROW_ID + " TEXT primary key, " +
                    CardDbAdapter.FLAVOR + " TEXT, " +
                    CardDbAdapter.SET + " TEXT, " +
                    CardDbAdapter.TEXT + " TEXT, " +
                    CardDbAdapter.CLASS + " TEXT, " +
                    CardDbAdapter.RARITY + " TEXT, " +
                    CardDbAdapter.ATTACK + " INT, " +
                    CardDbAdapter.HEALTH + " INT, " +
                    CardDbAdapter.DURABILITY + " INT, " +
                    CardDbAdapter.COST + " INT, " +
                    CardDbAdapter.ELITE + " INT, " +
                    CardDbAdapter.NAME + " TEXT);";

    private static final String CREATE_TABLE_DECKS =
            "create table " + DeckDbAdapter.TABLE_NAME +
                    "(" + DeckDbAdapter.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    DeckDbAdapter.CREATED + " INT, " +
                    DeckDbAdapter.LAST_MODIFIED + " INT, " +
                    DeckDbAdapter.NAME + " TEXT, " +
                    DeckDbAdapter.CLASS + " TEXT, " +
                    DeckDbAdapter.VERSION + " TEXT, "  +
                    DeckDbAdapter.CARD_DATA + " TEXT);";

    protected static class DatabaseHelper extends SQLiteOpenHelper
    {
        protected  Context mContext;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            mContext = context;
        }

        public void createCardDatabase(SQLiteDatabase db) {
            Log.d(TAG, "Creating card database");

            // store a preference to remind us what version we're using for the data file
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            prefs.edit().putInt("DATA_FILE", Constants.CARD_DATA_FILE).commit();

            db.execSQL(CREATE_TABLE_CARDS);

            // load the database up with the list of cards
            loadCardDatabase(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d(TAG, "Creating databases");
            createCardDatabase(db);
            db.execSQL(CREATE_TABLE_DECKS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // TODO code to update DB
        }

        /**
         * Performs the initial creation of the database, based on the JSON resource
         */
        protected void loadCardDatabase(SQLiteDatabase db)
        {
            //db.beginTransaction();

            CardDbAdapter cardAdapter = new CardDbAdapter(mContext, db);

            // TODO move the loading/reading of JSON into cardDbAdapter
            List<Card> cards = CardManager.getInstance(mContext).getAllCards();
            for(Card c : cards)
            {
                cardAdapter.insertCard(c);
            }

            //db.endTransaction();
        }
    }

    public DbAdapter(Context context) {

        mContext = context;
        mDbHelper = new DatabaseHelper(context);
    }

    public DbAdapter open() throws SQLException {

        if(mDb == null || !mDb.isOpen())
            mDb = mDbHelper.getWritableDatabase();

        // check if the data file version has changed
        if(PreferenceManager.getDefaultSharedPreferences(mContext).getInt("DATA_FILE", -1) !=
                Constants.CARD_DATA_FILE) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }
            }.execute();

            // data file changed, recreate the data
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Card data has changed, recreating card database");
                    mDb.execSQL("DROP TABLE IF EXISTS " + CardDbAdapter.TABLE_NAME);
                    mDbHelper.createCardDatabase(mDb);

                    // notify listeners that we're ready to go
                    EventBus.getDefault().post(new EventDatabaseReady());
                }
            }).start();

            return null;
        }

        return this;
    }

    public void close() {

        if(mDb != null && mDb.isOpen())
            mDb.close();
    }
}
