package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.nav.NavDrawerListAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.HomeCardListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItem;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItemType;

import java.util.ArrayList;

public class CardListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeCardListFragment())
                    .commit();
        }

        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer((ListView)findViewById((R.id.left_drawer)));

        final ListView drawer = (ListView)findViewById(R.id.left_drawer);
        ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
        items.add(new NavDrawerItem("Home", NavDrawerItemType.Nav));
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

                Intent intent = new Intent(getBaseContext(), DeckBuilderActivity.class);
                startActivity(intent);
            }
        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}
