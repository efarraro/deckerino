package com.slothwerks.hearthstone.compendiumforhearthstone.view;

import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Eric on 9/25/2014.
 */
public class CardItemViewHolder {
    public final TextView nameTextView;
    public final FrameLayout rarityGem;
    public final TextView cardTextView;
    public final TextView costText;
    public final TextView attackText;
    public final TextView healthCost;
    public final FrameLayout attackLayout;
    public final FrameLayout healthLayout;

    public CardItemViewHolder(TextView name, FrameLayout gem, TextView text, TextView cost,
                              TextView attack, TextView health,
                              FrameLayout atkLayout, FrameLayout hLayout) {

        nameTextView = name;
        rarityGem = gem;
        cardTextView = text;
        costText = cost;
        attackText = attack;
        healthCost = health;
        attackLayout = atkLayout;
        healthLayout = hLayout;
    }
}
