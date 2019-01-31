package com.example.chatroomfirebaseapp;

import android.content.Intent;
import android.os.Message;
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

public class MessagelistActivity extends AppCompatActivity implements MessagesAdapter.customButtonListener {

    private DatabaseReference mDatabase;
    private DatabaseReference mMessagesReference;
    private FirebaseAuth mAuth;

    private int MESSAGE_ID_COUNTER = 0;


    private ImageButton home,addNewMessageButton;
    private TextView threadTitle;
    private EditText addNewMessageText;
    private ListView messagesListView;

    private Threads current_thread;
    private Users current_user;

    private String TAG = "message", newThread;
    private List<Messages> mMessagesList = new ArrayList<>();
    private MessagesAdapter mMessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagelist);

        home = findViewById(R.id.imageButtonHome);
        addNewMessageButton = findViewById(R.id.imageButtonAddNewMessage);
        addNewMessageText = findViewById(R.id.editTextAddNewMessage);
        threadTitle = findViewById(R.id.textViewThreadTitle);
        messagesListView = findViewById(R.id.listVIewMessages);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMessagesReference = mDatabase.child("messages");
        mAuth = FirebaseAuth.getInstance();

        if (getIntent().hasExtra("THREAD")) {
            current_thread = (Threads) getIntent().getExtras().getSerializable("THREAD");
            Log.d(TAG, "onCreate: in messages : " + current_thread.toString());
            threadTitle.setText(current_thread.getTitle());
        }

        if (getIntent().hasExtra("USER")) {
            current_user = (Users) getIntent().getExtras().getSerializable("USER");
        }

        mMessagesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseMessages(dataSnapshot);
                mMessagesAdapter = new MessagesAdapter(MessagelistActivity.this, R.layout.messages_item, mMessagesList);
                mMessagesAdapter.setCustomButtonListner(MessagelistActivity.this);
                messagesListView.setAdapter(mMessagesAdapter);
                Log.d(TAG, "Parsed Messages");
                Log.d(TAG, "Message_id_ctr = "+ MESSAGE_ID_COUNTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMessagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MESSAGE_ID_COUNTER = Integer.parseInt(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessagelistActivity.this, ThreadlistActivity.class);
                intent.putExtra("USER",current_user);
                startActivity(intent);
                finish();
            }
        });

        addNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String created_at, id, message,thread_id, user_fname, user_id, user_lname;
                String addMessageText = addNewMessageText.getText().toString();
                if(addMessageText.equals("")) {
                    Toast.makeText(MessagelistActivity.this,"Enter Message!",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("ctr", "2 ");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());

                Log.d(TAG, "onClick: THREADCOUNTER " + MESSAGE_ID_COUNTER + " " + mMessagesList.size());
                Messages messages = new Messages(date,String.valueOf(MESSAGE_ID_COUNTER+1),
                        addMessageText,current_thread.id , current_user.fname, current_user.userID, current_user.lname);
                mMessagesReference.child(String.valueOf(MESSAGE_ID_COUNTER+1)).setValue(messages);
                mMessagesList.add(messages);

                mMessagesAdapter.notifyDataSetChanged();
                Log.d(TAG, "Thread Size : " + mMessagesList.size());
            }
        });
    }

    public void parseMessages(DataSnapshot dataSnapshot) {
        //String created_at, id, message,thread_id, user_fname, user_id, user_lname;
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
            Messages messages = new Messages(params[0],params[1],params[2],params[3],params[4],params[5], params[6]);
            if (params[3].equals(current_thread.id)) {
                mMessagesList.add(messages);
            }
            Log.d(TAG, "Thread Size : " + mMessagesList.size());
        }
    }

    public void removeMessage(final String messageID, int position) {
        mMessagesList.remove(position);
        Log.d("Del", "removeThread: "+messageID);
        mMessagesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(messageID)) {
                        Log.d("Del", "Match Found: " + snapshot.toString());
                        snapshot.getRef().removeValue();
                    }
                    Log.d("Del", "Thread Size : " + mMessagesList.size());
                }
                mMessagesAdapter.notifyDataSetChanged();
//                mMessagesAdapter = new MessagesAdapter(MessagelistActivity.this, R.layout.messages_item, mMessagesList);
//                mMessagesAdapter.setCustomButtonListner(MessagelistActivity.this);
//                messagesListView.setAdapter(mMessagesAdapter);
                Log.d(TAG, "Parsed Threads");
                Log.d(TAG, "Thread_id_ctr = "+ MESSAGE_ID_COUNTER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onButtonClickListner(String messageID, int position) {
        //Log.d("tag", "onClick: removeThread : " + getToken() + "id - "+ messageID + " position : " + position);
        removeMessage(messageID, position);
    }
}
