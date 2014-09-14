package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

import java.util.List;

/**
 * Created by Eric on 9/9/2014.
 */
public class CardListCursorAdapter extends CursorAdapter {

    public CardListCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.card_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // TODO use view holder
        TextView listItemName = (TextView)view.findViewById(R.id.card_list_item_name);
        listItemName.setText(cursor.getString(cursor.getColumnIndex(CardDbAdapter.NAME)));
    }
}
