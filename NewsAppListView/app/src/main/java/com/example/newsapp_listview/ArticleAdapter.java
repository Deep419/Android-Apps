package com.example.newsapp_listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Deep1 on 2/26/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(@NonNull Context context, int resource, @NonNull List<Article> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Article article = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);
        }

        TextView title = convertView.findViewById(R.id.title_textview);
        TextView date = convertView.findViewById(R.id.pubAt_textview);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        imageView.setImageBitmap(null);

        title.setText(article.title);
        date.setText(article.getPublishedAt());
        Picasso.get().load(article.getUrlToImage()).into(imageView); //with(convertView.getContext())


        return convertView;
    }
}

