package com.example.flickrimagesearchapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SwipePagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    List<Hits> hits;

    public SwipePagerAdapter(Context context, List<Hits> inHits) {
        mContext = context;
        hits = inHits;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return hits.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.images_swipe, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        //imageView.setImageURI(Uri.parse(hits.get(position).getUserImageURL()));
        Log.d("demo","i - " + position + "/ " + getCount() + " url : " + hits.get(position).getUserImageURL());
        Picasso.get().load(hits.get(position).getUserImageURL()).into(imageView);
        container.addView(view,0);

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container!=null) {
            container.removeViewAt(position);
        }
    }
}

