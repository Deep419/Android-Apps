package com.example.chatroomfirebaseapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThreadlistActivity extends AppCompatActivity implements ThreadsAdapter.customButtonListener {

    private int THREAD_ID_COUNTER = 0;

    private DatabaseReference mDatabase;
    private DatabaseReference mThreadReference;
    private FirebaseAuth mAuth;

    private Users current_user;
    private List<Threads> mThreadsList = new ArrayList<>();
    private ThreadsAdapter mThreadsAdapter;

    private ImageButton logout,addNewThreadButton;
    private TextView name;
    private EditText addNewThreadText;
    private ListView threadsListView;
    private String TAG = "threadlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threadlist);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mThreadReference = mDatabase.child("threads");
        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.imageButtonLogout);
        addNewThreadButton = findViewById(R.id.imageButtonAddNewThread);
        addNewThreadText = findViewById(R.id.editTextAddNewThread);
        name = findViewById(R.id.textViewFullName);
        threadsListView = findViewById(R.id.listViewThreads);

        name.setText(getName());

        if(getIntent().hasExtra("USER")) {
            current_user = (Users) getIntent().getExtras().getSerializable("USER");
            name.setText(current_user.getFname() + " " + current_user.getLname());
        }

        mThreadReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseThread(dataSnapshot);
                mThreadsAdapter = new ThreadsAdapter(ThreadlistActivity.this, R.layout.threads_item, mThreadsList);
                mThreadsAdapter.setCustomButtonListner(ThreadlistActivity.this);
                threadsListView.setAdapter(mThreadsAdapter);
                Log.d(TAG, "Parsed Threads");
                Log.d(TAG, "Thread_id_ctr = "+ THREAD_ID_COUNTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mThreadReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    THREAD_ID_COUNTER = Integer.parseInt(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentUserID();
                mAuth.signOut();
                Intent intent = new Intent(ThreadlistActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addNewThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addThreadText = addNewThreadText.getText().toString();
                if(addThreadText.equals("")) {
                    Toast.makeText(ThreadlistActivity.this,"Enter Thread Name!",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("ctr", "2 ");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());

                Log.d(TAG, "onClick: THREADCOUNTER " + THREAD_ID_COUNTER + " " + mThreadsList.size());
                Threads threads = new Threads(date,String.valueOf(THREAD_ID_COUNTER+1),
                        addThreadText, current_user.fname, current_user.userID, current_user.lname);
                mThreadReference.child(String.valueOf(THREAD_ID_COUNTER+1)).setValue(threads);
                mThreadsList.add(threads);

                mThreadsAdapter.notifyDataSetChanged();
                Log.d(TAG, "Thread Size : " + mThreadsList.size());
            }
        });

        threadsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("demo", "onItemClick: ");
            }
        });

        threadsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("message", "onItemClick: " + i + " mThreads Title : " + mThreadsList.get(i).getTitle());
                Intent intent = new Intent(ThreadlistActivity.this, MessagelistActivity.class);
                intent.putExtra("THREAD",mThreadsList.get(i));
                intent.putExtra("USER",current_user);
                Log.d("message", "onItemClick: " + mThreadsList.get(i).toString());
                startActivity(intent);
            }
        });
    }

    public void parseThread(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            int size = (int)snapshot.getChildrenCount();
            if (size == 0) { return; }
            String  [] params = new String[size];
            //Log.d(TAG, "onDataChange: " + size);
            int i = 0;
            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                //Log.d(TAG, "onDataChange: "+ snapshot1.toString());
                params[i] = snapshot1.getValue(String.class);
                i++;
            }
            Threads threads = new Threads(params[0],params[1],params[2],params[3],params[4],params[5]);
            //Log.d(TAG, threads.toString());
            mThreadsList.add(threads);
            Log.d(TAG, "Thread Size : " + mThreadsList.size());
        }
    }

    public void removeThread(String uID, final String threadID, int position) {
        mThreadsList.remove(position);
        Log.d("Del", "removeThread: "+threadID);
        mThreadReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(threadID)) {
                        Log.d("Del", "Match Found: " + snapshot.toString());
                        snapshot.getRef().removeValue();
                    }
//                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                        //Log.d(TAG, "onDataChange: "+ snapshot1.toString());
//                        params[i] = snapshot1.getValue(String.class);
//                        i++;
//                    }
                    //Threads threads = new Threads(params[0],params[1],params[2],params[3],params[4],params[5]);
                    //Log.d(TAG, threads.toString());
                    //mThreadsList.add(threads);
                    Log.d("Del", "Thread Size : " + mThreadsList.size());
                }
                mThreadsAdapter = new ThreadsAdapter(ThreadlistActivity.this, R.layout.threads_item, mThreadsList);
                mThreadsAdapter.setCustomButtonListner(ThreadlistActivity.this);
                threadsListView.setAdapter(mThreadsAdapter);
                Log.d(TAG, "Parsed Threads");
                Log.d(TAG, "Thread_id_ctr = "+ THREAD_ID_COUNTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public String getCurrentUserID(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        return sharedPreferences.getString("userID","");
    }

    public void removeCurrentUserID(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public String getName(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        return sharedPreferences.getString("name","");
    }

    @Override
    public void onButtonClickListner(String threadID, int position) {
        Log.d("tag", "onClick: removeThread : " + getCurrentUserID() + "id - "+ threadID + " position : " + position);
        removeThread(getCurrentUserID(), threadID, position);
        }
}
