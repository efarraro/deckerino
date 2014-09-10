package com.slothwerks.hearthstone.compendiumforhearthstone.data;

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

    protected CollectionManager() {
        mCardToQuantityMap = new HashMap<String, Integer>();
    }

    public static CollectionManager getInstance() {

        if(sInstance == null) {
            sInstance = new CollectionManager();
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
}
