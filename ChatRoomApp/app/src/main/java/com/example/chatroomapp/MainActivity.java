package com.example.chatroomapp;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private String TAG = "demo";
    private EditText email, pass;
    private Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Check token - ["+ getToken() +"]");

        //If no token in memory
        if(getToken().equals("")) {
            setContentView(R.layout.activity_main);
            setTitle("Chat Room");

            email = findViewById(R.id.editTextEmail);
            pass = findViewById(R.id.editTextPass);
            login = findViewById(R.id.buttonLogin);
            signup = findViewById(R.id.buttonSignUp);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Performing Login");
                    performLogin(email.getText().toString(), pass.getText().toString());
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Performing Sign Up");
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });

        }
        else {
            Log.d(TAG, "Token Present, Going ThreadsActivity");
            Intent intent = new Intent(MainActivity.this, ThreadsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void performLogin(String username, String password) {

        RequestBody formBody = new FormBody.Builder()
                .add("email",username)
                .add("password",password)
                .build();

        final Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + String.valueOf(Thread.currentThread().getId()));
                String str = response.body().string();
                Log.d(TAG, "onResponse: String "+str);

                Gson gson = new Gson();
                TokenResponse tokenResponse = gson.fromJson(str, TokenResponse.class);

                Log.d(TAG, "onResponse: Status " + tokenResponse.getStatus());
                if(tokenResponse.getStatus().equals("error")){
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "Incorrect email and/or password!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else{
                    Log.d(TAG, "Main -> Saving Token");
                    setPref(tokenResponse);
                    Intent intent = new Intent(MainActivity.this, ThreadsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public String getToken(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    public void setPref(TokenResponse tokenResponse){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",tokenResponse.getToken());
        editor.putString("name", tokenResponse.getUser_fname() + " " + tokenResponse.getUser_lname());
        editor.putString("userID", tokenResponse.getUser_id());
        editor.commit();
    }
}


