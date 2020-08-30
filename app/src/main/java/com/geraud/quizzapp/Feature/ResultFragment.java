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
import android.widget.Toast;

import com.geraud.quizzapp.Model.Result;
import com.geraud.quizzapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class ResultFragment extends Fragment implements RewardedVideoAdListener {

    private static final String ADMOB_APP_ID = "ca-app-pub-4709428567137080~4107467797";

    private RewardedVideoAd mRewardedVideoAd;

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
        // Inflate the layout for getContext() fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);

//        //show Admob
//        // Use an activity context to get the rewarded video instance.
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
//        mRewardedVideoAd.setRewardedVideoAdListener(this);
//        mRewardedVideoAd.loadAd(ADMOB_APP_ID, new AdRequest.Builder().build());
//
//        //Display a rewarded video ad
//        if (mRewardedVideoAd.isLoaded()) {
//            mRewardedVideoAd.show();
//        }

        return view;
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
                navController.navigate(R.id.action_resultFragment_to_listFragment);
            }
        });

        resultPercent = view.findViewById(R.id.results_percent);
        resultProgress = view.findViewById(R.id.results_progress);

        //show results
        resultCorrect.setText(mResult.getCorrect()+ "");
        resultWrong.setText(mResult.getWrong()+ "");
        resultMissed.setText(mResult.getUnAnswered()+ "");

        //Calculate Progress
        long total = (long) mResult.getCorrect() + mResult.getWrong() + mResult.getUnAnswered();
        long percent = (mResult.getCorrect() * 100) / total;

        resultPercent.setText(percent + "%");
        resultProgress.setProgress((int) percent);

    }

//    @Override
//    public void onResume() {
//        mRewardedVideoAd.resume(getActivity());
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        mRewardedVideoAd.pause(getActivity());
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        mRewardedVideoAd.destroy(getActivity());
//        super.onDestroy();
//    }



    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(getContext(), "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(getContext(), "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(getContext(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(getContext(), "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(getContext(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(getContext(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(getContext(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(getContext(), "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }

}
