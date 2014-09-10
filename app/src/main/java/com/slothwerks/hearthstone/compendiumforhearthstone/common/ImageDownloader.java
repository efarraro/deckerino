package com.slothwerks.hearthstone.compendiumforhearthstone.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 9/7/2014.
 */
public class ImageDownloader<Token> extends HandlerThread {

    private static final String TAG = "ImageDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    Handler mHandler;
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    Handler mResponseHandler;

    Listener<Token> mListener;
    public interface Listener<Token> {

        void onImageDownloaded(Token token, Bitmap bitmap);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }

    public ImageDownloader(Handler responseHandler) {
        super(TAG);

        mResponseHandler = responseHandler;
    }

    public void queueImage(Token token, String url) {
        requestMap.put(token, url);

        mHandler
                .obtainMessage(MESSAGE_DOWNLOAD, token)
                .sendToTarget();
    }

    private void handleRequest(final Token token) {
        try {
            final String url = requestMap.get(token);
            if(url == null)
                return;

            byte[] bitmapBytes = getBytesFromUrl(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

            Log.d("test", "bytes received" + bitmap.getByteCount());

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {

                    Log.d("test", "here");

                    if(requestMap.get(token) == null)
                        return;

                    Log.d("tets", "setting bitmap");

                    requestMap.remove(token);
                    mListener.onImageDownloaded(token, bitmap);
                }
            });

        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD) {
                    Token token = (Token)msg.obj;
                    handleRequest(token);
                }
            }
        };
    }

    public static byte[] getBytesFromUrl(String url) throws MalformedURLException, IOException {
        URL u = new URL(url);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Log.d("test", "Retrieving " + url);

        InputStream in = u.openStream();

        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }

        out.close();

        return out.toByteArray();
    }
}
