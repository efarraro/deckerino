package com.slothwerks.hearthstone.compendiumforhearthstone.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardSet;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Rarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
            return PlayerClass.Neutral;

        try
        {
            return PlayerClass.valueOf(s);

        } catch(Exception e) {

            e.printStackTrace();
        }

        return PlayerClass.Neutral;
    }

    public static int getPrimaryColorForClass(PlayerClass playerClass, Resources resources) {

        switch(playerClass) {
            case Druid:
                return resources.getColor(R.color.druid_primary);
            case Mage:
                return resources.getColor(R.color.mage_primary);
            case Priest:
                return resources.getColor(R.color.priest_primary);
            case Rogue:
                return resources.getColor(R.color.rogue_primary);
            case Paladin:
                return resources.getColor(R.color.paladin_primary);
            case Warrior:
                return resources.getColor(R.color.warrior_primary);
            case Warlock:
                return resources.getColor(R.color.warlock_primary);
            case Shaman:
                return resources.getColor(R.color.shaman_primary);
            case Hunter:
                return resources.getColor(R.color.hunter_primary);
            case Neutral:
                return resources.getColor(R.color.neutral_primary);
            default:
                return 0;
        }
    }

    public static String localizedStringForPlayerClass(PlayerClass playerClass, Context context) {
        switch (playerClass) {

            case Druid:
                return context.getString(R.string.druid);
            case Hunter:
                return context.getString(R.string.hunter);
            case Mage:
                return context.getString(R.string.mage);
            case Neutral:
                return context.getString(R.string.neutral);
            case Paladin:
                return context.getString(R.string.paladin);
            case Priest:
                return context.getString(R.string.priest);
            case Rogue:
                return context.getString(R.string.rogue);
            case Shaman:
                return context.getString(R.string.shaman);
            case Warlock:
                return context.getString(R.string.warlock);
            case Warrior:
                return context.getString(R.string.warrior);
            default:
                return context.getString(R.string.unknown);
        }
    }

    public static ArrayList<PlayerClass> getClassList() {

        return new ArrayList<PlayerClass>(Arrays.asList(
                PlayerClass.Druid,
                PlayerClass.Hunter,
                PlayerClass.Mage,
                PlayerClass.Paladin,
                PlayerClass.Priest,
                PlayerClass.Rogue,
                PlayerClass.Shaman,
                PlayerClass.Warlock,
                PlayerClass.Warrior));
    }
}
