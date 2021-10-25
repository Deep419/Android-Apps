package com.example.chatroomapp;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ThreadsActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private ImageButton logout,addNewThreadButton;
    private TextView name;
    private EditText addNewThreadText;
    private String TAG = "demo", newThread;
    private List<Threads> mThreads = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);

        mRecyclerView = findViewById(R.id.recycleViewThreads);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        logout = findViewById(R.id.imageButtonLogout);
        addNewThreadButton = findViewById(R.id.imageButtonAddNewThread);
        addNewThreadText = findViewById(R.id.editTextAddNewThread);
        name = findViewById(R.id.textViewFullName);

        name.setText(getName());

        getThreads(getToken());

        Log.d(TAG, "onCreate Parent Thread");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThreadsActivity.this, MainActivity.class);
                removeToken();
                startActivity(intent);
                finish();
            }
        });

        addNewThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newThread = addNewThreadText.getText().toString();
                if(newThread.equals("")) {
                    Toast.makeText(ThreadsActivity.this,"Enter Thread Name!",Toast.LENGTH_LONG).show();
                    return;
                }
                addThread(getToken(),newThread);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getThreads(String token) {

        final Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                ThreadsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ThreadsActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + String.valueOf(Thread.currentThread().getId()));
                String str = response.body().string();
                Log.d(TAG, "onResponse: String "+str);

                Gson gson = new Gson();
                ThreadResponse threadResponse = gson.fromJson(str, ThreadResponse.class);

                Log.d(TAG, "onResponse: Status " + threadResponse.getStatus());
                if(threadResponse.getStatus().equals("error")){
                    ThreadsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ThreadsActivity.this, "Incorrect email and/or password!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else if (threadResponse.getStatus().equals("ok")){
                    mThreads.addAll(threadResponse.getThreads());
                    ThreadsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            mAdapter = new ThreadsAdapter(mThreads, ThreadsActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            //mAdapter = new ThreadsAdapter(ThreadsActivity.this, R.layout.threads_item, mThreads);
                            //mAdapter.setCustomButtonListner(ThreadsActivity.this);
                            //final ArrayAdapter<String> mAdapter = new ArrayAdapter(ThreadsActivity.this, android.R.layout.simple_list_item_1,titleList);
                            //threadsListView.setAdapter(mAdapter);

//                            addNewThreadButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    String addThread = addNewThreadText.getText().toString();
//                                    if(addThread.equals("")) {
//                                        Toast.makeText(ThreadsActivity.this,"Enter Thread Name!",Toast.LENGTH_LONG).show();
//                                        return;
//                                    }
//                                    Log.d(TAG, "onClick: 1");
//                                    addThread(getToken(),addNewThreadText.getText().toString());
//                                    //threadsListView.setAdapter(mAdapter);
//                                    Log.d(TAG, "onClick: getThread size Notify");
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                            });
                        }
                    });
                }
            }
        });
    }

    public void addThread(String token, String title) {

        RequestBody formBody = new FormBody.Builder()
                .add("title",title)
                .build();

        final Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/add")
                .post(formBody)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: addThread");
                ThreadsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ThreadsActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + String.valueOf(Thread.currentThread().getId()));
                String str = response.body().string();
                Log.d(TAG, "onResponse: addThread String "+str);

                Gson gson = new Gson();
                ThreadResponse threadResponse = gson.fromJson(str, ThreadResponse.class);
                final Threads thread = gson.fromJson(str,Threads.class);

                final String status = threadResponse.getStatus();
                Log.d(TAG, "onResponse: addThread Status " + threadResponse.getStatus());
                if(threadResponse.getStatus().equals("error")){
                    ThreadsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ThreadsActivity.this, status, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(threadResponse.getStatus().equals("ok")) {

                    Log.d(TAG, "run: " + mAdapter.getItemCount());
                    Log.d(TAG, "onClick: addThread " + mThreads.size());
                    mThreads.add(thread);

                    Log.d(TAG, "onClick: addThread " + mThreads.size());

                    ThreadsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onClick: addThread size Notify");
//get
                            mAdapter.notifyDataSetChanged();
                            Log.d(TAG, "run: " + mAdapter.getItemCount());
                        }
                    });

                    //Log.d(TAG, "onClick: addThread " + mAdapter.getPosition(thread));
                }
            }
        });
    }

    public void removeThread(String token, String threadID, final int position) {

        final Request request = new Request.Builder()
                .header("Authorization", "BEARER " + token)
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/delete/"+threadID)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: removeThread");
                ThreadsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ThreadsActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d(TAG, "onResponse: addThread String "+str);

                Gson gson = new Gson();
                ThreadResponse threadResponse = gson.fromJson(str, ThreadResponse.class);

                final String status = threadResponse.getStatus();
                Log.d(TAG, "onResponse: addThread Status " + threadResponse.getStatus());
                if(threadResponse.getStatus().equals("error")){
                    ThreadsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ThreadsActivity.this, status, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(status.equals("ok")) {
                    Log.d(TAG, "onClick: removeThread size " + mThreads.size());
                    mThreads.remove(position);
                    Log.d(TAG, "onClick: removeThread size" + mThreads.size());
                    ThreadsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //threadsListView.setAdapter(mAdapter);
                            Log.d(TAG, "onClick: removeThread size Notify");
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }



//    @Override
//    public void onButtonClickListner(String threadID, int position) {
//        Log.d("tag", "onClick: removeThread : " + getToken() + "id - "+ threadID + " position : " + position);
//        removeThread(getToken(), threadID, position);
//    }

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

    public void removeToken(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
