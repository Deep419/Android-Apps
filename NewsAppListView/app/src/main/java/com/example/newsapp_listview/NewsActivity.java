package com.example.newsapp_listview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    String category = null;
    ArrayList<Article> articles = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listView = findViewById(R.id.news_listView);

        if(getIntent().getExtras().get("CATEGORIES") != null) {
            category = (String) getIntent().getExtras().get("CATEGORIES");
            setTitle(category);



            if(isConnected()){
                Log.d("test", "Connected to Internet.");
                new GetArticlesAsync().execute("https://newsapi.org/v2/top-headlines?country=us&category="+category.toLowerCase()+"&apiKey=f9e8d814ae264f95aa1a8cbd5bb5fa52");

                Log.d("test", "Articles Size outside: " + articles.size());
                ArticleAdapter adapter = new ArticleAdapter(this, R.layout.article_item, articles);

                listView.setAdapter(adapter);
                Log.d("test", " Adapter Set. Articles Size : " + articles.size());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("demo","Selected Article : " + i);

                        //This should go to 3rd activity that displays the Article.
                        Intent intent = new Intent(NewsActivity.this,DetailActivity.class);
                        intent.putExtra("ARTICLE",articles.get(i));
                        startActivity(intent);
                    }
                });
            }
            else {
                Log.d("test", "No Connection.");
                Toast.makeText(NewsActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class GetArticlesAsync extends AsyncTask<String, Void, ArrayList<Article> > {

        public GetArticlesAsync() {       }

        ProgressDialog loadPhoto;

        @Override
        protected void onPreExecute() {
            loadPhoto = new ProgressDialog(NewsActivity.this);
            loadPhoto.setCancelable(false);

            loadPhoto.setMessage("Loading News...");
            loadPhoto.setProgress(0);
            loadPhoto.show();
        }

        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    Log.d("line",result.length()+"");

                    JSONObject root = new JSONObject(result);
                    JSONArray articles_array = root.getJSONArray("articles");
                    String title,publishedAt,urlToImage,description;
                    for (int i=0;i<articles_array.length();i++) {
                        JSONObject articleJSON = articles_array.getJSONObject(i);

                        title = articleJSON.getString("title");
                        publishedAt = articleJSON.getString("publishedAt");
                        urlToImage = articleJSON.getString("urlToImage");
                        description = articleJSON.getString("description");

                        Article article = new Article(title, publishedAt, urlToImage, description);
                        Log.d("in_async",i + " " + article.toString());
                        articles.add(article);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {

            }
            return articles;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles_r) {
            Log.d("out","" + articles.size());
            //while (articles.size() == 0) { continue; }
            if(articles != null && !articles.isEmpty()){
                Log.d("test", "This should first " + articles.toString());

                Log.d("test", "The URLS are " + articles.size());
                if(isConnected()){
                    loadPhoto.setMessage("Photo Loaded!");
                    loadPhoto.setProgress(100);
                    loadPhoto.dismiss();
                    loadPhoto.setProgress(0);
                    Log.d("test", "Connected to Internet.");
                }
                else {
                    Log.d("test", "No Connection.");
                    Toast.makeText(NewsActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(NewsActivity.this, "No News Found", Toast.LENGTH_SHORT).show();

                listView.setVisibility(View.INVISIBLE);

                Log.d("test", "URL list is null");
            }
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
