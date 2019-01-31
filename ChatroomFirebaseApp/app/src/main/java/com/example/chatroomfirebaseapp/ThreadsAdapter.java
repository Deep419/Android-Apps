package com.example.chatroomfirebaseapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ThreadsAdapter extends ArrayAdapter<Threads> {
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(String threadID, int position);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public ThreadsAdapter(@NonNull Context context, int resource, @NonNull List<Threads> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Threads threads = getItem(position);
        //Log.d("tag", "getView: " + threads.getId());
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.threads_item, parent, false);
        }

        TextView title = convertView.findViewById(R.id.textViewTitle);
        ImageView imageView = convertView.findViewById(R.id.imageButtonDelete);

        imageView.setVisibility(View.INVISIBLE);
        title.setText(threads.title);

        //SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString)
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences("pref_file",Context.MODE_PRIVATE);
        String currentID = sharedPreferences.getString("userID","");

        if(currentID.equals(threads.getUser_id())) {
            Log.d("tag", "getView: position"+position);
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("tag", "onClick: ");
                    if (customListner != null) {
                        Log.d("tag", "null: ");
                        customListner.onButtonClickListner(threads.getId(), position);
                    }
                }
            });
        }

        return convertView;
    }
}
