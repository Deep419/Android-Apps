package com.example.studentform_with_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayFragment extends Fragment {
    private static final String STUDENT_KEY = "student_key";

    private Student mStudent;
    private TextView mTextViewName, mTextViewEmail,mTextViewDepart, mTextViewMood;


    private OnFragmentInteractionListener mListener;

    public DisplayFragment() {
        // Required empty public constructor
    }

    public static DisplayFragment newInstance(Student student) {
        DisplayFragment fragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(STUDENT_KEY, student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStudent = (Student) getArguments().getSerializable(STUDENT_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextViewName = getActivity().findViewById(R.id.textView2DispName);
        mTextViewEmail = getActivity().findViewById(R.id.textView2DispEmail);
        mTextViewDepart = getActivity().findViewById(R.id.textView2DispDepart);
        mTextViewMood = getActivity().findViewById(R.id.textView2DispMood);

        mTextViewName.setText(mStudent.name);
        mTextViewEmail.setText(mStudent.email);
        mTextViewDepart.setText(mStudent.department);
        mTextViewMood.setText(String.valueOf(mStudent.mood));

        getActivity().findViewById(R.id.imageViewName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoEditFragment(mStudent,"name");
            }
        });

        getActivity().findViewById(R.id.imageViewEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoEditFragment(mStudent,"email");
            }
        });

        getActivity().findViewById(R.id.imageViewDepart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoEditFragment(mStudent,"depart");
            }
        });

        getActivity().findViewById(R.id.imageViewMood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoEditFragment(mStudent,"mood");
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
        public void gotoEditFragment(Student student,String action);
        void onFragmentInteraction(Uri uri);
    }
}
