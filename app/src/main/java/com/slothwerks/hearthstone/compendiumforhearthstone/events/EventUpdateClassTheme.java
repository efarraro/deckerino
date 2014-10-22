package com.slothwerks.hearthstone.compendiumforhearthstone.events;

import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

/**
 * Created by Eric on 10/22/2014.
 */
public class EventUpdateClassTheme {
    protected PlayerClass mPlayerClass;

    public EventUpdateClassTheme(PlayerClass playerClass) {
        mPlayerClass = playerClass;
    }

    public PlayerClass getPlayerClass() {
        return mPlayerClass;
    }

    public void setClass(PlayerClass aClass) {
        mPlayerClass = aClass;
    }
}
