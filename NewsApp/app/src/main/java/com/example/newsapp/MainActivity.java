/*
MainActivity.java
InClass 06

Deep Ghaghara, Pranathi Kallem
Group 23

 */
package com.example.newsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetArticlesAsyncTask.IData {

    private ArrayList<Article> articles;
    private int currentArticle = 0;

    private TextView search, ctr_current, ctr_total, descrip, title, publishedAt, ctr_text;
    private ImageView imageView;
    private ImageButton ibPrev, ibNext;
    private Button buttonGo;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search_cat);
        imageView = findViewById(R.id.imageView);
        ibNext = findViewById(R.id.btn_next);
        ibPrev = findViewById(R.id.btn_prev);
        buttonGo = findViewById(R.id.btn_go);
        ctr_current = findViewById(R.id.tv_con_1);
        ctr_text = findViewById(R.id.tv_con_2);
        ctr_total = findViewById(R.id.tv_con_3);
        descrip = findViewById(R.id.tv_description);
        title = findViewById(R.id.tv_title);
        publishedAt = findViewById(R.id.tv_publishedAt);


        imageView.setImageBitmap(null);
        ibPrev.setVisibility(View.INVISIBLE);
        ibNext.setVisibility(View.INVISIBLE);
        ctr_current.setVisibility(View.INVISIBLE);
        ctr_text.setVisibility(View.INVISIBLE);
        ctr_total.setVisibility(View.INVISIBLE);

        if(isConnected()){
            Log.d("test1", "Connected to Internet.");
        }
        else {
            Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "Go Button was clicked");
                articles = new ArrayList<>();
                Log.d("test", "1. OnClickGo : " + Thread.currentThread().getId());
                final String[] keywordArray = {"Business","Entertainment", "General", "Health", "Science", "Sports", "Technology"};

                //AlertDialog
                AlertDialog.Builder keywordPicker = new AlertDialog.Builder(MainActivity.this);
                keywordPicker.setTitle("Choose a Keyword")
                        .setSingleChoiceItems(keywordArray,-1, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("test", keywordArray[i] + " was pressed!");

                                search.setText(keywordArray[i]);

                                currentArticle = 0;
                                dialogInterface.dismiss();

                                if(isConnected()){
                                    Log.d("test", "Connected to Internet.");
                                    new GetArticlesAsyncTask(MainActivity.this).execute("https://newsapi.org/v2/top-headlines?country=us&category="+keywordArray[i].toLowerCase()+"&apiKey=f9e8d814ae264f95aa1a8cbd5bb5fa52");

                                    Log.d("test", "2. After async : " + Thread.currentThread().getId());
                                }
                                else {
                                    Log.d("test", "No Connection.");
                                    Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                final AlertDialog alert = keywordPicker.create();
                alert.show();
                Log.d("outside"," " + articles.size());
            }
        });

        ibPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If currentArticle is already 0, go to the last photo instead
                Log.d("outside"," " + articles.size());
                if(currentArticle == 0)
                    currentArticle = articles.size()-1;
                else
                    currentArticle--;

                if(isConnected()){
                    Log.d("test", "Connected to Internet.");
                    //new loadArticle(articles.get(currentArticle), imageView,MainActivity.this).execute(articles.get(currentArticle).getUrlToImage());
                    setArticle(articles.get(currentArticle));
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
                if(currentArticle == articles.size()-1)
                    currentArticle = 0;
                else
                    currentArticle++;

                if(isConnected()){
                    Log.d("test", "Connected to Internet.");
                    //new loadArticle(articles.get(currentArticle), imageView,MainActivity.this).execute(articles.get(currentArticle).getUrlToImage());
                    setArticle(articles.get(currentArticle));
                }
                else {
                    Log.d("test", "No Connection.");
                    Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setArticle(Article current_article) {
        Picasso.get().load(current_article.getUrlToImage()).into(imageView);
        descrip.setText(current_article.getDescription());
        title.setText(current_article.getTitle());
        publishedAt.setText(current_article.getPublishedAt());
        ctr_current.setText(String.valueOf(currentArticle+1));
        ctr_total.setText(String.valueOf(articles.size()));
        Log.d("Final",String.valueOf(currentArticle) + " : " + String.valueOf(articles.size()));
    }

    @Override
    public void handleArticles(Article data) {

        Log.d("test", "4. Handle Data Main : " + Thread.currentThread().getId());
        if (data!=null) {
            articles.add(data);
        }
    }

    @Override
    public void onPre() {
        dialog = new ProgressDialog(MainActivity.this);
        Log.d("test", "3. Pre Main : " + Thread.currentThread().getId());
        dialog.setMessage("Loading News...");
        dialog.show();

    }

    @Override
    public void onPost() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Log.d("test", "5. Post Main : " + Thread.currentThread().getId());
        if(articles != null && !articles.isEmpty()){
            Log.d("test", articles.toString());

            Log.d("test", "The URLS are " + articles.toString());
            if(isConnected()){
                Log.d("test", "Connected to Internet.");
                //new loadArticle(articles.get(0),imageView,MainActivity.this).execute(articles.get(0).getUrlToImage());
                setArticle(articles.get(0));
            }
            else {
                Log.d("test", "No Connection.");
                Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
            }

            if(articles.size() > 1) {
                ibPrev.setVisibility(View.VISIBLE);
                ibNext.setVisibility(View.VISIBLE);
                ctr_current.setVisibility(View.VISIBLE);
                ctr_text.setVisibility(View.VISIBLE);
                ctr_total.setVisibility(View.VISIBLE);
            }else{
                ibPrev.setVisibility(View.INVISIBLE);
                ibNext.setVisibility(View.INVISIBLE);
            }
        }else {
            Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();

            imageView.setImageBitmap(null);
            ibPrev.setVisibility(View.INVISIBLE);
            ibNext.setVisibility(View.INVISIBLE);

            Log.d("test", "URL list is null");
        }
    }

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
