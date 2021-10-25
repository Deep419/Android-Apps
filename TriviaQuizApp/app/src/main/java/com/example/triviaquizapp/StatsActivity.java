package com.example.triviaquizapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        if(getIntent() != null && getIntent().getExtras() != null) {
            setContentView(R.layout.activity_stats);
            int correct = (int) getIntent().getExtras().get("CORRECT");
            int total = (int) getIntent().getExtras().get("TOTAL");

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbarCorrect);
            progressBar.setMax(total);
            progressBar.setProgress(correct);

            ((TextView) findViewById(R.id.txtPercent)).setText(((double)correct*100.0/(double)total) + "%");

            if(correct == total){
                ((TextView) findViewById(R.id.txtSuggest)).setText("GOOD JOB!! YOU GOT ALL TRIVIA QUESTIONS CORRECT.");
            }
            else {
                ((TextView) findViewById(R.id.txtSuggest)).setText("Try again and see if you can get all the correct answers!");
            }
        }
        else{
            Log.d("test", "Did not recieve any intent");
        }
    }

    public void onQuit(View view) {
        Log.d("test", "Quit Button was clicked");
        Intent intent = new Intent();
        intent.putExtra("RESULT", false);
        setResult(RESULT_CANCELED,intent);
        finish();
    }

    public void onTryAgain(View view) {
        Log.d("test", "Try Again Button was clicked");
        Intent intent = new Intent();
        intent.putExtra("RESULT", true);
        setResult(RESULT_OK, intent);
        finish();
    }
}
