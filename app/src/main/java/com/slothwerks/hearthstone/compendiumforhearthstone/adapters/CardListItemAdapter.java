package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

import java.util.List;

/**
 * Created by Eric on 9/9/2014.
 */
public class CardListItemAdapter extends ArrayAdapter<Card> {

    public CardListItemAdapter(Context context, int resource, int textViewResourceId, List<Card> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.card_list_item, parent, false);
        }

        Card card = getItem(position);

        // TODO use view holder pattern
        // set the name
        TextView listItemName = (TextView)convertView.findViewById(R.id.card_list_item_name);
        listItemName.setText(card.getName());

        return convertView;
    }
}
