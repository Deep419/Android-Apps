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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton start_button;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference mDatabase;

    private List<Meeting> mMeetings = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test", "App started");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        start_button = findViewById(R.id.add_imageButton);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getMeetings();
//        String id = "21";
//        Meeting m = new Meeting("Title1","Woodward",id,2020,12,10,11,13);
//        Log.d("Test", "M:  " + m.toString());
//
//        mMeetings.add(m);
//        mDatabase.child("meetings").setValue(id);
//        mDatabase.child("meetings").child(id).setValue(m);
//        Log.d("Test", "Inside " + mDatabase.child("meetings"));



        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMeeting.class);
                startActivity(intent);
            }
        });
    }

    public void getMeetings(){

        Log.d("Test", "getMeeting DB: " + mDatabase);
        mDatabase.child("meetings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("Test", "getMeeting RV: " + mRecyclerView);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();

//                    Log.d("Test", "New " + ds.getValue(String.class));
                    Log.d("Test", "onDataChange: "+ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                mAdapter = new MeetingAdapter(mMeetings, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
