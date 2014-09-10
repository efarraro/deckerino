package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.CardListItemAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.CardManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        final ArrayAdapter<Card> adapter =
                new CardListItemAdapter(
                        getActivity(), R.layout.card_list_item, 0,
                        CardManager.getInstance(getActivity()).getAllCards());

        ListView listView = (ListView)v.findViewById(R.id.main_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // show the detail page for the selected card
                Card card = (Card)adapter.getItem(position);
                Intent intent = new Intent(getActivity(), CardDetail.class);
                intent.putExtra(Card.CARD_ID, card.getId());
                startActivity(intent);
            }
        });

        return v;
    }
}
