package com.example.newsapp_listview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Article article;
    private TextView descrip, title, publishedAt;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detail Activity");

        imageView = findViewById(R.id.imageView);
        descrip = findViewById(R.id.tv_description);
        title = findViewById(R.id.tv_title);
        publishedAt = findViewById(R.id.tv_publishedAt);

        if (getIntent().getExtras().get("ARTICLE") != null) {
            article = (Article) getIntent().getExtras().get("ARTICLE");
            Picasso.get().load(article.getUrlToImage()).into(imageView); //with(DetailActivity.this)
            descrip.setText(article.getDescription());
            title.setText(article.getTitle());
            publishedAt.setText(article.getPublishedAt());
        }
    }
}
