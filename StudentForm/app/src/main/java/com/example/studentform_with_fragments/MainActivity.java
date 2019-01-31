package com.example.studentform_with_fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,
        DisplayFragment.OnFragmentInteractionListener, EditFragment.OnFragmentInteractionListener {

    private static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.container,new MainFragment(),"mainfragment").commit();
    }

    @Override
    public void gotoDisplayFragment(Student student) {
        Log.d(TAG, "gotoDisplayFragment: " + student.toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, DisplayFragment.newInstance(student), "displayFrament").commit();
    }

    @Override
    public void gotoEditFragment(Student student, String action) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, EditFragment.newInstance(student,action), "editfragment").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
