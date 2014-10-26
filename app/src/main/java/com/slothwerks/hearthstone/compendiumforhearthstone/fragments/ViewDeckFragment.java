package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.activities.DeckBuilderActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.activities.TrackDeckActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.CardListCursorAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.DeckListArrayAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventUpdateClassTheme;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/23/2014.
 */
public class ViewDeckFragment extends Fragment implements IntentConstants {

    protected Deck mDeck;

    public ViewDeckFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_deck, container, false);

        // retrieve the deck that we're interested in
        long deckId = getArguments().getLong(DECK_ID);
        mDeck = new DeckDbAdapter(getActivity().getApplicationContext()).getDeckById(deckId);

        // set the name for this activity to that of the deck
        getActivity().setTitle(mDeck.getName());

        // set the color of the action bar based on class
        ActionBarActivity a = (ActionBarActivity)getActivity();
        a.getSupportActionBar().
                setBackgroundDrawable(new ColorDrawable(
                        Utility.getPrimaryColorForClass(
                                mDeck.getPlayerClass(), getActivity().getResources())));

        // set up the listview to display the deck
        ListView listView = (ListView)view.findViewById(R.id.view_deck_listview);
        DeckListArrayAdapter adapter =
                new DeckListArrayAdapter(getActivity().getApplicationContext(), mDeck.getCards());
        listView.setAdapter(adapter);

        // set up edit deck button
        Button editDeckButton = (Button)view.findViewById(R.id.view_deck_edit_button);
        editDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeckBuilderActivity.class);
                intent.putExtra(DECK_ID, mDeck.getId());
                startActivity(intent);
            }
        });

        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set up track deck button
        Button trackDeckButton = (Button)view.findViewById(R.id.view_deck_track_button);
        trackDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrackDeckActivity.class);
                intent.putExtra(DECK_ID, mDeck.getId());
                startActivity(intent);
            }
        });

        // if there are no cards, show the message that there are no cards
        TextView text = (TextView)view.findViewById(R.id.view_deck_no_card_text);
        if(mDeck.getCardCount() == 0) {
            text.setVisibility(View.VISIBLE);
            trackDeckButton.setEnabled(false);
        }
        else
            text.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // request any additional theme work required
        EventBus.getDefault().post(new EventUpdateClassTheme(mDeck.getPlayerClass()));

        // TODO refresh cursor after resuming
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}