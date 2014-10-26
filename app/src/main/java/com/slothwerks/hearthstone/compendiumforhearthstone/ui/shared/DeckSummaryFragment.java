package com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventRequestDisplayDeck;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DeckSummaryFragment extends Fragment {

    public DeckSummaryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_deck_summary, container, false);

        // when the user clicks 'show deck', send an event notifying listeners to show the deck
        Button showDeckButton = (Button)view.findViewById(R.id.deck_summary_show_deck_button);
        showDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventRequestDisplayDeck());
            }
        });

        return view;
    }

    public void onEventMainThread(EventDeckUpdated e) {
        TextView cardCountText = (TextView)getView().findViewById(R.id.deck_summary_card_count);
        cardCountText.setText(e.getDeck().getCards().size() + "/ 30");

        // TODO L10N
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }
}
