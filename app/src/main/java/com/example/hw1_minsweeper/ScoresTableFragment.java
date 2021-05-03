package com.example.hw1_minsweeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScoresTableFragment extends Fragment {

    interface ScoresTableFragmentListener {

        void scoreTableRequested();

    }

    private ScoresTableFragmentListener mListener;
    private TextView mTextView;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
   //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
  //  private String mParam1;
 //   private String mParam2;

    public ScoresTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment scoresTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoresTableFragment newInstance(String param1, String param2) {
        ScoresTableFragment fragment = new ScoresTableFragment();
        Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
   //    args.putString(ARG_PARAM2, param2);
   //     fragment.setArguments(args);
       return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*      if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scores_table, container, false);
        mTextView = (TextView)view.findViewById(R.id.scoreTableFragment);
        mTextView.setFocusableInTouchMode(true);
        mTextView.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                if(ScoresTableFragment.this.mListener != null) {

                    ScoresTableFragment.this.mListener.scoreTableRequested();

                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ScoresTableFragmentListener listener = (ScoresTableFragmentListener) context;

            if (listener != null)
                this.registerListener(listener);
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + " must implement BottomFragmentListener");

        }
    }

    public void registerListener(ScoresTableFragmentListener listener) {

        this.mListener = listener;
    }
    public void moveToFullScoreActivity(Context context){
        Intent intent = new Intent(context, FullScoreTableActivity.class);
        startActivity(intent);

    }
}
