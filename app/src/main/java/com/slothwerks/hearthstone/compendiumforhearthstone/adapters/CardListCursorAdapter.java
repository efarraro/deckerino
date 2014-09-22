package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.common.ImageDownloader;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardType;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Rarity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 9/9/2014.
 */
public class CardListCursorAdapter extends CursorAdapter {

    public static final String TAG = "CardListCursorAdapter";
    protected HashMap<String, Integer> mCardIdToQuantityMap;

    public CardListCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
        mCardIdToQuantityMap = new HashMap<String, Integer>();
    }

    public void updateQuantityForCard(Card card, int newQuantity) {
        mCardIdToQuantityMap.put(card.getId(), new Integer(newQuantity));

        notifyDataSetChanged();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //return inflater.inflate(R.layout.card_image_item, parent, false);
        return inflater.inflate(R.layout.card_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Card card = CardDbAdapter.cursorToCard(cursor);

        // TODO use view holder
        TextView listItemName = (TextView)view.findViewById(R.id.card_list_item_name);
        String name = card.getName();
        int quantity = mCardIdToQuantityMap.containsKey(card.getId()) ?
                mCardIdToQuantityMap.get(card.getId()) : 0;
        if(quantity > 0) {
            name += " x " + quantity;
            view.setBackgroundColor(context.getResources().getColor(R.color.primary_light));
        }
        else {
            view.setBackgroundColor(Color.WHITE);
        }
        listItemName.setText(name);

        // get the rarity indicator (vertical colored bar)
        FrameLayout rarityGem =
                (FrameLayout)view.findViewById(R.id.card_list_item_rarity_gem);

        // set the color of the card's rarity indicator depending on rarity
        if(card.getRarity() == Rarity.Epic)
            rarityGem.setBackground(context.getResources().getDrawable(R.drawable.gem_epic));
        else if(card.getRarity() == Rarity.Rare)
            rarityGem.setBackground(context.getResources().getDrawable(R.drawable.gem_rare));
        else if(card.getRarity() == Rarity.Legendary)
            rarityGem.setBackground(context.getResources().getDrawable(R.drawable.gem_legendary));
        else if(card.getRarity() == Rarity.Common)
            rarityGem.setBackground(context.getResources().getDrawable(R.drawable.gem_common));
        else
            rarityGem.setBackgroundColor(Color.TRANSPARENT);

        // set the info for the card
        TextView listItemText = (TextView) view.findViewById(R.id.card_list_item_text);
        if(card.getText() != null) {
            listItemText.setText(Html.fromHtml(card.getText()));
        } else {
            listItemText.setText(new String());
        }

        // set the cost
        TextView costText = (TextView)view.findViewById(R.id.card_list_item_cost);
        costText.setText(String.valueOf(card.getCost()));

        // set attack
        TextView attackText = (TextView)view.findViewById(R.id.card_list_item_attack);
        attackText.setText(String.valueOf(card.getAttack()));

        // set health
        TextView healthText = (TextView)view.findViewById(R.id.card_list_item_health);
        healthText.setText(String.valueOf(card.getHealth()));


        // determine if we should hide the attack/health sections
        FrameLayout attackLayout =
                (FrameLayout)view.findViewById(R.id.card_list_item_attack_container);
        FrameLayout healthLayout =
                (FrameLayout)view.findViewById(R.id.card_list_item_health_container);

        if(card.getHealth() == 0 && card.getAttack() == 0) {

            attackLayout.setVisibility(View.GONE);
            healthLayout.setVisibility(View.GONE);
        }
        else {

            attackLayout.setVisibility(View.VISIBLE);
            healthLayout.setVisibility(View.VISIBLE);
        }
    }
}
