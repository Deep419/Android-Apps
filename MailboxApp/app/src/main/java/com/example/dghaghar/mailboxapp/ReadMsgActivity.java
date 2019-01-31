package com.example.dghaghar.mailboxapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadMsgActivity extends AppCompatActivity {

    private static final String TAG = "readmsg";
    private TextView textViewMessage, textViewFrom;

    private Users current_user,selected_user;
    private Message message;

    private DatabaseReference mDatabase, mMessagesReference,mUsersReference;
    private FirebaseAuth mAuth;

    private int MESSAGE_ID_COUNTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_msg);
        setTitle("Read Message");

        if(getIntent().hasExtra("USER")) {
            current_user = (Users) getIntent().getExtras().getSerializable("USER");
            Log.d(TAG, "GotIntent: CurrentUser : " + current_user.toString());
        }
        if(getIntent().hasExtra("MESSAGE_ID")) {
            MESSAGE_ID_COUNTER = getIntent().getExtras().getInt("MESSAGE_ID");
            Log.d(TAG, "GotIntent: MESSAGE_ID : " + MESSAGE_ID_COUNTER);
        }

        if(getIntent().hasExtra("MESSAGE")) {
            message = (Message) getIntent().getExtras().getSerializable("MESSAGE");
            Log.d(TAG, "GotIntent: MESSAGE : " + message.toString());
        }

        textViewMessage = findViewById(R.id.textViewMessage);
        textViewFrom = findViewById(R.id.textViewFrom);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMessagesReference = mDatabase.child("messages");
        mUsersReference = mDatabase.child("users");
        mAuth = FirebaseAuth.getInstance();

        textViewMessage.setText(message.data);
        textViewFrom.setText(message.from_name);
        mMessagesReference.child(message.id).child("isRead").setValue(true);

        mUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseContacts(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.readmsg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reply:
                //mAuth.signOut();
                Intent intent = new Intent(ReadMsgActivity.this, ComposeMsgActivity.class);
                intent.putExtra("USER",current_user);
                intent.putExtra("MESSAGE_ID",MESSAGE_ID_COUNTER);
                Log.d(TAG, "onOptionsItemSelected: Sending User : " + selected_user.toString());
                intent.putExtra("SEND_TO",selected_user);
                startActivity(intent);
                finish();
                return true;
            case R.id.delete:
                Intent mIntent = new Intent(ReadMsgActivity.this, InboxActivity.class);
                mIntent.putExtra("USER",current_user);
                //mIntent.putExtra("MESSAGE_ID",MESSAGE_ID_COUNTER);
                mIntent.putExtra("MESSAGE",message);
                startActivity(mIntent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReadMsgActivity.this, InboxActivity.class);
        intent.putExtra("USER",current_user);
        intent.putExtra("MESSAGE_ID",MESSAGE_ID_COUNTER);
        startActivity(intent);
        finish();
    }

    public void parseContacts(DataSnapshot datasnapshot) {
        for (DataSnapshot snapshot : datasnapshot.getChildren()) {
            Users users = snapshot.getValue(Users.class);
            //Log.d(TAG, "Messages Size : " + users.toString());
            if (message.from_id.equals(users.userID)) {
                //Log.d(TAG, "Messages Size : " + users.toString());
                selected_user = users;
            }
        }
    }
}
