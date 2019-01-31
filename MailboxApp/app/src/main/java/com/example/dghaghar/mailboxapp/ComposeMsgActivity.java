package com.example.dghaghar.mailboxapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ComposeMsgActivity extends AppCompatActivity {

    private static final String TAG = "compose";

    private String messageText, sentTo;
    private TextView textViewTo;
    private ImageView imageViewContacts;
    private Button sendButton;
    private EditText editTextMessage;

    private int MESSAGE_ID_COUNTER;

    private DatabaseReference mDatabase, mMessagesReference, mUsersReference;
    private FirebaseAuth mAuth;

    private Users current_user,selected_user;
    private List<String> contacts = new ArrayList<>();
    private List<Users> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_msg);
        setTitle("Compose Message");

        sendButton = findViewById(R.id.buttonSend);
        editTextMessage = findViewById(R.id.editTextMessage);
        textViewTo = findViewById(R.id.textViewTo);
        imageViewContacts = findViewById(R.id.imageViewContacts);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMessagesReference = mDatabase.child("messages");
        mUsersReference = mDatabase.child("users");
        mAuth = FirebaseAuth.getInstance();

        if(getIntent().hasExtra("USER")) {
            current_user = (Users) getIntent().getExtras().getSerializable("USER");
            Log.d(TAG, "GotIntent: CurrentUser : " + current_user.toString());
        }
        if(getIntent().hasExtra("MESSAGE_ID")) {
            MESSAGE_ID_COUNTER = getIntent().getExtras().getInt("MESSAGE_ID");
            Log.d(TAG, "GotIntent: MESSAGE_ID : " + MESSAGE_ID_COUNTER);
        }
        if(getIntent().hasExtra("SEND_TO")) {
            selected_user = (Users) getIntent().getExtras().getSerializable("SEND_TO");
            textViewTo.setText(selected_user.name);
        }

        mUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parseContacts(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageViewContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder contactsDialog = new AlertDialog.Builder(ComposeMsgActivity.this);
                contactsDialog.setTitle("Contacts")
                        .setSingleChoiceItems(contacts.toArray(new String[0]), -1,
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textViewTo.setText(contacts.get(which));
                                dialog.dismiss();
                                selected_user = usersList.get(which);
                            }
                        });
                AlertDialog alert = contactsDialog.create();
                alert.show();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageText = editTextMessage.getText().toString();
                sentTo = textViewTo.getText().toString();

                if (messageText.equals("")) {
                    Toast.makeText(ComposeMsgActivity.this, "Enter Message!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (sentTo.equals("")) {
                    Toast.makeText(ComposeMsgActivity.this, "Select Receiever", Toast.LENGTH_LONG).show();
                    return;
                }

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");
                String date = df.format(Calendar.getInstance().getTime());
                Log.d(TAG, "Time : " + date);
                //data, from_id, from_name, id, time, to_id, to_name, isRead
                MESSAGE_ID_COUNTER = MESSAGE_ID_COUNTER+1;
                Message message = new Message(messageText,current_user.userID,current_user.name,
                        String.valueOf(MESSAGE_ID_COUNTER),date,selected_user.userID,
                        selected_user.name,false);
                mMessagesReference.child(String.valueOf(MESSAGE_ID_COUNTER)).setValue(message);
                Toast.makeText(ComposeMsgActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ComposeMsgActivity.this, InboxActivity.class);
                intent.putExtra("USER",current_user);
                startActivity(intent);
                finish();
                }
        });
    }

    public void parseContacts(DataSnapshot datasnapshot) {
        for (DataSnapshot snapshot : datasnapshot.getChildren()) {
            Users users = snapshot.getValue(Users.class);
            Log.d(TAG, "Messages Size : " + users.toString());
            if (!current_user.name.equals(users.name)) {
                contacts.add(users.name);
                usersList.add(users);
            }
            Log.d(TAG, "Contacts size: " +contacts.size());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ComposeMsgActivity.this, InboxActivity.class);
        intent.putExtra("USER",current_user);
        intent.putExtra("MESSAGE_ID",MESSAGE_ID_COUNTER);
        startActivity(intent);
        finish();
    }
}
