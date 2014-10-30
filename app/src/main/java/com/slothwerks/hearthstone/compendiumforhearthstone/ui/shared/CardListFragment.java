package com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardQuantityUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardQuantityPair;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

import java.sql.SQLException;

import de.greenrobot.event.EventBus;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CardListFragment extends Fragment implements IntentConstants {

    public static final String TAG = "MainFragment";

    public static final String PLAYER_CLASS = "PLAYER_CLASS";

    protected CardDbAdapter mCardDbAdapter;
    protected StickyListHeadersListView mListView;
    protected PlayerClass mPlayerClass;

    public CardListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_card_list, container, false);

        Cursor cursor = null;

        // get a list of all the cards from the database
        // TODO handle this in the DbAdapter class for cards, make it easier to get this data
        try {
            mCardDbAdapter = (CardDbAdapter) new CardDbAdapter(getActivity()).open();

        } catch (SQLException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.fatal_error));
            builder.setMessage(getString(R.string.error_sql_unable_to_open_db));
            builder.show();
        }

        Bundle args = getArguments();
        String playerClassStr = args.getString(PLAYER_CLASS);
        mPlayerClass = PlayerClass.Neutral;
        if(playerClassStr != null)
            mPlayerClass = PlayerClass.valueOf(playerClassStr);

        long deckId = args.getLong(DECK_ID, -1);

        //cursor = mCardDbAdapter.getAllCards();
        cursor = mCardDbAdapter.getCardsByClass(mPlayerClass);

        // set up the adapter for the list view
        final CardListCursorAdapter adapter =
                new CardListCursorAdapter(getActivity(), cursor);

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                Log.d(TAG, "Searching for " + constraint.toString());
                return mCardDbAdapter.getCardsLike(constraint.toString(), mPlayerClass);
            }
        });

        mListView = (StickyListHeadersListView)v.findViewById(R.id.main_list_view);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Card card = CardDbAdapter.cursorToCard(
                        (Cursor)mListView.getAdapter().getItem(position));

                EventBus.getDefault().post(new EventCardTapped(card));
            }
        });

        // set up the search box
        final EditText searchTextView = (EditText)v.findViewById(R.id.card_list_search_box);
        searchTextView.setHint(
                String.format(getString(R.string.card_list_search_hint), mPlayerClass.toString()));

        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(getUserVisibleHint()) {
                    CardListCursorAdapter cursorAdapter =
                            (CardListCursorAdapter) mListView.getAdapter();

                    cursorAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        // highlight the cards, if we're loading this card list with a deck
        if(deckId != -1) {
            Deck deck = new DeckDbAdapter(getActivity()).getDeckById(deckId);
            for(CardQuantityPair pair : deck.getCards()) {
                adapter.updateQuantityForCard(pair.getCard(), pair.getQuantity());
            }
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, mPlayerClass.toString() + getUserVisibleHint());

        EventBus.getDefault().register(this);

        // open a DB connection
        if(mCardDbAdapter == null) {
            try {
                mCardDbAdapter = (CardDbAdapter) new CardDbAdapter(getActivity()).open();

            } catch (SQLException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.fatal_error));
                builder.setMessage(getString(R.string.error_sql_unable_to_open_db));
                builder.show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);

        // if we have a DB connection, close it on pause
        if(mCardDbAdapter != null)
            mCardDbAdapter.close();;
    }

    public void onEventMainThread(EventCardQuantityUpdated e) {

        CardListCursorAdapter adapter = (CardListCursorAdapter)mListView.getAdapter();
        adapter.updateQuantityForCard(e.getCard(), e.getQuantity());
    }
}
