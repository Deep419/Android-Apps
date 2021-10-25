package com.example.newsappxmlrss;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Article> articles = new ArrayList<>();
    private int currentArticle = 0;

    private TextView search, ctr_current, ctr_total, descrip, title, publishedAt, ctr_text;
    private ImageView imageView;
    private ImageButton ibPrev, ibNext;
    private Button buttonGo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
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
        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(10);
        progressBar.setVisibility(View.INVISIBLE);
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
            Log.d("test", "No Connection.");
            Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "Go Button was clicked");

                final String[] keywordArray = {"Top Stories", "World", "U.S.", "Business", "Politics",
                        "Technology", "Health","Entertainment", "Travel","Living","Most Recent"};

                //AlertDialog
                AlertDialog.Builder keywordPicker = new AlertDialog.Builder(MainActivity.this);
                keywordPicker.setTitle("Choose Category")
                        .setSingleChoiceItems(keywordArray,-1, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("test", keywordArray[i] + " was pressed!");

                                search.setText(keywordArray[i]);

                                articles = new ArrayList<>();
                                currentArticle = 0;
                                dialogInterface.dismiss();

                                if(isConnected()){
                                    Log.d("test", "Connected to Internet.");
                                    String url_build = "http://rss.cnn.com/rss/";
                                    String selected_category = keywordArray[i].toLowerCase();
                                    if(selected_category.equals("top stories")) { url_build = url_build + "cnn_topstories.rss"; }
                                    else if(selected_category.equals("world")) { url_build = url_build + "cnn_world.rss"; }
                                    else if(selected_category.equals("u.s.")) { url_build = url_build + "cnn_us.rss"; }
                                    else if(selected_category.equals("business")) { url_build = url_build + "money_latest.rss"; }
                                    else if(selected_category.equals("politics")) { url_build = url_build + "cnn_allpolitics.rss"; }
                                    else if(selected_category.equals("technology")) { url_build = url_build + "cnn_tech.rss"; }
                                    else if(selected_category.equals("health")) { url_build = url_build + "cnn_health.rss"; }
                                    else if(selected_category.equals("entertainment")) { url_build = url_build + "cnn_showbiz.rss"; }
                                    else if(selected_category.equals("travel")) { url_build = url_build + "cnn_travel.rss"; }
                                    else if(selected_category.equals("living")) { url_build = url_build + "cnn_living.rss"; }
                                    else if(selected_category.equals("most recent")) { url_build = url_build + "cnn_latest.rss"; }

                                    Log.d("url","Url_build : " + url_build);

                                    new GetArticlesAsync(articles).execute(url_build);

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

        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(articles.get(currentArticle).getLink()));
                startActivity(intent);
            }
        });

        title.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(articles.get(currentArticle).getLink()));
                startActivity(intent);
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

    // AsyncTask to get the article URLs based on the user's chosen keyword
    private class GetArticlesAsync extends AsyncTask<String, Integer, ArrayList<Article> > {
        private ArrayList<Article> article_alist = new ArrayList<>();

        public GetArticlesAsync(ArrayList<Article> articles) {
            this.article_alist = articles;
        }
        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Article> arr = new ArrayList<>();

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    arr = ArticleParser.ArticlePullParser.parseArticle(connection.getInputStream());
                    //String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null) {
                    connection.disconnect();
                }
            }
            Log.d("inBG",article_alist.size()+"");
            for(int i= 0;i<arr.size();i++){
                article_alist.add(arr.get(i));
            }
            Log.d("inBG",article_alist.size()+"");
            for (int i = 0;i<=9;i++) {
                Log.d("array",article_alist.get(i).toString());
            }
            return article_alist;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            progressBar.setVisibility(View.GONE);
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
                // Set buttons to visible

            }else {
                Toast.makeText(MainActivity.this, "No Images Found", Toast.LENGTH_SHORT).show();

                imageView.setImageBitmap(null);
                ibPrev.setVisibility(View.INVISIBLE);
                ibNext.setVisibility(View.INVISIBLE);

                Log.d("test", "URL list is null");
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
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

