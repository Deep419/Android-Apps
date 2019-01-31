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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "mainfrag";
    private RadioGroup mRadioGroup;
    private String department, name, email;
    private int mood=-1;
    private EditText editTextName, editTextEmail;
    private RadioButton selected_rb;
    private SeekBar mSeekBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editTextName = getActivity().findViewById(R.id.editTextName);
        editTextEmail = getActivity().findViewById(R.id.editTextEmail);
        mRadioGroup = getActivity().findViewById(R.id.radioGroup);
        mSeekBar = getActivity().findViewById(R.id.seekBar);
        mSeekBar.setMax(100);
        getActivity().findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = editTextName.getText().toString();
                email = editTextEmail.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.equals("")) {
                    Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Select Department", Toast.LENGTH_SHORT).show();
                    return;
                }
                selected_rb = getActivity().findViewById(mRadioGroup.getCheckedRadioButtonId());
                department = String.valueOf(selected_rb.getText());
                mood = mSeekBar.getProgress();

                Student student = new Student(name, email, department, mood);
                Log.d(TAG, "Student : " +student.toString());
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

        public void gotoDisplayFragment(Student student);
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
