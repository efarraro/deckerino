package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.activities.ViewDeckActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.DeckManagerCursorAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;

import java.sql.SQLException;

/**
 * Created by Eric on 9/22/2014.
 */
public class DeckManagementFragment extends Fragment {

    public DeckManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck_management, container, false);

        DeckDbAdapter adapter = new DeckDbAdapter(getActivity());

        // get and display a list of the all the decks to choose from
        try {

            adapter = (DeckDbAdapter)adapter.open();

            Cursor cursor = adapter.getAllDecks();

            ListView listView = (ListView)rootView.findViewById(R.id.deck_management_list_view);
            listView.setAdapter(new DeckManagerCursorAdapter(getActivity(), cursor));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    // TODO get ID of deck to pull from DB

                    // when a user clicks on the deck, allow them to view that deck
                    Intent intent = new Intent(getActivity(), ViewDeckActivity.class);
                    startActivity(intent);
                }
            });

        }  catch(SQLException e) {
            e.printStackTrace();
            // TODO fatal error
        } finally {
            adapter.close();
        }

        return rootView;
    }
}
