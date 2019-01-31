/*
GetArticlesAsyncTask.java
InClass 06

Deep Ghaghara, Pranathi Kallem
Group 23

 */
package com.example.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetArticlesAsyncTask extends AsyncTask<String, Void, Void> {
    private IData mIData;

    GetArticlesAsyncTask(IData IData) {
        mIData = IData;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Log.d("test", "InBack Async " + Thread.currentThread().getId());
        HttpURLConnection connection = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String result = IOUtils.toString(connection.getInputStream(), "UTF-8");
                Log.d("line",result.length()+"");

                JSONObject root = new JSONObject(result);
                JSONArray articles = root.getJSONArray("articles");
                String title,publishedAt,urlToImage,description;
                for (int i=0;i<articles.length();i++) {
                    JSONObject articleJSON = articles.getJSONObject(i);

                    title = articleJSON.getString("title");
                    publishedAt = articleJSON.getString("publishedAt");
                    urlToImage = articleJSON.getString("urlToImage");
                    description = articleJSON.getString("description");

                    Article article = new Article(title, publishedAt, urlToImage, description);
                    Log.d("in_async",i + " " + article.toString());
                    mIData.handleArticles(article);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.d("test", "Pre Async : " + Thread.currentThread().getId());
        mIData.onPre();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("test", "Post Async : " + Thread.currentThread().getId());
        mIData.onPost();
    }

    public interface IData{
        void handleArticles(Article data);
        void onPre();
        void onPost();
    }
}
