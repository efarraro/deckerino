package com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    public final LinearLayout statBlockLayout;

    public CardItemViewHolder(TextView name, FrameLayout gem, TextView text, TextView cost,
                              TextView attack, TextView health,
                              LinearLayout statLayout) {

        nameTextView = name;
        rarityGem = gem;
        cardTextView = text;
        costText = cost;
        attackText = attack;
        healthCost = health;
        statBlockLayout = statLayout;
    }
}
