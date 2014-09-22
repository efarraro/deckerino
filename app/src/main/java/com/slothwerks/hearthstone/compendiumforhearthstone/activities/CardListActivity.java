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
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.HomeCardListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItem;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItemType;

import java.util.ArrayList;

public class CardListActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeCardListFragment())
                    .commit();
        }

        setTitle(getString(R.string.app_name));
    }
}
