package com.example.triviaquizapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Question> triviaQuestions;
    int count = 0, correct = 0;
    TextView questionNumber, timeLeft, question;
    ImageView photo;
    Button exit, next;
    ConstraintLayout currentOptions;

    CountDownTimer ctdown = new CountDownTimer(1000*60*2,1000){

        public void onTick(long x ){
            Log.d("test","COUNTDOWN GOING ON ");
            timeLeft.setText("Time Left : " + x/1000 );
        }

        @Override
        public void onFinish() {
            Log.d("test","COUNTDOWN ENDS ");
            generateStats();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trivia);
        setTitle("Trivia");

        if(getIntent() != null && getIntent().getExtras() != null) {
            triviaQuestions = (ArrayList<Question>) getIntent().getExtras().get("TRIVIA");
            Log.d("test",triviaQuestions.size()+"");
            question = findViewById(R.id.txtquestion);
            questionNumber = findViewById(R.id.txtQuestionNumber);
            timeLeft = findViewById(R.id.txtTimeLeft);
            photo = findViewById(R.id.viewImage);
            exit = findViewById(R.id.btn_3_exit);
            next = findViewById(R.id.btn_3_nxt);
            currentOptions = findViewById(R.id.optionsLayout);

            generateScreen();
        }
        else{
            Log.d("test", "No Intent was passed or if passed was null");
        }
    }

    public void generateStats(){
        Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
        intent.putExtra("CORRECT", correct);
        intent.putExtra("TOTAL", triviaQuestions.size());
        ctdown.cancel();
        startActivityForResult(intent,0);
    }

    public void generateScreen(){

        questionNumber.setText("Question " + (triviaQuestions.get(count).getNumber()+1));
        timeLeft.setText("Time Left : " + "XXX");
        getphoto();
        question.setText(triviaQuestions.get(count).getQuestion());

        int size = triviaQuestions.get(count).getOptions().size();
        Log.d("test", "Options size is " + size);
        int current_id = currentOptions.getId();
        for(int i = 0; i < size; i++){

            TextView current = new TextView(this);
            current.setId(View.generateViewId());
            ConstraintLayout.LayoutParams currentParams = new ConstraintLayout.LayoutParams(currentOptions.getMaxWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            currentParams.leftMargin = 16;
            currentParams.topMargin = 32;
            currentParams.rightMargin = 16;
            currentParams.goneEndMargin = 16;
            currentParams.goneStartMargin = 16;
            currentParams.topToBottom = current_id;
            currentParams.rightToRight = currentOptions.getId();
            currentParams.leftToLeft = currentOptions.getId();
            currentParams.horizontalBias = (float) 0.0;
            current.setLayoutParams(currentParams);
            current.setText( ((char)(i+65)) + ") " + triviaQuestions.get(count).getOptions().get(i));
            current.setTextSize(16);
            current.setOnClickListener(this);

            currentOptions.addView(current);
            current_id = current.getId();

        }

        next.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    public void updateScreen(){

        questionNumber.setText("Question " + (triviaQuestions.get(count).getNumber()+1));
        getphoto();
        question.setText(triviaQuestions.get(count).getQuestion());

        currentOptions.removeAllViews();

        int size = triviaQuestions.get(count).getOptions().size();
        Log.d("test", "Options size is " + size);
        int current_id = currentOptions.getId();
        for(int i = 0; i < size; i++){

            TextView current = new TextView(this);
            current.setId(View.generateViewId());
            ConstraintLayout.LayoutParams currentParams = new ConstraintLayout.LayoutParams(currentOptions.getMaxWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            currentParams.leftMargin = 16;
            currentParams.topMargin = 32;
            currentParams.rightMargin = 16;
            currentParams.goneEndMargin = 16;
            currentParams.goneStartMargin = 16;
            currentParams.topToBottom = current_id;
            currentParams.rightToRight = currentOptions.getId();
            currentParams.leftToLeft = currentOptions.getId();
            currentParams.horizontalBias = (float) 0.0;
            current.setLayoutParams(currentParams);
            current.setText( ((char)(i+65)) + ") " + triviaQuestions.get(count).getOptions().get(i));
            current.setTextSize(16);
            current.setOnClickListener(this);

            currentOptions.addView(current);
            current_id = current.getId();

        }

    }

    public void getphoto(){
        if(triviaQuestions.get(count).getUrl().length() > 3) {
            Log.d("url", "the url is :  " + triviaQuestions.get(count).getUrl());
            if(isConnected()){
                Log.d("test", "Is connected to internet. Ready to connect yayayayayy");
                new LoadingImages(photo, this).execute(triviaQuestions.get(count).getUrl());
            }
            else {
                Log.d("test", "Is not connected to internet. You poopoo.");
                Toast.makeText(TriviaActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
            }

            //Picasso.with(this).load(triviaQuestions.get(count).getUrl()).into(photo);
        }else {
            Log.d("url","the url is :  " + triviaQuestions.get(count).getUrl());

            photo.setImageDrawable(null);
        }
    }

    @Override
    public void onClick(View view) {

        if(count == 0){ startCountdownTimer(); }

        if(view.getId() == next.getId()){
            Log.d("test", "Next Button Was Clicked");

            count++;

            if(count < triviaQuestions.size()){ updateScreen(); }
            else{
                Log.d("test", "No more questions");
                Log.d("test", "Time to generate stats");
                generateStats();
            }

        }
        else if(view.getId() == exit.getId()){
            Log.d("test", "Exit Button Was Clicked");
            generateStats();
        }
        else{
            Log.d("test", "Some Option was Clicked");

            if(count < triviaQuestions.size()) {

                int size = triviaQuestions.get(count).getOptions().size();
                int answer = -1;
                for(int i = 0; i < size;i++){
                    if(((TextView) view).getText().toString().contains(triviaQuestions.get(count).getOptions().get(i))){
                        Log.d("test", triviaQuestions.get(count).getOptions().get(i) +" Was Clicked");
                        answer = i;
                        break;
                    }
                }

                if(answer == triviaQuestions.get(count).getAnswer()){
                    correct++;
                    Log.d("test", "You answered Correctly");
                }
                else{
                    Log.d("test", "You answered incorrect choice");
                }
            }

            count++;

            if(count < triviaQuestions.size())
                updateScreen();
            else{
                Log.d("test", "No more questions");
                Log.d("test", "Time to generate stats");
                Log.d("test", "You got Correct " + correct);
                generateStats();
            }
        }
    }

    public void startCountdownTimer(){
        if(count == 0){
            Log.d("test","COUNTDOWN STARTS LOL ");
            ctdown.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("test", "We come here");
        if(requestCode == 0){
            Log.d("test", "We come here TOO ");
            if(resultCode == RESULT_OK){

                Log.d("test", "GOING AGAINN!!");
                Intent goAgain = new Intent(TriviaActivity.this, TriviaActivity.class);
                goAgain.putExtra("TRIVIA", (ArrayList<Question>) getIntent().getExtras().get("TRIVIA"));
                startActivity(goAgain);
                finish();

            }
            else if(resultCode == RESULT_CANCELED){
                Log.d("test", "User wanted to quit");
                finish();
            }
            else{
                Log.d("test", "HGMMM A BUG?? OR OVERLOOK??");
            }
        }
    }

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
