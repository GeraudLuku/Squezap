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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.geraud.quizzapp.DetailsFragmentArgs;
import com.geraud.quizzapp.DetailsFragmentDirections;
import com.geraud.quizzapp.Model.QuizListModel;
import com.geraud.quizzapp.R;
import com.jaredrummler.materialspinner.MaterialSpinner;


public class DetailsFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private QuizListModel quizListModel;

    private ImageView detailsImage;
    private TextView detailsTitle;
    private TextView detailsDesc;
    private MaterialSpinner detailsDiff;
    private ElegantNumberButton detailsQuestions;

    private Button detailsStartBtn;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        quizListModel = DetailsFragmentArgs.fromBundle(getArguments()).getQuizListModel();

        //Initialize UI Elements
        detailsImage = view.findViewById(R.id.details_image);
        detailsTitle = view.findViewById(R.id.details_title);
        detailsDesc = view.findViewById(R.id.details_desc);


        detailsDiff = view.findViewById(R.id.details_difficulty_text);
        detailsDiff.setItems("Easy", "Medium", "Hard");
        detailsDiff.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //set difficulty to quizLisModel class
                quizListModel.setLevel(item.toLowerCase());
            }
        });

        detailsQuestions = view.findViewById(R.id.details_questions_text);
        detailsQuestions.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                //set number of questions to quizListModel class
                quizListModel.setQuestions(newValue);
            }
        });

        //set information on screen
        Glide.with(getContext())
                .load(quizListModel.getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(detailsImage);

        detailsTitle.setText(quizListModel.getName());
        detailsDesc.setText(quizListModel.getDesc());

        detailsStartBtn = view.findViewById(R.id.details_start_btn);
        detailsStartBtn.setOnClickListener(this);
    }


    //send final quiz model data to quiz fragment
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_start_btn:
                DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment(quizListModel);
                action.setFinalQuizListModel(quizListModel);
                navController.navigate(action);
                break;
        }
    }
}
