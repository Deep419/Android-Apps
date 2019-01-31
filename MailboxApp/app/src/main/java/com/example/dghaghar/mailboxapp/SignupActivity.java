package com.example.dghaghar.mailboxapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String TAG = "signup";
    private EditText fname, lname, email, pass, retypePass;
    private Button cancel, signup;
    private String sFName,sLName,sEmail,sPass,sRetype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("MessageMe! (Sign Up)");

        fname = findViewById(R.id.editTextFirstName);
        lname = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editTextEmail2);
        pass = findViewById(R.id.editTextPass1);
        retypePass = findViewById(R.id.editTextPass2);
        cancel = findViewById(R.id.buttonCancel);
        signup = findViewById(R.id.buttonSignUp2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
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
                    Toast.makeText(SignupActivity.this,"Enter First Name!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sLName.equals("")) {
                    Toast.makeText(SignupActivity.this,"Enter Last Name!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sEmail.equals("")) {
                    Toast.makeText(SignupActivity.this,"Enter Email!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sPass.equals("")) {
                    Toast.makeText(SignupActivity.this,"Enter Password!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(sRetype.equals("")) {
                    Toast.makeText(SignupActivity.this,"Retype the Password!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!sPass.equals(sRetype)){
                    Toast.makeText(SignupActivity.this,"Passwords Do not Match!",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d(TAG, "Performing Sign Up");
                performSignUp(sFName, sLName, sEmail, sPass);
            }
        });
    }

    private void performSignUp(final String sFName, final String sLName, final String sEmail, final String sPass) {
        mAuth.createUserWithEmailAndPassword(sEmail, sPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: SignUp success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Users new_user = new Users(user.getUid(), sEmail, sPass, sFName+ " " + sLName);
                            mDatabase.child("users").child(user.getUid()).setValue(new_user);
                            Toast.makeText(SignupActivity.this, "User Signup successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignupActivity.this, InboxActivity.class);
                            intent.putExtra("USER", new_user);
                            startActivity(intent);
                            finish();


                        } else {
                            Log.d(TAG, "onComplete: Signup failure " + task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
