package com.slothwerks.hearthstone.compendiumforhearthstone.ui.browse;

import android.os.Bundle;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseDrawerActivity;

public class BrowseActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BrowseFragment())
                    .commit();
        }

        setTitle(getString(R.string.app_name));
    }
}
