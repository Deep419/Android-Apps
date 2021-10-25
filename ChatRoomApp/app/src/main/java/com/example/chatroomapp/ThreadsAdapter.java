package com.example.chatroomapp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


public class ThreadsAdapter extends RecyclerView.Adapter<ThreadsAdapter.ViewHolder> {
    //customButtonListener customListner;
    private String TAG = "threadAdapter";
    private List<Threads> mThreads;
    private Context mContext;

    public ThreadsAdapter(List<Threads> threads, Context context) {
        mThreads = threads;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.threads_item,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Threads threads = mThreads.get(position);
        //Log.d("demo", "onBindViewHolder: ");
        holder.title.setText(threads.getTitle());
        holder.deleteThread.setVisibility(View.INVISIBLE);


        String currentID = mContext.getSharedPreferences("pref_file",Context.MODE_PRIVATE)
                .getString("userID","");

        Log.d(TAG, "onBindViewHolder: currentID : " + currentID);
        if(currentID.equals(threads.getUser_id())) {
            Log.d(TAG, "onBindViewHolder: ");
            holder.deleteThread.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currentSelectedPosition = position;
                Log.d("demo", "onClick: ");
                notifyDataSetChanged();
                Log.d("demo", "onItemClick: " + position + " mThreads Title : " + mThreads.get(position).getTitle());
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("THREAD",mThreads.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThreads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageButton deleteThread;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.textViewTitle);
            this.deleteThread = itemView.findViewById(R.id.imageButtonDelete);
        }
    }

//    public interface customButtonListener {
//        public void onButtonClickListner(String threadID, int position);
//    }
//
//    public void setCustomButtonListner(customButtonListener listener) {
//        this.customListner = listener;
//    }

//    public ThreadsAdapter(@NonNull Context context, int resource, @NonNull List<Threads> objects) {
//        super(context, resource, objects);
//    }
//
//    @NonNull
//    @Override
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        final Threads threads = getItem(position);
//        //Log.d("tag", "getView: " + threads.getId());
//        if(convertView==null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.threads_item, parent, false);
//        }
//
//        TextView title = convertView.findViewById(R.id.textViewTitle);
//        ImageButton imageButton = convertView.findViewById(R.id.imageButtonDelete);
//
//        imageButton.setVisibility(View.INVISIBLE);
//        title.setText(threads.title);
//
//        //SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString)
//        SharedPreferences sharedPreferences = getContext()
//                .getSharedPreferences("pref_file", MODE_PRIVATE);
//        String currentID = sharedPreferences.getString("userID","");
//
//        if(currentID.equals(threads.getUser_id())) {
//            Log.d("tag", "getView: position"+position);
//            imageButton.setVisibility(View.VISIBLE);
//            imageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d("tag", "onClick: ");
//                    if (customListner != null) {
//                        Log.d("tag", "null: ");
//                        customListner.onButtonClickListner(threads.getId(), position);
//                    }
//                }
//            });
//        }
//
//        return convertView;
//    }
}


