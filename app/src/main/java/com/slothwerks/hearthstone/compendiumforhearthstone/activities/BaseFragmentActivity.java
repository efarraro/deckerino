package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.nav.NavDrawerListAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItem;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItemType;

import java.util.ArrayList;

/**
 * Created by Eric on 9/21/2014.
 */
public class BaseFragmentActivity extends FragmentActivity {

    protected ActionBarDrawerToggle mToggle;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // expects a DrawerLayout to be present
        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        // TODO localize these
        // expects a ListView called left_drawer to be present
        final ListView drawer = (ListView)findViewById(R.id.left_drawer);
        ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
        items.add(new NavDrawerItem("Main", NavDrawerItemType.Title));
        items.add(new NavDrawerItem("Browse Cards", NavDrawerItemType.Nav));
        items.add(new NavDrawerItem("Deck", NavDrawerItemType.Title));
        items.add(new NavDrawerItem("Create", NavDrawerItemType.Nav));
        items.add(new NavDrawerItem("View", NavDrawerItemType.Nav));
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

                if(!item.getTitle().equals("Create")) {
                    Intent intent = new Intent(getBaseContext(), ChooseClassActivity.class);
                    startActivityForResult(intent, 0);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), DeckBuilderActivity.class);
                    startActivity(intent);
                }
            }
        });

        // TODO purpose of open/close strings?
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
