package com.slothwerks.hearthstone.compendiumforhearthstone.ui.browse;

import android.os.Bundle;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDatabaseReady;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseDrawerActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit.ChooseClassFragment;

import de.greenrobot.event.EventBus;

public class BrowseActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO clean this up; ugly way to figure out if the database is ready yet
        setContentView(R.layout.activity_card_list);

        DbAdapter db = null;
        try {
            db = new CardDbAdapter(this).open();
            if(db == null) {
                EventBus.getDefault().register(this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new LoadingDatabaseFragment())
                        .commit();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }

        if (savedInstanceState == null && db != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BrowseFragment())
                    .commit();
        }

        setTitle(getString(R.string.app_name));
    }

    public void onEventMainThread(EventDatabaseReady e) {
        EventBus.getDefault().unregister(this);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.container, new BrowseFragment()).commit();
    }
}
