package com.example.flickrimagesearchapp;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "demo";
    private final OkHttpClient client = new OkHttpClient();
    private List<Hits> hits = new ArrayList<>();
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = findViewById(R.id.pager);
        mEditText = findViewById(R.id.editText);

        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,"Enter Search!",Toast.LENGTH_LONG).show();
                    return;
                }
                mEditText.setText(mEditText.getText().toString());
                getImages(getString(R.string.apiKey),mEditText.getText().toString());
            }
        });

        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setHint(getString(R.string.search));
            }
        });




    }

    public void getImages(String APIKey, String search) {

        final Request request = new Request.Builder()
                .url("https://pixabay.com/api/?key="+APIKey+"&q="+search)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: addThread");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + String.valueOf(Thread.currentThread().getId()));
                String str = response.body().string();
                Log.d(TAG, "onResponse: addThread String "+str);

                Gson gson = new Gson();
                HitResponse hitResponse = gson.fromJson(str, HitResponse.class);

                hits = hitResponse.getHits();

                Log.d(TAG, "onResponse: " + hits.get(10).getUserImageURL());

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPagerAdapter = new SwipePagerAdapter(MainActivity.this, hits);

                        mPager.setAdapter(mPagerAdapter);
                    }
                });
            }
        });
    }

}
