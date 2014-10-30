package com.slothwerks.hearthstone.compendiumforhearthstone.ui.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit.DeckBuilderActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.track.TrackDeckActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared.DeckListArrayAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventUpdateClassTheme;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/23/2014.
 */
public class ViewDeckFragment extends Fragment implements IntentConstants {

    protected long mDeckId;
    protected Deck mDeck;
    protected ListView mListView;
    protected DeckListArrayAdapter mDeckListArrayAdapter;
    protected TextView mNoCardsText;
    protected Button mTrackDeckButton;

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

        mDeckId = getArguments().getLong(DECK_ID);
        mDeck = new DeckDbAdapter(getActivity()).getDeckById(mDeckId);

        mListView = (ListView)view.findViewById(R.id.view_deck_listview);

        // set the color of the action bar based on class
        ActionBarActivity a = (ActionBarActivity)getActivity();
        a.getSupportActionBar().
                setBackgroundDrawable(new ColorDrawable(
                        Utility.getPrimaryColorForClass(
                                mDeck.getPlayerClass(), getActivity().getResources())));

        // set up edit deck button
        Button editDeckButton = (Button)view.findViewById(R.id.view_deck_edit_button);
        editDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeckBuilderActivity.class);
                intent.putExtra(DECK_ID, mDeckId);
                startActivity(intent);
            }
        });

        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNoCardsText = (TextView)view.findViewById(R.id.view_deck_no_card_text);

        // set up track deck button
        mTrackDeckButton = (Button)view.findViewById(R.id.view_deck_track_button);
        mTrackDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrackDeckActivity.class);
                intent.putExtra(DECK_ID, mDeckId);
                startActivity(intent);
            }
        });

        refresh();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // request any additional theme work required
        EventBus.getDefault().post(new EventUpdateClassTheme(mDeck.getPlayerClass()));

        // refresh in case deck has been modified
        refresh();
    }

    protected void refresh() {

        mDeck = new DeckDbAdapter(getActivity()).getDeckById(mDeckId);

        // set up the listview to display the deck
        mDeckListArrayAdapter =
                new DeckListArrayAdapter(getActivity().getApplicationContext(), mDeck.getCards());
        mListView.setAdapter(mDeckListArrayAdapter);

        // if there are no cards, show the message that there are no cards
        if(mDeck.getCardCount() == 0) {
            mNoCardsText.setVisibility(View.VISIBLE);
            mTrackDeckButton.setEnabled(false);
        }
        else
            mNoCardsText.setVisibility(View.GONE);

        // update activity title
        getActivity().setTitle(mDeck.getName());

    }
}