package com.slothwerks.hearthstone.compendiumforhearthstone.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
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
                else if(item.getTitle().equals(getString(R.string.nav_manage))) {
                    Intent intent = new Intent(getBaseContext(), DeckManagementActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.deck_builder_menu_name_deck, R.string.deck_builder_menu_name_deck);

        drawerLayout.setDrawerListener(mToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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

        // create a DeckBuilderActivity and indicate that this is a new deck
        PlayerClass selectedClass = PlayerClass.valueOf(playerClassStr);
        Intent intent = new Intent(this, DeckBuilderActivity.class);
        intent.putExtra(CREATE_DECK, true);
        intent.putExtra(PLAYER_CLASS, selectedClass.toString());
        startActivity(intent);
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

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }
}
