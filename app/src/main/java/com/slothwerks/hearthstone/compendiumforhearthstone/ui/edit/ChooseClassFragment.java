package com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit.ChooseClassActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

/**
 * Created by Eric on 9/21/2014.
 */
public class ChooseClassFragment extends Fragment {

    public ChooseClassFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_class, container, false);

        final ListView listView = (ListView)rootView.findViewById(R.id.choose_class_list_view);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,
                Utility.getClassListAsLocalizedStrings(getActivity()));

        listView.setAdapter(adapter);

        // add a click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String playerClassString = adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(ChooseClassActivity.PLAYER_CLASS, playerClassString);
                getActivity().setResult(0, intent);
                getActivity().finish();
            }
        });

        return rootView;
    }
}
