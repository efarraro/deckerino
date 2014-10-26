package com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;

public class ChooseClassActivity extends BaseActivity {

    public final static String PLAYER_CLASS = "PlayerClass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ChooseClassFragment())
                    .commit();
        }

        setTitle(getString(R.string.activity_choose_class));
    }
}
