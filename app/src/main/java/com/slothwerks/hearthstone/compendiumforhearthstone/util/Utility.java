package com.slothwerks.hearthstone.compendiumforhearthstone.util;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardSet;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Rarity;

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

    public static PlayerClass stringToPlayerClass(String s)
    {
        if(s == null)
            return PlayerClass.None;

        try
        {
            return PlayerClass.valueOf(s);

        } catch(Exception e) {

            e.printStackTrace();
        }

        return PlayerClass.Unknown;
    }
}
