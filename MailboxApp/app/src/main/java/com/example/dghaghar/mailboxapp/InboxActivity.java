package com.example.dghaghar.mailboxapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InboxActivity extends AppCompatActivity {

    private int MESSAGE_ID_COUNTER = 0;

    private DatabaseReference mDatabase;
    private DatabaseReference mMessageReference;
    private FirebaseAuth mAuth;

    private Users current_user;
    private Message message;
    private List<Message> messageArrayList = new ArrayList<>();
    private MessageAdapter messageAdapter;

    private ListView messagesListView;
    private String TAG = "inbox";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMessageReference = mDatabase.child("messages");
        mAuth = FirebaseAuth.getInstance();

        messagesListView = findViewById(R.id.listView);

        if(getIntent().hasExtra("USER")) {
            current_user = (Users) getIntent().getExtras().getSerializable("USER");
            Log.d(TAG, "onCreate: CurrentUser : " + current_user.toString());
        }

        if(getIntent().hasExtra("MESSAGE")) {
            message = (Message) getIntent().getExtras().getSerializable("MESSAGE");
            Log.d(TAG, "GotIntent: MESSAGE : " + message.toString());
            mMessageReference.child(message.id).removeValue();
        }

        mMessageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseMessages(dataSnapshot);
                Collections.sort(messageArrayList, new Comparator<Message>() {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");
                    @Override
                    public int compare(Message rhs, Message lhs) {
                        try {
                            return dateFormat.parse(lhs.time).compareTo(dateFormat.parse(rhs.time));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                messageAdapter = new MessageAdapter(InboxActivity.this, R.layout.message_item, messageArrayList);
                Log.d(TAG, "onDataChange: " + messageArrayList.size());
                messagesListView.setAdapter(messageAdapter);
                Log.d(TAG, "Parsed Threads");
                Log.d(TAG, "Thread_id_ctr = "+ MESSAGE_ID_COUNTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MESSAGE_ID_COUNTER = Integer.parseInt(snapshot.getKey());
                }
                Log.d(TAG, "onDataChange: Current MESSAGE_ID : " + MESSAGE_ID_COUNTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //On clicking a message, ReadMsgActivity is launched
        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(InboxActivity.this, ReadMsgActivity.class);
                intent.putExtra("MESSAGE",messageArrayList.get(i));
                intent.putExtra("USER",current_user);
                intent.putExtra("MESSAGE_ID",MESSAGE_ID_COUNTER);
                Log.d("message", "onItemClick: " + messageArrayList.get(i).toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inbox_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //On clicking logout, Signout, go to login, finish()
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(InboxActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            //On clicking compose, go to ComposeMsgActivity
            case R.id.compose:
                Intent mIntent = new Intent(InboxActivity.this, ComposeMsgActivity.class);
                mIntent.putExtra("USER",current_user);
                mIntent.putExtra("MESSAGE_ID",MESSAGE_ID_COUNTER);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void parseMessages(DataSnapshot datasnapshot) {
        for (DataSnapshot snapshot : datasnapshot.getChildren()) {
            Message message = snapshot.getValue(Message.class);
            //Log.d(TAG, "Messages Size : " + message.toString());
            if (current_user.name.equals(message.to_name)) {
                messageArrayList.add(message);
            }
        }
        Log.d(TAG, "Messages Size : " + messageArrayList.size());
    }
}
