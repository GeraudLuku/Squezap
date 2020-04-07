package com.geraud.quizzapp.Feature;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geraud.quizzapp.Model.Result;
import com.geraud.quizzapp.R;
import com.geraud.quizzapp.ResultFragmentArgs;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {

    private NavController navController;

    private TextView resultCorrect;
    private TextView resultWrong;
    private TextView resultMissed;

    private TextView resultPercent;
    private ProgressBar resultProgress;

    private Result mResult;

    private Button resultHomeBtn;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        mResult = ResultFragmentArgs.fromBundle(getArguments()).getResult();

        //Initialize UI Elements
        resultCorrect = view.findViewById(R.id.results_correct_text);
        resultWrong = view.findViewById(R.id.results_wrong_text);
        resultMissed = view.findViewById(R.id.results_missed_text);

        resultHomeBtn = view.findViewById(R.id.results_home_btn);
        resultHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_resultFragment_to_listFragment);
            }
        });

        resultPercent = view.findViewById(R.id.results_percent);
        resultProgress = view.findViewById(R.id.results_progress);

        //show results
        resultCorrect.setText(mResult.getCorrect());
        resultWrong.setText(mResult.getWrong());
        resultMissed.setText(mResult.getUnAnswered());

        //Calculate Progress
        long total = (long) mResult.getCorrect() + mResult.getWrong() + mResult.getUnAnswered();
        long percent = (mResult.getCorrect() * 100) / total;

        resultPercent.setText(percent + "%");
        resultProgress.setProgress((int) percent);

    }
}
