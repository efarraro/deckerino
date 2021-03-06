package com.slothwerks.hearthstone.compendiumforhearthstone.ui.track;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared.DeckListArrayAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventUpdateClassTheme;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardQuantityPair;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 10/4/2014.
 */
public class TrackDeckFragment extends Fragment implements IntentConstants {

    protected Deck mDeck;
    protected TextView mCardsRemainingTextView;

    public TrackDeckFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_deck, container, false);

        // retrieve the deck that we're interested in
        long deckId = getArguments().getLong(DECK_ID);
        mDeck = new DeckDbAdapter(getActivity().getApplicationContext()).getDeckById(deckId);

        // set up the listview to display the deck
        ListView listView = (ListView)view.findViewById(R.id.view_deck_listview);
        final DeckListArrayAdapter adapter =
                new DeckListArrayAdapter(getActivity().getApplicationContext(), mDeck.getCards());
        listView.setAdapter(adapter);

        // show the number of cards remaining in the deck
        mCardsRemainingTextView = (TextView)view.findViewById(R.id.track_deck_cards_left);
        mCardsRemainingTextView.setText(String.format(getString(
                R.string.track_deck_cards_remaining), mDeck.getCardCount(), 30));

        // set the name for this activity to that of the deck
        getActivity().setTitle(mDeck.getName());

        // tapping on one of the cards should decrement the number of remaining cards in the deck
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CardQuantityPair pair = (CardQuantityPair)parent.getItemAtPosition(position);
                pair.updateQuantity(-1);
                adapter.notifyDataSetChanged();

                if(mCardsRemainingTextView != null)
                    mCardsRemainingTextView.setText(String.format(getString(
                            R.string.track_deck_cards_remaining), mDeck.getCardCount(), 30));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // set the color of the action bar based on class
        ((BaseActivity)getActivity()).getSupportActionBar().
                setBackgroundDrawable(new ColorDrawable(Utility.getPrimaryColorForClass(
                        mDeck.getPlayerClass(), getActivity().getResources())));

        // request any additional theme work required
        EventBus.getDefault().post(new EventUpdateClassTheme(mDeck.getPlayerClass()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }
}
