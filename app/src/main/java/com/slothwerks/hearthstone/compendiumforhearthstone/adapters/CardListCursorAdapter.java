package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.common.ImageDownloader;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Rarity;

import java.util.List;

/**
 * Created by Eric on 9/9/2014.
 */
public class CardListCursorAdapter extends CursorAdapter implements Filterable {

    public static final String TAG = "CardListCursorAdapter";
    protected ImageDownloader<ImageView> mImageDownloadThread;

    public CardListCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
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
        if(card.getPlayerClass() != null)
            listItemName.setText(card.getName());
        else
            listItemName.setText(card.getName());

        // set the color of the card's name depending on rarity
        if(card.getRarity() == Rarity.Epic)
            listItemName.setTextColor(context.getResources().getColor(R.color.epic));
        else if(card.getRarity() == Rarity.Rare)
            listItemName.setTextColor(context.getResources().getColor(R.color.rare));
        else if(card.getRarity() == Rarity.Legendary)
            listItemName.setTextColor(context.getResources().getColor(R.color.legendary));
        else if(card.getRarity() == Rarity.Common)
            listItemName.setTextColor(context.getResources().getColor(R.color.common));

        if(card.getText() != null) {
            TextView listItemText = (TextView) view.findViewById(R.id.card_list_item_text);
            listItemText.setText(Html.fromHtml(card.getText()));
        }

        /*Card card = CardDbAdapter.cursorToCard(cursor);

        ImageView imageView = (ImageView)view.findViewById(R.id.card_image_item_imageview);
        mImageDownloadThread.queueImage(imageView, card.getImageUrl());*/
    }
}
