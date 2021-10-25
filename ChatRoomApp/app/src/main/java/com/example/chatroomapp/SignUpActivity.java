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

public class SignUpActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private String TAG = "demo";
    private EditText fname, lname, email, pass, retypePass;
    private Button cancel, signup;
    private String sFName,sLName,sEmail,sPass,sRetype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fname = findViewById(R.id.editTextFirstName);
        lname = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editTextEmail2);
        pass = findViewById(R.id.editTextPass1);
        retypePass = findViewById(R.id.editTextPass2);
        cancel = findViewById(R.id.buttonCancel);
        signup = findViewById(R.id.buttonSignUp2);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                sFName = fname.getText().toString();
                sLName = lname.getText().toString();
                sEmail = email.getText().toString();
                sPass = pass.getText().toString();
                sRetype = retypePass.getText().toString();

                Log.d(TAG, "onClick12: " + sFName.length());

                if(sFName.equals("")) {
                    Toast.makeText(SignUpActivity.this,"Enter First Name!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sLName.equals("")) {
                    Toast.makeText(SignUpActivity.this,"Enter Last Name!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sEmail.equals("")) {
                    Toast.makeText(SignUpActivity.this,"Enter Email!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sPass.equals("")) {
                    Toast.makeText(SignUpActivity.this,"Enter Password!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sRetype.equals("")) {
                    Toast.makeText(SignUpActivity.this,"Retype the Password!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!sPass.equals(sRetype)){
                    Toast.makeText(SignUpActivity.this,"Passwords Do not Match!",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d(TAG, "Performing Sign Up");
                performSignUp(sFName, sLName, sEmail, sPass);
            }
        });
    }

    public void performSignUp(String fname, String lname, String email, String pass) {

        RequestBody formBody = new FormBody.Builder()
                .add("email",email)
                .add("password",pass)
                .add("fname",fname)
                .add("lname",lname)
                .build();

        final Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/signup")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "SignUp onFailure: ");
                SignUpActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignUpActivity.this, "Connection to server was not successful!",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d(TAG, "onResponse: " + String.valueOf(Thread.currentThread().getId()));
                String str = response.body().string();
                Log.d(TAG, "SignUp onResponse: String "+str);

                Gson gson = new Gson();
                final TokenResponse tokenResponse = gson.fromJson(str, TokenResponse.class);

                Log.d(TAG, "onResponse: Status " + tokenResponse.getStatus());
                if(tokenResponse.getStatus().equals("error")){
                    Log.d(TAG, "onResponse: Token Error");
                    SignUpActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SignUpActivity.this, tokenResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    SignUpActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SignUpActivity.this, "User Created!", Toast.LENGTH_LONG).show();
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setPref(tokenResponse);
                    Intent intent = new Intent(SignUpActivity.this, ThreadsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void setPref(TokenResponse token){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),
                        MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token.getToken());
        editor.putString("name", token.getUser_fname() + " " + token.getUser_lname());
        editor.putString("userID", token.getUser_id());
        editor.commit();
    }
}
