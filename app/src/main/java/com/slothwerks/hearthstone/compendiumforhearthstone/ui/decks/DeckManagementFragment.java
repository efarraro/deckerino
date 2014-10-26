package com.slothwerks.hearthstone.compendiumforhearthstone.ui.decks;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit.ChooseClassActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.view.ViewDeckActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeleteSelectedDeck;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;

import java.sql.SQLException;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/22/2014.
 */
public class DeckManagementFragment extends Fragment implements
        IntentConstants, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    protected DeckManagerCursorAdapter mAdapter;
    protected DeckContextBarHandler mContextBarHandler;
    protected ActionMode mActionMode;
    protected ListView mListView;
    protected TextView mNoDecksTextView;
    protected Button mCreateDeckButton;

    public DeckManagementFragment() {
        mContextBarHandler = new DeckContextBarHandler();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

            mListView = (ListView)rootView.findViewById(R.id.deck_management_list_view);
            mAdapter = new DeckManagerCursorAdapter(getActivity(), cursor);
            mListView.setAdapter(mAdapter);
            mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            mListView.setOnItemClickListener(this);
            mListView.setOnItemLongClickListener(this);

            mNoDecksTextView = (TextView)rootView.findViewById(R.id.deck_management_no_decks);
            mCreateDeckButton =
                    (Button)rootView.findViewById(R.id.deck_management_create_deck_button);

            mCreateDeckButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChooseClassActivity.class);
                    startActivityForResult(intent, 0);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // when a user clicks on the deck, allow them to view that deck
        Intent intent = new Intent(getActivity(), ViewDeckActivity.class);

        // get the selected deck ID and pass
        Cursor c = (Cursor)mAdapter.getItem(position);
        long deckId = c.getLong(c.getColumnIndex(DeckDbAdapter.ROW_ID));
        intent.putExtra(DECK_ID, deckId);

        // clear the selection
        mListView.clearChoices();
        mAdapter.notifyDataSetChanged();

        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        mListView.setItemChecked(position, true);

        mActionMode = getActivity().startActionMode(mContextBarHandler);

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        refresh();
    }

    public void onEventMainThread(EventDeleteSelectedDeck e) {

        // figure out what the selected item is, and delete it
        Deck deck = Deck.fromCursor(
                getActivity(),
                (Cursor)mAdapter.getItem(mListView.getCheckedItemPosition()),
                false);

        try {
            new DeckDbAdapter(getActivity()).deleteDeck(deck);
        } catch(SQLException ex) {
            ex.printStackTrace();
            // TODO show error
            return;
        }

        // get rid of any highlighted item
        mListView.clearChoices();

        // get and display a list of the all the decks to choose from
        refresh();
    }

    protected void refresh()  {

        // refresh the list of decks
        DeckDbAdapter adapter = new DeckDbAdapter(getActivity());
        try {

            adapter = (DeckDbAdapter) adapter.open();

            Cursor cursor = adapter.getAllDecks();
            mAdapter.swapCursor(cursor);

            // notify the adapter that we've removed an item
            mAdapter.notifyDataSetChanged();
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            adapter.close();
        }

        // if there are no decks, show special messaging telling the user to create one
        if(mAdapter.getCount() > 0) {
            mNoDecksTextView.setVisibility(View.GONE);
            mCreateDeckButton.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mNoDecksTextView.setVisibility(View.VISIBLE);
            mCreateDeckButton.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }
}
