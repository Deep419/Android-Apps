package com.example.meetingscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference mDatabase;

    private List<Meeting> mMeetings = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "App started");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageButton start_button = findViewById(R.id.add_imageButton);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mMeetings.add(new Meeting("Title1","Woodward","1",2020,12,10,11,13));
//        mMeetings.add(new Meeting("Title1","Woodward","2",2020,12,10,11,13));
//
//        mDatabase.child("meetings").setValue(mMeetings);


        Log.d(TAG, "onCreate: ");
        Log.d(TAG, "mMeetings Pre" + mMeetings.size());
        getMeetings(new FirebaseCallback() {
            @Override
            public void onCallback(List<Meeting> list) {
                Log.d(TAG, "onCallback: "+list.toString());
                mMeetings = list;
                Log.d(TAG, "mMeetings Post" + mMeetings.size());

                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mAdapter = new MeetingAdapter(mMeetings, MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                });


            }
        });

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMeeting.class);
                startActivity(intent);
            }
        });
    }

    public interface FirebaseCallback {
        void onCallback(List<Meeting> list);
    }

    public void getMeetings(final FirebaseCallback firebaseCallback){

        Log.d(TAG, "getMeeting DB: " + mDatabase);
        mDatabase.child("meetings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "getMeeting RV: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mMeetings.add(ds.getValue(Meeting.class));
                }
                firebaseCallback.onCallback(mMeetings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
