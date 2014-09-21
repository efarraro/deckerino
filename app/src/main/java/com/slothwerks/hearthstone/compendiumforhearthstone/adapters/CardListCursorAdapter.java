package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
public class CardListCursorAdapter extends CursorAdapter implements Filterable {

    public static final String TAG = "CardListCursorAdapter";
    protected ImageDownloader<ImageView> mImageDownloadThread;
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
            view.setBackgroundColor(0xffd0d9ff);
        }
        else {
            view.setBackgroundColor(Color.WHITE);
        }
        listItemName.setText(name);

        // set the color of the card's name depending on rarity
        if(card.getRarity() == Rarity.Epic)
            listItemName.setTextColor(context.getResources().getColor(R.color.epic));
        else if(card.getRarity() == Rarity.Rare)
            listItemName.setTextColor(context.getResources().getColor(R.color.rare));
        else if(card.getRarity() == Rarity.Legendary)
            listItemName.setTextColor(context.getResources().getColor(R.color.legendary));
        else if(card.getRarity() == Rarity.Common)
            listItemName.setTextColor(context.getResources().getColor(R.color.common));
        else
            listItemName.setTextColor(context.getResources().getColor(R.color.basic));

        if(card.getText() != null) {
            TextView listItemText = (TextView) view.findViewById(R.id.card_list_item_text);
            listItemText.setText(Html.fromHtml(card.getText()));
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

        /*Card card = CardDbAdapter.cursorToCard(cursor);

        ImageView imageView = (ImageView)view.findViewById(R.id.card_image_item_imageview);
        mImageDownloadThread.queueImage(imageView, card.getImageUrl());*/
    }
}
