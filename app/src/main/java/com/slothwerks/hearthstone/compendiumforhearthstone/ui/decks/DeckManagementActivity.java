package com.slothwerks.hearthstone.compendiumforhearthstone.ui.decks;

import android.os.Bundle;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseDrawerActivity;

public class DeckManagementActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_management);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DeckManagementFragment())
                    .commit();
        }
    }

}
