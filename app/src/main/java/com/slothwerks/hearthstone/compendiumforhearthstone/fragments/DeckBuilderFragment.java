package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.DeckBuilderPagerAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardQuantityUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventUpdateClassTheme;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import junit.framework.Assert;

import java.sql.SQLException;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/17/2014.
 */
public class DeckBuilderFragment extends Fragment implements IntentConstants {

    protected Deck mDeck;
    protected long mDeckId;

    public DeckBuilderFragment() {

        mDeck = new Deck();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Assert.assertTrue("Expected deck_id bundle arg", getArguments().containsKey(DECK_ID));
        mDeckId = getArguments().getLong(DECK_ID);
        mDeck = new DeckDbAdapter(getActivity().getApplicationContext()).getDeckById(mDeckId);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deck_builder, container, false);

        ViewPager pager = (ViewPager)rootView.findViewById(R.id.deck_list_pager);
        pager.setAdapter(new DeckBuilderPagerAdapter(
                getActivity().getSupportFragmentManager(), mDeck.getPlayerClass(), mDeckId));

        PagerSlidingTabStrip tabs =
                (PagerSlidingTabStrip)rootView.findViewById(R.id.deck_builder_tab_strip);
        tabs.setBackground(new ColorDrawable(Utility.getPrimaryColorForClass(
                mDeck.getPlayerClass(), getResources())));
        tabs.setTextColor(Color.WHITE);
        tabs.setIndicatorColor(Color.WHITE);
        tabs.setViewPager(pager);

        return rootView;
    }

    public Deck getDeck() {
        return mDeck;
    }

    public void onEventMainThread(EventCardTapped e) {

        // add card to deck, if possible
        if(mDeck.canAddToDeck(e.getCard())) {
            mDeck.addToDeck(e.getCard());
        }
        else {
            // if we couldn't add the card due to quantity constraints, set the quantity to 0
            // allows the user to repeatedly tap a card to add/remove to get the quanity they want
            mDeck.removeAllCopies(e.getCard());

            // show a toast if the user tries to add too many cards
            if(mDeck.getCardCount() == 30) {
                Toast.makeText(getActivity(),
                        R.string.deck_builder_too_many_cards, Toast.LENGTH_SHORT).show();
            }
        }

        // notify listeners that the current deck has been updated
        EventBus.getDefault().post(new EventDeckUpdated(mDeck));

        // notify listeners which card was updated, specifically
        EventBus.getDefault().post(new EventCardQuantityUpdated
                (e.getCard(), mDeck.getCountForCard(e.getCard())));

        // update the database in realtime with the new card data (performance impact?)
        DeckDbAdapter deckDbAdapter = null;
        try {
            deckDbAdapter = (DeckDbAdapter)new DeckDbAdapter(getActivity()).open();
            deckDbAdapter.updateCardData(mDeck);

        } catch(SQLException ex) {

            ex.printStackTrace();
        } finally {
            if(deckDbAdapter != null)
                deckDbAdapter.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        // let listeners know that we've resumed, and that they should update their deck pointer
        EventBus.getDefault().post(new EventDeckUpdated(mDeck));

        // request any additional theme work required
        EventBus.getDefault().post(new EventUpdateClassTheme(mDeck.getPlayerClass()));
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.deck_builder, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        if(item.getItemId() == R.id.deck_builder_action_name_deck) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            View view = inflater.inflate(R.layout.popup_name_deck, null, false);
            final TextView deckNameText = (TextView)view.findViewById(R.id.popup_name_deck_text);
            deckNameText.setText(mDeck.getName());

            final AlertDialog dialog =
                    builder.setTitle(getString(R.string.deck_builder_menu_name_deck)).
                            setView(view).
                            setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).
                            setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

                                // user clicked OK, save the deck
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDeck.setName(deckNameText.getText().toString());

                                    try {
                                        new DeckDbAdapter(getActivity()).updateDeckName(mDeck);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(),
                                                getString(R.string.error_unable_to_save),
                                                Toast.LENGTH_SHORT);
                                    }

                                    dialog.dismiss();
                                }
                            }).
                            create();

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}