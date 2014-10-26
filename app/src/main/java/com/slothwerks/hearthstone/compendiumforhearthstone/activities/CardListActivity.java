package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.os.Bundle;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.HomeCardListFragment;

public class CardListActivity extends BaseActivity {

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
