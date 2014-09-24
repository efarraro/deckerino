package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.nav.NavDrawerListAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.CardListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItem;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItemType;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Eric on 9/21/2014.
 */
public class BaseFragmentActivity extends FragmentActivity implements IntentConstants {

    protected ActionBarDrawerToggle mToggle;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // expects a DrawerLayout to be present
        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        // TODO localize these
        // expects a ListView called left_drawer to be present
        final ListView drawer = (ListView)findViewById(R.id.left_drawer);

        Assert.assertTrue("Expected View with ID R.id.left_drawer!", drawer != null);

        ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
        items.add(new NavDrawerItem("Main", NavDrawerItemType.Title));
        items.add(new NavDrawerItem("Browse Cards", NavDrawerItemType.Nav));
        items.add(new NavDrawerItem("Deck", NavDrawerItemType.Title));
        items.add(new NavDrawerItem("Create", NavDrawerItemType.Nav));
        items.add(new NavDrawerItem("Manage", NavDrawerItemType.Nav));
        items.add(new NavDrawerItem("Utilities", NavDrawerItemType.Title));
        items.add(new NavDrawerItem("Deck Tracker", NavDrawerItemType.Nav));
        items.add(new NavDrawerItem("About", NavDrawerItemType.Nav));
        NavDrawerListAdapter ad = new NavDrawerListAdapter(this, items);
        drawer.setAdapter(ad);

        // listen to clicks for navigation
        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawer.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawer);

                NavDrawerItem item = (NavDrawerItem)drawer.getAdapter().getItem(position);

                // TODO consider effect of localization
                if(item.getTitle().equals("Create")) {
                    Intent intent = new Intent(getBaseContext(), ChooseClassActivity.class);
                    startActivityForResult(intent, 0);
                }
                else if(item.getTitle().equals("Manage")) {
                    Intent intent = new Intent(getBaseContext(), DeckManagementActivity.class);
                    startActivity(intent);
                }
            }
        });

        // TODO purpose of open/close strings?  update: accessibility
        mToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.drawable.ic_navigation_drawer,
                R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(mToggle);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // this was required to get the proper icon to show
        //http://stackoverflow.com/questions/17825629/android-drawerlayout-doesnt-show-the-right-indicator-icon
        mToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // for now, the only result we expect here is to select a class
        // TODO but at some point, we probably need to check the request code

        // no data -- eg: user hit the back button instead of selecting a class
        if(data == null)
            return;

        String playerClassStr = data.getStringExtra(ChooseClassActivity.PLAYER_CLASS);
        if(playerClassStr == null)
            return;

        PlayerClass selectedClass = PlayerClass.valueOf(playerClassStr);
        long id = -1;

        // create a deck in the database
        // TODO is this too much for this class?
        // TODO refactor this into DeckBuilder and specify 'create deck' action in bundle
        try {
            DeckDbAdapter a = (DeckDbAdapter) new DeckDbAdapter(this).open();

            id = a.createEmptyDeck(selectedClass);

            Log.d("TEST", "new deck " + id);

            Cursor allDeckCursor = a.getAllDecks();
            allDeckCursor.moveToFirst();
            while(true) {

                Log.d("z", "Z: " + allDeckCursor.getString(allDeckCursor.getColumnIndex(DeckDbAdapter.ROW_ID)) + " " + allDeckCursor.getString(1) + " " + allDeckCursor.getString(2) + " " + allDeckCursor.getString(4));

                if(allDeckCursor.isLast())
                    break;
                else
                    allDeckCursor.moveToNext();
            }

        } catch(SQLException e) {

            // TODO indicate fatal error
            e.printStackTrace();
            finishActivity(0);
        }

        if(id != -1) {
            Intent intent = new Intent(this, DeckBuilderActivity.class);
            intent.putExtra(DECK_ID, Long.toString(id));
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // this was required to make the navbar drawer icon responsive
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
