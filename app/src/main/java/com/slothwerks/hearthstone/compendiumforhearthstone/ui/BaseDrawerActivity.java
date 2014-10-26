package com.slothwerks.hearthstone.compendiumforhearthstone.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventUpdateClassTheme;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.browse.BrowseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.decks.DeckManagementActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit.ChooseClassActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit.DeckBuilderActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.navigation.NavDrawerItem;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.navigation.NavDrawerItemType;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.navigation.NavDrawerListAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import junit.framework.Assert;

import java.sql.SQLException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 10/26/2014.
 */
public class BaseDrawerActivity extends BaseActivity {

    protected ActionBarDrawerToggle mToggle;
    protected ListView mNavListView;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // expects a DrawerLayout to be present
        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        // expects a ListView called left_drawer to be present
        mNavListView = (ListView)findViewById(R.id.left_drawer);

        Assert.assertTrue("Expected View with ID R.id.left_drawer!", mNavListView != null);

        ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
        items.add(new NavDrawerItem(getString(R.string.nav_main), NavDrawerItemType.Title));
        items.add(new NavDrawerItem(getString(R.string.nav_browse_cards), NavDrawerItemType.Nav));
        items.add(new NavDrawerItem(getString(R.string.nav_deck), NavDrawerItemType.Title));
        items.add(new NavDrawerItem(getString(R.string.nav_create), NavDrawerItemType.Nav));
        items.add(new NavDrawerItem(getString(R.string.nav_manage), NavDrawerItemType.Nav));
        NavDrawerListAdapter ad = new NavDrawerListAdapter(this, items);
        mNavListView.setAdapter(ad);

        // listen to clicks for navigation
        mNavListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mNavListView.setItemChecked(position, true);
                drawerLayout.closeDrawer(mNavListView);

                NavDrawerItem item = (NavDrawerItem)mNavListView.getAdapter().getItem(position);

                if(item.getTitle().equals(getString(R.string.nav_browse_cards))) {
                    Intent intent = new Intent(getBaseContext(), BrowseActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
                if(item.getTitle().equals(getString(R.string.nav_create))) {
                    Intent intent = new Intent(getBaseContext(), ChooseClassActivity.class);
                    startActivityForResult(intent, 0);
                }
                else if(item.getTitle().equals(getString(R.string.nav_manage))) {
                    Intent intent = new Intent(getBaseContext(), DeckManagementActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // this was required to get the proper icon to show
        //http://stackoverflow.com/questions/17825629/android-drawerlayout-doesnt-show-the-right-indicator-icon
        mToggle.syncState();
    }

    public void onEventMainThread(EventUpdateClassTheme e) {
        mNavListView.setBackground(new ColorDrawable(Utility.getPrimaryColorForClass(
                e.getPlayerClass(), getResources())));
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
            intent.putExtra(DECK_ID, id);
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

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }
}
