package com.example.chatroomapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity implements MessageAdapter.customButtonListener  {

    private final OkHttpClient client = new OkHttpClient();
    private ImageButton home,addNewMessageButton;
    private TextView threadTitle;
    private EditText addNewMessageText;
    private ListView messagesListView;
    private Threads current_thread;
    private String TAG = "message", newThread;
    private List<Messages> mMessages = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        home = findViewById(R.id.imageButtonHome);
        addNewMessageButton = findViewById(R.id.imageButtonAddNewMessage);
        addNewMessageText = findViewById(R.id.editTextAddNewMessage);
        threadTitle = findViewById(R.id.textViewThreadTitle);
        messagesListView = findViewById(R.id.listVIewMessages);

        if (getIntent().hasExtra("THREAD")) {
            current_thread = (Threads) getIntent().getExtras().getSerializable("THREAD");
            threadTitle.setText(current_thread.getTitle());
        }

        getMessages(getToken(),current_thread.getId());

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });


    }

    public void getMessages(String token, String thread_id) {

        final Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/messages/"+thread_id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                MessageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MessageActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d(TAG, "onResponse: String "+str);

                Gson gson = new Gson();
                MessagesResponse messagesResponse = gson.fromJson(str, MessagesResponse.class);

                Log.d(TAG, "onResponse: Status " + messagesResponse.getStatus());
                if(messagesResponse.getStatus().equals("error")){
                    MessageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MessageActivity.this, "Incorrect email and/or password!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else if (messagesResponse.getStatus().equals("ok")){
                    mMessages.addAll(messagesResponse.getMessages());
                    MessageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            adapter = new MessageAdapter(MessageActivity.this, R.layout.messages_item, mMessages);
                            adapter.setCustomButtonListner(MessageActivity.this);
                            //final ArrayAdapter<String> adapter = new ArrayAdapter(ThreadsActivity.this, android.R.layout.simple_list_item_1,titleList);
                            messagesListView.setAdapter(adapter);

                            addNewMessageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String addThread = addNewMessageText.getText().toString();
                                    if(addThread.equals("")) {
                                        Toast.makeText(MessageActivity.this,"Enter Thread Name!",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Log.d(TAG, "onClick: 1");
                                    addMessage(getToken(),addNewMessageText.getText().toString());
                                    //threadsListView.setAdapter(adapter);
                                    Log.d(TAG, "onClick: getThread size Notify");
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void addMessage(String token, String message_text) {

        RequestBody formBody = new FormBody.Builder()
                .add("message",message_text)
                .add("thread_id",current_thread.getId())
                .build();

        final Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/message/add")
                .post(formBody)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: addThread");
                MessageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MessageActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + String.valueOf(Thread.currentThread().getId()));
                String str = response.body().string();
                Log.d(TAG, "onResponse: addThread String "+str);

                Gson gson = new Gson();
                MessagesResponse messagesResponse = gson.fromJson(str, MessagesResponse.class);
                Log.d(TAG, "onResponse: Pre " + str);
                //str.replace("{\"status\":\"ok\",\"message\":","");
                str = str.substring(25, str.length() - 1);
                Log.d(TAG, "onResponse: Post " + str);
                final Messages messages = gson.fromJson(str,Messages.class);

                final String status = messagesResponse.getStatus();
                Log.d(TAG, "MESSAGE " + messagesResponse.getStatus());
                if(messagesResponse.getStatus().equals("error")){
                    MessageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MessageActivity.this, status, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(messagesResponse.getStatus().equals("ok")) {

                    MessageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onClick: addMessage size Notify");
                            //threadsListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Log.d(TAG, "onClick: addMessage " + mMessages.size());
                    mMessages.add(messages);

                    Log.d(TAG, "onClick: addMessage " + mMessages.size());
                    Log.d(TAG, "onClick: addMessage " + adapter.getPosition(messages));
                }
            }
        });
    }

    public void removeMessage(String token, String messageID, final int position) {

        final Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/message/delete/"+messageID)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: removeThread");
                MessageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MessageActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d(TAG, "onResponse: addThread String "+str);

                Gson gson = new Gson();
                MessagesResponse messagesResponse = gson.fromJson(str, MessagesResponse.class);

                final String status = messagesResponse.getStatus();
                Log.d(TAG, "onResponse: addThread Status " + messagesResponse.getStatus());
                if(messagesResponse.getStatus().equals("error")){
                    MessageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MessageActivity.this, status, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(status.equals("ok")) {
                    Log.d(TAG, "onClick: removeThread size " + mMessages.size());
                    mMessages.remove(position);
                    Log.d(TAG, "onClick: removeThread size" + mMessages.size());
                    MessageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //threadsListView.setAdapter(adapter);
                            Log.d(TAG, "onClick: removeThread size Notify");
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public String getName(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        return sharedPreferences.getString("name","");
    }

    public String getUserID(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        return sharedPreferences.getString("userID","");
    }

    public String getToken(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }


    @Override
    public void onButtonClickListner(String messageID, int position) {
        Log.d("tag", "onClick: removeThread : " + getToken() + "id - "+ messageID + " position : " + position);
        removeMessage(getToken(), messageID, position);
    }
}
