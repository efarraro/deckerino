package com.slothwerks.hearthstone.compendiumforhearthstone.ui.decks;

import android.os.Debug;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeleteSelectedDeck;

import junit.framework.Assert;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 10/6/2014.
 */
public class DeckContextBarHandler implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        MenuInflater inflater=  mode.getMenuInflater();
        inflater.inflate(R.menu.deck_management_context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_deck_management_delete_deck:
                EventBus.getDefault().post(new EventDeleteSelectedDeck());
                mode.finish();
                return true;
        }

        throw new UnsupportedOperationException("Unexpected action");
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
