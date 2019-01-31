package com.example.newsapp_listview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoriesActivity extends AppCompatActivity {

    private ListView listView;
    String[] categories = {"Business","Entertainment","General","Health","Science","Sports","Technology"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setTitle("Categories");

        listView = findViewById(R.id.categories_listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("demo","Clicked Item" + i + ", category " + categories[i]);
                Intent intent = new Intent(CategoriesActivity.this, NewsActivity.class);
                intent.putExtra("CATEGORIES",categories[i]);
                startActivity(intent);
            }
        });
    }
}
