package com.example.meetingscheduler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private String TAG = "meetingAdapter";
    private List<Meeting> mMeetings;
    private Context mContext;

    public MeetingAdapter(List<Meeting> meetings, Context context) {
        mMeetings = meetings;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_item,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Meeting meeting = mMeetings.get(position);
        holder.title.setText(meeting.getTitle());
        holder.place.setText(meeting.getPlace());
        String dateTime = "On" + meeting.getMonth() + "/" + meeting.getDay() + "/" + meeting.getYear() + " at " + meeting.getHour() + ":" + meeting.getMin();
        holder.dateTime.setText(dateTime);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("demo", "onClick: ");
                notifyDataSetChanged();
                Log.d("demo", "onItemClick: " + position + " mThreads Title : " + mMeetings.get(position).getTitle());
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMeetings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView place;
        protected TextView dateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.textView_title);
            this.place = itemView.findViewById(R.id.textView_place);
            this.dateTime = itemView.findViewById(R.id.textView_datetime);
        }
    }
}
