/*
InClass 05
loadImage.java
Name : Deep Ghaghara, Pranathi Kallem
Group 23

*/

package com.example.photogalleryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Deep1 on 2/13/2018.
 */

public class loadImage extends AsyncTask<String, Void, Void>{

    ImageView imageView;
    Bitmap bitmap = null;
    ProgressDialog loadPhoto;

    Context context;

    public loadImage(ImageView iv, Context c) {
        imageView = iv;
        context = c;
    }
    @Override
    protected void onPreExecute() {
        Log.d("test","pre loadImage.java");
        loadPhoto = new ProgressDialog(context);
        loadPhoto.setCancelable(false);
        loadPhoto.setMessage("Loading Photo ...");
        loadPhoto.setProgress(0);
        loadPhoto.show();
    }


    @Override
    protected void onPostExecute(Void aVoid) {

        Log.d("test","post loadImage.java");
        loadPhoto.setMessage("Photo Loaded!");
        loadPhoto.setProgress(100);
        loadPhoto.dismiss();
        loadPhoto.setProgress(0);
        if (bitmap != null && imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        bitmap = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("test", "We got a new Bitmap");
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close open connection
        }
        return null;
    }
}