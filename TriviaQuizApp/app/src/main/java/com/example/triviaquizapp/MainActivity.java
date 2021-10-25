package com.example.triviaquizapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String url = "http://dev.theappsdr.com/apis/trivia_json/trivia_text.php";
    ArrayList<Question> questions;
    private Button btn_start, btn_exit;
    private ImageView imageView;
    private TextView tv_1_trivia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Trivia Time");

        questions = new ArrayList<>();

        btn_start = findViewById(R.id.btn_1_start);
        btn_exit = findViewById(R.id.btn_1_exit);
        imageView = findViewById(R.id.imageView);
        tv_1_trivia = findViewById(R.id.tv_1_trivia);

        btn_start.setEnabled(false);

        if(isConnected()){
            Log.d("test", "Is connected to internet.");
            //new GetDataAsync(questions).execute(url);
            new GetQuestions(questions, MainActivity.this).execute(url);
            Log.d("quest",questions.size()+"");
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.trivia));

            imageView.setVisibility(View.VISIBLE);
            tv_1_trivia.setVisibility(View.VISIBLE);
            btn_start.setEnabled(true);
        }
        else {
            Log.d("test", "Not Connected.");
            Toast.makeText(MainActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
        }

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Start Trivia Button was Clicked");
                Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
                intent.putExtra("TRIVIA", questions);
                startActivity(intent);

            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Exit Button was Clicked");
                finish();
            }
        });
    }

    // isConnected() method to check if there is a live internet connection.
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
