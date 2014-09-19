package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck_summary, container, false);
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
