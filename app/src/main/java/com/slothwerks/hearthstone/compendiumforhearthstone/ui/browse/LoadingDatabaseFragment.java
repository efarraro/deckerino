package com.slothwerks.hearthstone.compendiumforhearthstone.ui.browse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;

/**
 * Created by Eric on 11/3/2014.
 */
public class LoadingDatabaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_database, container, false);

        return view;
    }
}
