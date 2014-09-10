package com.slothwerks.hearthstone.compendiumforhearthstone.data;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eric on 9/1/2014.
 */
public class CardManager {

    protected static CardManager sInstance;

    protected ArrayList<Card> mAllCards;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    protected Context mContext;

    protected CardManager(Context context) {
        mAllCards = new ArrayList<Card>();
        mContext = context;

        try {
            loadCardList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException j) {
            j.printStackTrace();
        }
    }

    protected void loadCardList() throws IOException, JSONException
    {
        //AssetManager assetManager = mContext.getAssets();
        InputStream is = mContext.getResources().openRawResource(R.raw.all_sets);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = reader.readLine();
        StringBuffer sb = new StringBuffer();
        while(line != null) {
            sb.append(line);
            line = reader.readLine();
        }

        Log.d("test", sb.toString());

        JSONObject root = new JSONObject(sb.toString());
        Iterator<String> setKeys = root.keys();

        while(setKeys != null && setKeys.hasNext())
        {
            String set = setKeys.next();
            JSONArray cardObjects = root.getJSONArray(set);
            for(int i = 0; i < cardObjects.length(); i++)
            {
                JSONObject cardObject = cardObjects.getJSONObject(i);

                try {

                    Card card = Card.fromJson(cardObject, set);

                    if(card == null)
                    {
                        Log.d("test", cardObject.toString());
                    }

                    if(card.isCollectible())
                        mAllCards.add(card);

                } catch(JSONException e) { e.printStackTrace(); }
            }
        }
    }

    public static CardManager getInstance(Context context)
    {
        if(sInstance == null)
            sInstance = new CardManager(context);

        return sInstance;
    }

    public ArrayList<Card> getAllCards() {
        return mAllCards;
    }

    public void setAllCards(ArrayList<Card> allCards) {
        mAllCards = allCards;
    }

    public Card cardById(String id) {
        for(Card card : mAllCards) {
            if(card.getId().equals(id))
                return card;
        }

        return null;
    }
}
