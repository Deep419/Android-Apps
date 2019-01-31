package com.example.dghaghar.mailboxapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Message> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Message message = getItem(position);
        Log.d("inbox", "getView: position : " + position);
        Log.d("inbox", "getView: " + message.toString());
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
        }

        TextView sender = convertView.findViewById(R.id.textViewSender);
        TextView dateTime = convertView.findViewById(R.id.textViewDateTime);
        TextView messageData = convertView.findViewById(R.id.textViewMessageSummary);
        ImageView imageViewBlue = convertView.findViewById(R.id.imageViewBlue);
        ImageView imageViewGrey = convertView.findViewById(R.id.imageViewGrey);

        imageViewBlue.setVisibility(View.VISIBLE);
        imageViewGrey.setVisibility(View.INVISIBLE);

        sender.setText(message.from_name);
        dateTime.setText(message.time);
        if (message.data.length() < 43) {
            messageData.setText(message.data);
        } else {
            messageData.setText(message.data.substring(0, 43) + "...");
        }
        if(message.isRead) {
            Log.d("tag", "getView: position"+position);
            imageViewBlue.setVisibility(View.INVISIBLE);
            imageViewGrey.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
