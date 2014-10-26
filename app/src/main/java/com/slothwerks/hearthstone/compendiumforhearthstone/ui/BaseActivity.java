package com.slothwerks.hearthstone.compendiumforhearthstone.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;

import junit.framework.Assert;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/21/2014.
 */
public class BaseActivity extends ActionBarActivity implements IntentConstants {

    @Override
    protected void onStart() {
        super.onStart();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        Assert.assertTrue("Expects R.id.toolbar", toolbar != null);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
