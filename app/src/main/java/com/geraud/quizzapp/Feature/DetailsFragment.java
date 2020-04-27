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
import com.geraud.quizzapp.Model.Category;
import com.geraud.quizzapp.R;
import com.jaredrummler.materialspinner.MaterialSpinner;


public class DetailsFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private Category category;

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

        //instantiate navigation controller and get Category object
        navController = Navigation.findNavController(view);
        category = DetailsFragmentArgs.fromBundle(getArguments()).getCategory();

        //Initialize UI Elements
        detailsImage = view.findViewById(R.id.details_image);
        detailsTitle = view.findViewById(R.id.details_title);
        detailsDesc = view.findViewById(R.id.details_desc);


        //get and set category difficulty default difficulty is Easy
        detailsDiff = view.findViewById(R.id.details_difficulty_text);
        detailsDiff.setItems("Easy", "Medium", "Hard");
        detailsDiff.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //set difficulty to Category class
                category.setLevel(item.toLowerCase());
            }
        });

        //get and set number of questions
        detailsQuestions = view.findViewById(R.id.details_questions_text);
        detailsQuestions.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                //set number of questions to category class
                category.setQuestions(newValue);
            }
        });

        //set category image on screen
        Glide.with(view.getContext())
                .load(category.getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(detailsImage);

        //set title and description of category
        detailsTitle.setText(category.getName());
        detailsDesc.setText(category.getDesc());

        //start quiz button
        detailsStartBtn = view.findViewById(R.id.details_start_btn);
        detailsStartBtn.setOnClickListener(this);
    }


    //send final quiz model data to quiz fragment
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_start_btn:
//                DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment(category);
//                navController.navigate(action);
                DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment detailsFragmentToQuizFragment = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment(category);
                navController.navigate(detailsFragmentToQuizFragment);
                break;
        }
    }
}
