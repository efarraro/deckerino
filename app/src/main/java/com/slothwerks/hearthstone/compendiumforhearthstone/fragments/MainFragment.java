package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.CardListCursorAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.CardManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CollectionDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

import java.sql.SQLException;

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        Cursor cursor = null;

        // get a list of all the cards from the database
        try {
            CardDbAdapter adapter = (CardDbAdapter) new CardDbAdapter(getActivity()).open();
            cursor = adapter.getAllCards();

        } catch(SQLException e)
        {
           e.printStackTrace();

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.fatal_error);
            dialog.setMessage(R.string.error_sql_unable_to_open_db);
            dialog.show();

            getActivity().finish();
        }

        // set up the adapter for the list view
        final CardListCursorAdapter adapter =
                new CardListCursorAdapter(getActivity(), cursor);

        ListView listView = (ListView)v.findViewById(R.id.main_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // show the detail page for the selected card when an item is clicked
                Card card = CardDbAdapter.cursorToCard((Cursor) adapter.getItem(position));
                Intent intent = new Intent(getActivity(), CardDetail.class);
                intent.putExtra(Card.CARD_ID, card.getId());
                startActivity(intent);
            }
        });

        // set up the search view
        SearchView searchView = (SearchView)v.findViewById(R.id.main_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {
                    CardDbAdapter adapter = (CardDbAdapter) new CardDbAdapter(getActivity()).open();

                    Cursor c = adapter.findCardsLike(newText);
                    c.moveToFirst();

                    while(!c.isAfterLast() && !c.isBeforeFirst())
                    {
                        Log.d(TAG, c.getString(c.getColumnIndex("name")));
                        c.moveToNext();
                    }

                    adapter.close();

                } catch(SQLException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        return v;
    }
}
