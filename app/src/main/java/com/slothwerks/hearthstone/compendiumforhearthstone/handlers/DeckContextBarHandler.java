package com.slothwerks.hearthstone.compendiumforhearthstone.handlers;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Eric on 10/6/2014.
 */
public class DeckContextBarHandler implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater=  mode.getMenuInflater();

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        /*switch(item.getItemId()) {
            // case R
            return true;
        }*/

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
