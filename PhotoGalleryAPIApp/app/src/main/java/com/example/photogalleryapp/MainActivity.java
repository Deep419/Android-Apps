/*
InClass 05
MainActivity.java
Name : Deep Ghaghara, Pranathi Kallem
Group 23

*/


package com.example.photogalleryapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> keywordList = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private int currentPhoto = 0;

    private TextView search;
    private ImageView imageView;
    private ImageButton ibPrev, ibNext;
    private Button buttonGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        search = findViewById(R.id.search);
        imageView = findViewById(R.id.imageView);
        ibNext = findViewById(R.id.btn_next);
        ibPrev = findViewById(R.id.btn_prev);
        buttonGo = findViewById(R.id.btn_go);


        imageView.setImageBitmap(null);
        ibPrev.setVisibility(View.INVISIBLE);
        ibNext.setVisibility(View.INVISIBLE);

        //Get Keywords API
        if(isConnected()){
            Log.d("test", "Connected to Internet.");
            new GetDataAsync(keywordList).execute("http://dev.theappsdr.com/apis/photos/keywords.php");
        }
        else {
            Log.d("test", "No Connection.");
            Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "Go Button was clicked");

                final String[] keywordArray = new String[keywordList.size()];

                for(int i = 0; i < keywordArray.length; i++) {
                    keywordArray[i] = keywordList.get(i);
                    Log.d("test", "THE keywords ARE " + keywordArray[i]);
                }

                //AlertDialog
                AlertDialog.Builder keywordPicker = new AlertDialog.Builder(MainActivity.this);
                keywordPicker.setTitle("Choose a Keyword")
                        .setSingleChoiceItems(keywordArray,-1, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("test", keywordArray[i] + " was pressed!");

                                search.setText(keywordArray[i]);

                                // Reset URL (for photos) array list
                                urls = new ArrayList<>();
                                currentPhoto = 0;
                                // Dismiss dialogInterface
                                dialogInterface.dismiss();

                                // After keyword is selected, use another AsyncTask to connect to the Get URLs API
                                // Retrieve image urls related to the selected keyword
                                if(isConnected()){
                                    Log.d("test", "Connected to Internet.");
                                    new GetLinksAsync(urls,search.getText() + "").execute("http://dev.theappsdr.com/apis/photos/index.php");
                                }
                                else {
                                    Log.d("test", "No Connection.");
                                    Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                                }

                                //Log.d("We got URLS")
                            }
                        });

                final AlertDialog alert = keywordPicker.create();
                alert.show();
            }
        });

        ibPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If currentPhoto is already 0, go to the last photo instead
                if(currentPhoto == 0)
                    currentPhoto = urls.size()-1;
                else
                    currentPhoto--;

                if(isConnected()){
                    Log.d("test", "Connected to Internet.");
                    new loadImage(imageView,MainActivity.this).execute(urls.get(currentPhoto));
                }
                else {
                    Log.d("test", "No Connection.");
                    Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPhoto == urls.size()-1)
                    currentPhoto = 0;
                else
                    currentPhoto++;

                if(isConnected()){
                    Log.d("test", "Connected to Internet.");
                    new loadImage(imageView,MainActivity.this).execute(urls.get(currentPhoto));
                }
                else {
                    Log.d("test", "No Connection.");
                    Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // AsyncTask to get keywords (keywords api) when the Activity starts

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<String> >{
        public GetDataAsync(ArrayList<String> keywords) {
            this.keywords = keywords;
        }
        private ArrayList<String> keywords;

        ProgressDialog loadDictionary;
        @Override
        protected void onPreExecute() {

            Log.d("test","pre Main.java");
            loadDictionary = new ProgressDialog(MainActivity.this);
            loadDictionary.setCancelable(false);

            loadDictionary.setMessage("Loading Dictionary ...");
            loadDictionary.setProgress(0);
            loadDictionary.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            StringTokenizer st = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                // Read the line from the keywords API
                st = new StringTokenizer(reader.readLine(), ";");
                while(st.hasMoreTokens() ){
                    keywords.add(st.nextToken());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return keywords;
        }


        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if(strings != null){
                Log.d("test","Post Main.java");
                Log.d("test", strings.toString());
                loadDictionary.setMessage("Dictionary Loaded!");
                loadDictionary.setProgress(100);
                loadDictionary.dismiss();
                loadDictionary.setProgress(0);

            }else {
                Log.d("test", "s is null");
            }
        }
    }

    // AsyncTask to get the image URLs based on the user's chosen keyword

    private class GetLinksAsync extends AsyncTask<String, Void, ArrayList<String> >{

        private ArrayList<String> urls;
        private String keyword;

        public GetLinksAsync(ArrayList<String> keywords, String key) {
            this.keyword = key;
            this.urls = keywords;
            Log.d("test", "\nKeyword is " + key);
            Log.d("test", "Array of URLs is " + keywords.toString());
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                String x = strings[0] + "?" + "keyword=" + keyword;

                URL url = new URL(x);
                Log.d("test",x);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line = "";
                while((line = reader.readLine()) != null){
                    urls.add(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return urls;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if(strings != null && !strings.isEmpty()){
                Log.d("test", strings.toString());

                Log.d("test", "The URLS are " + strings.toString());
                if(isConnected()){
                    Log.d("test", "Connected to Internet.");
                    new loadImage(imageView,MainActivity.this).execute(strings.get(0));
                }
                else {
                    Log.d("test", "No Connection.");
                    Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }

                if(strings.size() > 1) {
                    ibPrev.setVisibility(View.VISIBLE);
                    ibNext.setVisibility(View.VISIBLE);
                }else{
                    ibPrev.setVisibility(View.INVISIBLE);
                    ibNext.setVisibility(View.INVISIBLE);
                }
                // Set buttons to visible

            }else {
                Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();

                imageView.setImageBitmap(null);
                ibPrev.setVisibility(View.INVISIBLE);
                ibNext.setVisibility(View.INVISIBLE);

                Log.d("test", "URL list is null");
            }
            // Image Stuff
            // Call loadImage AsyncTask to load and set the first image

        }
    }

    // Other Methods

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}