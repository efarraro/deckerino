package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.DeckListArrayAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardQuantityPair;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 10/4/2014.
 */
public class TrackDeckFragment extends Fragment implements IntentConstants {

    protected Deck mDeck;

    public TrackDeckFragment() {
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

        // tapping on one of the cards should decrement the number of remaining cards in the deck
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CardQuantityPair pair = (CardQuantityPair)parent.getItemAtPosition(position);
                pair.updateQuantity(-1);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EventCardTapped e) {
        Log.d("TEST", "here");
    }*/
}
