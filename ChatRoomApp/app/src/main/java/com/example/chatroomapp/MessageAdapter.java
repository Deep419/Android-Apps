package com.example.chatroomapp;

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
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends ArrayAdapter<Messages> {

    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(String threadID, int position);
    }

    public void setCustomButtonListner(MessageAdapter.customButtonListener listener) {
        this.customListner = listener;
    }

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Messages> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Messages messages = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.messages_item, parent, false);
        }




        TextView title = convertView.findViewById(R.id.textViewMessage);
        TextView name = convertView.findViewById(R.id.textViewName);
        TextView time = convertView.findViewById(R.id.textViewTime);
        ImageButton imageButton = convertView.findViewById(R.id.imageButtonDeleteMessage);

        imageButton.setVisibility(View.INVISIBLE);
        title.setText(messages.message);
        name.setText(messages.user_fname + " " + messages.user_lname);

        Date date;
        PrettyTime p = new PrettyTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        try {
            date = format.parse(messages.created_at);
            Log.d("date", "getView: " + date);
            Log.d("date", "getView: " + p.format(date));
            time.setText(p.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString)
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences("pref_file", MODE_PRIVATE);
        String currentID = sharedPreferences.getString("userID","");

        if(currentID.equals(messages.getUser_id())) {
            Log.d("tag", "getView: position"+position);
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("tag", "onClick: ");
                    if (customListner != null) {
                        Log.d("tag", "null: ");
                        customListner.onButtonClickListner(messages.getId(), position);
                    }
                }
            });
        }

        return convertView;
    }
}
