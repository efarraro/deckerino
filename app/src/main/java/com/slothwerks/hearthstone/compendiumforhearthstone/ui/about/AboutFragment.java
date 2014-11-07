package com.slothwerks.hearthstone.compendiumforhearthstone.ui.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Eric on 11/6/2014.
 */
public class AboutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        ((BaseActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        // read and display the license info
        InputStream is = getResources().openRawResource(R.raw.licenses);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            final TextView licenseTextView = (TextView)rootView.findViewById(R.id.license_info);
            String line = reader.readLine();
            StringBuffer b = new StringBuffer();
            while(line != null) {
                b.append(line + "\n");
                line = reader.readLine();
            }
            licenseTextView.setText(b.toString());
        } catch(IOException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
