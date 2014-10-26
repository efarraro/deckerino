package com.slothwerks.hearthstone.compendiumforhearthstone.ui.decks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;


public class DeckListFragment extends Fragment implements IntentConstants {


    public DeckListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_deck_list, container, false);
    }

}
