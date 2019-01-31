package com.example.triviaquizapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by dghag on 2/19/2018.
 */

public class GetQuestions extends AsyncTask<String, Void, ArrayList<Question>> {

    private ArrayList<Question> ques;
    private Context context;


    public GetQuestions(ArrayList<Question> url,Context c) {
        this.ques = url;
        context = c;
    }


    ProgressDialog loadDictionary;
    @Override
    protected void onPreExecute() {
        loadDictionary = new ProgressDialog((context));
        loadDictionary.setCancelable(false);
        loadDictionary.setMessage("Loading Dictionary ...");
        loadDictionary.setProgress(0);
        loadDictionary.show();
    }

    @Override
    protected ArrayList<Question> doInBackground(String... strings) {
        StringTokenizer st = null;

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line = "";
            while((line = reader.readLine()) != null){

                Log.d("Make","line is " + line);
                st = new StringTokenizer(line, ";");
                Log.d("test", "String Tokenizer is " + st.toString());
                Log.d("Make","string tokenizers is " + st.countTokens());
                int qNumber = Integer.parseInt(st.nextToken());
                String question = st.nextToken();
                String link;
                if(line.contains(";;"))
                    link = "";
                else
                    link = st.nextToken();
                ArrayList<String> options = new ArrayList<>();
                while(st.hasMoreTokens() ){

                    options.add(st.nextToken());

                }
                //remove the last token since it is the answer choice.
                int answer = Integer.parseInt(options.remove(options.size()-1));

                Question q = new Question(qNumber,question,link,options,answer);
                ques.add(q);
                Log.d("test", "We Added a new question to the question bank.");
                Log.d("test", "Size of question bank is : " + ques.size());
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ques;
    }

    @Override
    protected void onPostExecute(ArrayList<Question> strings) {
        if(strings != null){
            Log.d("test", strings.toString());
            loadDictionary.setMessage("Dictionary Loaded!");
            loadDictionary.setProgress(100);
            loadDictionary.dismiss();
            loadDictionary.setProgress(0);
        }else {
            Log.d("test", "s is null");
        }
    }
}
