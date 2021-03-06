package com.slothwerks.hearthstone.compendiumforhearthstone.ui.decks;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import java.sql.SQLException;

/**
 * Created by Eric on 9/22/2014.
 */
public class DeckManagerCursorAdapter extends CursorAdapter {

    public DeckManagerCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.list_item_deck_managment_deck, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        try {
            Deck deck = Deck.fromCursor(context, cursor, true);

            // change the color of the square based on the class
            FrameLayout classSquare =
                    (FrameLayout) view.findViewById(R.id.list_item_deck_management_square);
            classSquare.setBackground(new ColorDrawable(
                    Utility.getPrimaryColorForClass(deck.getPlayerClass(), context.getResources())));

            // set the "class letter"
            TextView playerLetterText = (TextView) view.findViewById(
                    R.id.list_item_deck_management_class_letter);
            playerLetterText.setText(Utility.localizedStringForPlayerClass(
                    deck.getPlayerClass(), context).substring(0, 1));

            // set the name of the player class
            TextView classText = (TextView) view.findViewById(R.id.list_item_deck_management_class);
            classText.setText(Utility.localizedStringForPlayerClass(
                    deck.getPlayerClass(), context).toUpperCase());

            // set the deck name
            TextView deckNameText = (TextView) view.findViewById(R.id.list_item_deck_management_title);
            deckNameText.setText(deck.getName());

        } catch(SQLException e) {
            e.printStackTrace();
            // nothing we can really do at this point if we can't get data from the database
            // (hopefully, this should never happen)
        }
    }
}
