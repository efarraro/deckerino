package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.CardListCursorAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

import java.sql.SQLException;

import de.greenrobot.event.EventBus;

public class CardListFragment extends Fragment {

    public static final String TAG = "MainFragment";

    public static final String PLAYER_CLASS = "PLAYER_CLASS";

    protected CardDbAdapter mCardDbAdapter;

    public CardListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mCardDbAdapter = (CardDbAdapter) new CardDbAdapter(getActivity()).open();

        } catch(SQLException e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.fatal_error));
            builder.setMessage(getString(R.string.error_sql_unable_to_open_db));
            builder.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_card_list, container, false);

        Cursor cursor = null;

        // get a list of all the cards from the database

        Bundle args = getArguments();
        String playerClassStr = args.getString(PLAYER_CLASS);
        PlayerClass playerClass = PlayerClass.Neutral;
        if(playerClassStr != null)
            playerClass = PlayerClass.valueOf(playerClassStr);

        //cursor = mCardDbAdapter.getAllCards();
        cursor = mCardDbAdapter.getCardsByClass(playerClass);

        // set up the adapter for the list view
        final CardListCursorAdapter adapter =
                new CardListCursorAdapter(getActivity(), cursor);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                Log.d(TAG, "Searching for " + constraint.toString());
                return mCardDbAdapter.getCardsLike(constraint.toString());
            }
        });

        final ListView listView = (ListView)v.findViewById(R.id.main_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Card card = CardDbAdapter.cursorToCard(
                        (Cursor)listView.getAdapter().getItem(position));

                EventBus.getDefault().post(new EventCardTapped(card));
            }
        });

        // set up the search view
        /*SearchView searchView = (SearchView)v.findViewById(R.id.main_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                CardListCursorAdapter cursorAdapter =
                        (CardListCursorAdapter)listView.getAdapter();

                cursorAdapter.getFilter().filter(newText);

                return true;
            }
        });*/

        return v;
    }
}
