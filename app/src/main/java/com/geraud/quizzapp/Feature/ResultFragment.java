package com.geraud.quizzapp.Feature;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geraud.quizzapp.Model.Result;
import com.geraud.quizzapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class ResultFragment extends Fragment{

    //private static final String ADMOB_APP_ID = "ca-app-pub-4709428567137080~4107467797";
    private static final String ADMOB_APP_ID = "ca-app-pub-9390048287444061/3221111032";

    private NavController navController;

    private TextView resultCorrect;
    private TextView resultWrong;
    private TextView resultMissed;

    private TextView resultPercent;
    private ProgressBar resultProgress, savingProgress;

    private Result mResult;

    private Button resultHomeBtn;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialized nav controller and get results object
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

                //hide button and show progress bar
                resultHomeBtn.setVisibility(View.INVISIBLE);
                savingProgress.setVisibility(View.VISIBLE);

                //start saving the data to firebase
                //get current user uid
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                //get scrore in percentage
                //Calculate Progress
                long total = (long) mResult.getCorrect() + mResult.getWrong() + mResult.getUnAnswered();
                long percent = (mResult.getCorrect() * 100) / total;

                //create database reference
                FirebaseDatabase.getInstance().getReference().child("QuizScores")
                        .child(userID)
                        .child(mResult.getTitle())
                        .setValue(percent)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //show success toast
                                Toast.makeText(getContext(),"Updated score successfully",Toast.LENGTH_SHORT).show();
                                //if it passed redirect user to home page
                                navController.navigate(R.id.action_resultFragment_to_listFragment);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //if it failed
                        //hide preogress bar and show button
                        resultHomeBtn.setVisibility(View.VISIBLE);
                        savingProgress.setVisibility(View.INVISIBLE);

                        //tell user that it failed
                        Toast.makeText(getContext(),"Failed to upload score",Toast.LENGTH_SHORT).show();
                        Log.d("Failed Upload",e.getLocalizedMessage());
                    }
                });
            }
        });

        resultPercent = view.findViewById(R.id.results_percent);
        resultProgress = view.findViewById(R.id.results_progress);
        savingProgress = view.findViewById(R.id.progressBar);

        //show results
        resultCorrect.setText(mResult.getCorrect()+ "");
        resultWrong.setText(mResult.getWrong()+ "");
        resultMissed.setText(mResult.getUnAnswered()+ "");

        Log.d("Result",mResult.getTitle());  //Entertainment: Books   [String : Int]

        //Calculate Progress
        long total = (long) mResult.getCorrect() + mResult.getWrong() + mResult.getUnAnswered();
        long percent = (mResult.getCorrect() * 100) / total;

        resultPercent.setText(percent + "%");
        resultProgress.setProgress((int) percent);

    }

}
