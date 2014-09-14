package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.common.ImageDownloader;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.CardManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.CollectionManager;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.CardDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;

import java.lang.reflect.Array;

public class CardDetail extends Activity {

    protected TextView mQuantityOwnedTextView;

    public static final String BASE_IMAGE_URL_FORMAT =
            "http://wow.zamimg.com/images/hearthstone/cards/enus/original/%s.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(getIntent()))
                    .commit();
        }

        String cardId = getIntent().getStringExtra(Card.CARD_ID);
        Card card = CardManager.getInstance(this).cardById(cardId);

        setTitle(card.getName());
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_detail, menu);
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
    }
    */

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        protected ImageView mHeaderImageView;
        protected Intent mIntent;
        protected ImageDownloader<ImageView> mImageDownloadThread;
        protected String mCardId;
        protected TextView mQuantityOwnedTextView;

        public PlaceholderFragment(Intent intent) {

            mIntent = intent;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // prepare thread for downloading card image
            mImageDownloadThread = new ImageDownloader<ImageView>(new Handler());
            mImageDownloadThread.setListener(new ImageDownloader.Listener<ImageView>() {
                @Override
                public void onImageDownloaded(ImageView imageView, Bitmap bitmap) {

                if(isVisible()) {
                    imageView.setImageBitmap(bitmap);
                    mHeaderImageView.setImageBitmap(bitmap);
                }
                }
            });
            mImageDownloadThread.start();
            mImageDownloadThread.getLooper();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_card_detail, container, false);

            // set image
            ImageView imageView = (ImageView)rootView.findViewById(R.id.card_detail_image);
            mHeaderImageView = (ImageView)rootView.findViewById(R.id.card_detail_header_image);

            String cardId = mIntent.getStringExtra(Card.CARD_ID);
            //Card card = CardManager.getInstance(getActivity()).cardById(cardId);
            Card card = new CardDbAdapter(getActivity()).cardById(cardId);
            String url = String.format(BASE_IMAGE_URL_FORMAT, cardId);
            mImageDownloadThread.queueImage(imageView, url);

            // set flavor
            TextView flavorView = (TextView)rootView.findViewById(R.id.card_detail_flavor);
            flavorView.setText(card.getFlavor());

            // set card set
            TextView setView = (TextView)rootView.findViewById(R.id.card_detail_set_value);
            setView.setText(card.getSet().toString());

            // get and set the quantity owned
            int quantityOwned =
                    CollectionManager.getInstance(getActivity()).quantityForCardId(cardId);
            mQuantityOwnedTextView =
                    (TextView)rootView.findViewById(R.id.card_detail_quantity_value);
            mQuantityOwnedTextView.setText(Integer.toString(quantityOwned));

            // set up the 'Add to Collection' button
            Button addToCollectionButton =
                    (Button)rootView.findViewById(R.id.card_detail_add_to_collection_button);
            addToCollectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCurrentCardToCollection();
                }
            });

            return rootView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            mImageDownloadThread.quit();
        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();

            mImageDownloadThread.clearQueue();
        }

        protected void addCurrentCardToCollection() {
            CollectionManager.getInstance(
                    getActivity()).addQuantityForId(1, mIntent.getStringExtra(Card.CARD_ID));

            mQuantityOwnedTextView.setText(
                    Integer.toString(CollectionManager.getInstance(getActivity()).quantityForCardId(
                            mIntent.getStringExtra(Card.CARD_ID)
                    )));
        }
    }
}
