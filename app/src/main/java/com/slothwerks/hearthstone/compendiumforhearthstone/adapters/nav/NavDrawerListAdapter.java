package com.slothwerks.hearthstone.compendiumforhearthstone.adapters.nav;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItem;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.nav.NavDrawerItemType;

import java.util.List;

/**
 * Created by Eric on 9/17/2014.
 */
public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {

    public NavDrawerListAdapter(Context context, List<NavDrawerItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.nav_item_link, parent, false);
        }

        NavDrawerItem item = getItem(position);

        // set the title of the item
        TextView navItemLinkTitle = (TextView)convertView.findViewById(R.id.nav_item_link_title);
        navItemLinkTitle.setText(getItem(position).getTitle());

        // bold any of the drawer items that are 'title' type
        if(item.getItemType() == NavDrawerItemType.Title) {

            // TODO maybe convert this into a style to be applied?

            navItemLinkTitle.setTypeface(null, Typeface.BOLD);

            // also, show a horizontal divider
            View divider = convertView.findViewById(R.id.nav_item_divider);
            divider.setVisibility(View.VISIBLE);

            // font is smaller, and in all caps
            navItemLinkTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);
            navItemLinkTitle.setText(item.getTitle().toUpperCase());
        }

        return convertView;
    }
}
