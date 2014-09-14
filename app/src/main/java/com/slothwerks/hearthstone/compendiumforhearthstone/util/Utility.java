package com.slothwerks.hearthstone.compendiumforhearthstone.util;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardSet;

/**
 * Created by Eric on 9/14/2014.
 */
public class Utility {

    public static CardSet stringToCardSet(String s)
    {
        if(s.equals("Curse of Naxxramas"))
        {
            return CardSet.Naxxramas;
        }


        return CardSet.Debug;
    }
}
