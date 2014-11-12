package com.slothwerks.hearthstone.compendiumforhearthstone.ui.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.decks.DeckManagementActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit.DeckBuilderActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.track.TrackDeckActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared.DeckListArrayAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventUpdateClassTheme;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import android.net.Uri;

import java.sql.SQLException;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/23/2014.
 */
public class ViewDeckFragment extends Fragment implements IntentConstants {

    protected long mDeckId;
    protected Deck mDeck;
    protected ListView mListView;
    protected DeckListArrayAdapter mDeckListArrayAdapter;
    protected TextView mNoCardsText;
    protected Button mTrackDeckButton;
    protected Button mMakeCopyButton;

    public ViewDeckFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_deck, container, false);

        Uri uri = getActivity().getIntent().getData();
        if(uri != null) {
            try {
                mDeckId = -1;
                mDeck = Deck.fromDeckerinoFormat(getActivity(), uri.toString());
                // TODO need a better way to define a default name
                if(mDeck.getName() == "" || mDeck.getName() == null) {
                    mDeck.setName(String.format(getString(R.string.deck_builder_untitled_deck),
                            Utility.localizedStringForPlayerClass(
                                    mDeck.getPlayerClass(), getActivity())));
                }
            } catch(SQLException e) {
                e.printStackTrace();
                ((BaseActivity)getActivity()).showToast(getString(R.string.fatal_error), e.getMessage());
            }
        }
        else {
            mDeckId = getArguments().getLong(DECK_ID);
            mDeck = new DeckDbAdapter(getActivity()).getDeckById(mDeckId);
        }

        //if(mDeckId == -1) {

            /*((BaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
            ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // }*/

        mListView = (ListView)view.findViewById(R.id.view_deck_listview);

        // set the color of the action bar based on class
        ActionBarActivity a = (ActionBarActivity)getActivity();
        a.getSupportActionBar().
                setBackgroundDrawable(new ColorDrawable(
                        Utility.getPrimaryColorForClass(
                                mDeck.getPlayerClass(), getActivity().getResources())));

        // set up edit deck button
        Button editDeckButton = (Button)view.findViewById(R.id.view_deck_edit_button);
        editDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeckBuilderActivity.class);
                intent.putExtra(DECK_ID, mDeckId);
                startActivity(intent);
            }
        });

        // if coming from a link, there's nothing to go back to
        if(mDeckId == -1)
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        else
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNoCardsText = (TextView)view.findViewById(R.id.view_deck_no_card_text);

        // set up track deck button
        mTrackDeckButton = (Button)view.findViewById(R.id.view_deck_track_button);
        mTrackDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrackDeckActivity.class);
                intent.putExtra(DECK_ID, mDeckId);
                startActivity(intent);
            }
        });

        // hide edit/track if we've come from a deckerino link, and don't have a deck ID yet
        if(mDeckId == -1)
            ((View)view.findViewById(R.id.view_deck_horizontal_top_button_container)).
                    setVisibility(View.GONE);

        // set up the 'make copy' button (when visiting from a deckerino:// link
        mMakeCopyButton = (Button)view.findViewById(R.id.view_deck_make_copy_button);
        if(mDeckId != -1)
            mMakeCopyButton.setVisibility(View.GONE);
        mMakeCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO move this, seems weird to put this kind of code in a click handler
                DeckDbAdapter db = null;
                try {
                    db = new DeckDbAdapter(getActivity());
                    db = (DeckDbAdapter)db.open();
                    long id = db.createEmptyDeck(mDeck.getPlayerClass(), mDeck.getName());
                    mDeck.setId(id);
                    db.updateCardData(mDeck);
                    Intent intent = new Intent(getActivity(), DeckManagementActivity.class);
                    intent.putExtra(DECK_ID, id);
                    startActivity(intent);

                } catch(SQLException e) {
                    e.printStackTrace();
                    ((BaseActivity)getActivity()).showToast(
                            getString(R.string.fatal_error), e.getMessage());
                } finally {
                    if(db != null)
                        db.close();
                }
            }
        });

        refresh();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // request any additional theme work required
        EventBus.getDefault().post(new EventUpdateClassTheme(mDeck.getPlayerClass()));

        // refresh in case deck has been modified
        refresh();
    }

    protected void refresh() {

        if(mDeckId != -1)
            mDeck = new DeckDbAdapter(getActivity()).getDeckById(mDeckId);

        // set up the listview to display the deck
        mDeckListArrayAdapter =
                new DeckListArrayAdapter(getActivity().getApplicationContext(), mDeck.getCards());
        mListView.setAdapter(mDeckListArrayAdapter);

        // if there are no cards, show the message that there are no cards
        if(mDeck.getCardCount() == 0) {
            mNoCardsText.setVisibility(View.VISIBLE);
            mTrackDeckButton.setEnabled(false);
        }
        else
            mNoCardsText.setVisibility(View.GONE);

        // update activity title
        getActivity().setTitle(mDeck.getName());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_deck, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mDeck.toDeckerinoFormat());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}