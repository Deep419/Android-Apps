package com.example.inclass04;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class GeneratePasswordAsync extends AsyncTask<String,Void,Void> {

    private IData mIData;

    public GeneratePasswordAsync(IData IData) {
        mIData = IData;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String s = strings[0];
        String[] string = s.split("\\s+");
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=1;i<=Integer.parseInt(string[0]);i++) {
            arrayList.add(Util.getPassword((Integer.parseInt(string[1]))));
        }
        mIData.handleData(arrayList.toArray(new String[0]));
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.d("test", "Pre Async : " + Thread.currentThread().getId());
        mIData.onPre();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("test", "Post Async : " + Thread.currentThread().getId());
        mIData.onPost();
    }

    public interface IData{
        void onPre();
        void handleData(String[] strings);
        void onPost();
    }
}
