package com.example.studentform_with_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STUDENT_KEY = "STUDENT";
    private static final String ACTION = "ACTION";

    private Student student;
    private String action;

    private OnFragmentInteractionListener mListener;

    private EditText name, email;
    private RadioGroup depart;
    private RadioButton selected_rb;
    private SeekBar moodbar;
    private TextView department, ycm;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(Student param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(STUDENT_KEY, param1);
        args.putString(ACTION, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable(STUDENT_KEY);
            action = getArguments().getString(ACTION);
            Log.d("edit", "onCreate: Student " + student.toString() + " action : " + action);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        department = getActivity().findViewById(R.id.tv_department);
        ycm = getActivity().findViewById(R.id.tv_ycm);

        name = getActivity().findViewById(R.id.editText3Name);
        name.setText(student.name);
        email = getActivity().findViewById(R.id.editText3Email);
        email.setText(student.email);
        depart = getActivity().findViewById(R.id.radioGroup3);
        String id = student.department;
        RadioButton selected = getActivity().findViewById(R.id.rb_sis);
        if(selected.getText().equals(id)){
            selected.setChecked(true);
        }
        selected = getActivity().findViewById(R.id.rb_cs);
        if(selected.getText().equals(id)){
            selected.setChecked(true);
        }
        selected = getActivity().findViewById(R.id.rb_bio);
        if(selected.getText().equals(id)){
            selected.setChecked(true);
        }
        selected = getActivity().findViewById(R.id.rb_other);
        if(selected.getText().equals(id)){
            selected.setChecked(true);
        }
        moodbar = getActivity().findViewById(R.id.seekBar3) ;
        moodbar.setProgress(student.mood);

        switch (action) {
            case "name":
                email.setVisibility(View.INVISIBLE);
                depart.setVisibility(View.INVISIBLE);
                moodbar.setVisibility(View.INVISIBLE);
                department.setVisibility(View.INVISIBLE);
                ycm.setVisibility(View.INVISIBLE);
                break;
            case "email":
                name.setVisibility(View.INVISIBLE);
                depart.setVisibility(View.INVISIBLE);
                moodbar.setVisibility(View.INVISIBLE);
                department.setVisibility(View.INVISIBLE);
                ycm.setVisibility(View.INVISIBLE);
                break;
            case "depart":
                email.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                moodbar.setVisibility(View.INVISIBLE);
                ycm.setVisibility(View.INVISIBLE);
                break;
            case "mood":
                email.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                depart.setVisibility(View.INVISIBLE);
                department.setVisibility(View.INVISIBLE);
                break;
        }

        getActivity().findViewById(R.id.button_save).setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (action) {
                    case "name":
                        if (name.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        student.setName(name.getText().toString());
                        break;
                    case "email":
                        if (email.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        student.setEmail(email.getText().toString());
                        break;
                    case "depart":
                        selected_rb = getActivity().findViewById(depart.getCheckedRadioButtonId());
                        student.setDepartment(String.valueOf(selected_rb.getText()));
                        break;
                    case "mood":
                        student.setMood(moodbar.getProgress());
                        break;
                }
                mListener.gotoDisplayFragment(student);
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void gotoDisplayFragment(Student student);
        void onFragmentInteraction(Uri uri);
    }
}
