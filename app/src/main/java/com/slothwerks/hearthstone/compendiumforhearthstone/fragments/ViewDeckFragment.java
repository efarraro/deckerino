package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;

/**
 * Created by Eric on 9/23/2014.
 */
public class ViewDeckFragment extends Fragment {

    public ViewDeckFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_deck, container, false);
        return rootView;
    }
}