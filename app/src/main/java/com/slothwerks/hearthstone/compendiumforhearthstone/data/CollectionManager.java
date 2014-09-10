package com.slothwerks.hearthstone.compendiumforhearthstone.data;

import android.content.Context;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 9/7/2014.
 */
public class CollectionManager {

    protected static CollectionManager sInstance;
    protected HashMap<String, Integer> mCardToQuantityMap;
    protected Context mContext;

    protected CollectionManager(Context context) {

        mContext = context;
        mCardToQuantityMap = new HashMap<String, Integer>();
        initWithCardList(CardManager.getInstance(context).getAllCards());
    }

    public static CollectionManager getInstance(Context context) {

        if(sInstance == null) {
            sInstance = new CollectionManager(context);
        }

        return sInstance;
    }

    public void initWithCardList(List<Card> cardList) {
        mCardToQuantityMap = new HashMap<String, Integer>();

        for(Card card : cardList) {
            mCardToQuantityMap.put(card.getId(), 0);
        }
    }

    public int quantityForCardId(String id) {

        return mCardToQuantityMap.get(id);
    }

    public void addQuantityForId(int quantity, String id) {
        mCardToQuantityMap.put(id, quantity + mCardToQuantityMap.get(id));
    }
}
