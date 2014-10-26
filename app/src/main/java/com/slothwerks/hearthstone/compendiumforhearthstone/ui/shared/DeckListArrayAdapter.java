package com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardQuantityPair;

import java.util.List;

/**
 * Created by Eric on 9/20/2014.
 */
public class DeckListArrayAdapter extends ArrayAdapter<CardQuantityPair> {

    public DeckListArrayAdapter(Context context, List<CardQuantityPair> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item_deck_list, parent, false);
        }

        Card card = getItem(position).getCard();

        // show the card name
        TextView name = (TextView)convertView.findViewById(R.id.list_item_deck_list_name);
        name.setText(card.getName());

        // cards with 0 quantity show as 'disabled'
        if(getItem(position).getQuantity() == 0) {
            name.setTextColor(getContext().getResources().getColor(R.color.disabled));
        } else {
            name.setTextColor(getContext().getResources().getColor(R.color.default_text));
        }

        // show the cost for this card
        TextView cost = (TextView)convertView.findViewById(R.id.list_item_deck_list_cost);
        cost.setText(Integer.toString(card.getCost()));

        // show the quantity
        TextView quantityText = (TextView)convertView.
                findViewById(R.id.list_item_deck_list_quantity);
        quantityText.setText(Integer.toString(getItem(position).getQuantity()));


        return convertView;
    }
}
