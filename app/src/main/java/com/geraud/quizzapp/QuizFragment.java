package com.geraud.quizzapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geraud.quizzapp.Model.QuestionsModel;
import com.geraud.quizzapp.Model.TriviaQuestionObject;
import com.geraud.quizzapp.Model.TriviaResponseObject;
import com.geraud.quizzapp.Repository.RetrofitClientInstance;
import com.geraud.quizzapp.Repository.TriviaApi;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    private NavController navController;
    private QuizListModel quizListModel;

    private ArrayList<QuestionsModel> questionsModels = new ArrayList<>();


    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get quisListModel object from Details Fragment
        navController = Navigation.findNavController(view);
        quizListModel = QuizFragmentArgs.fromBundle(getArguments()).getFinalQuizListModel();

        //get questions from Trivia API
        //get retrofit questions
        if (quizListModel != null) {
            TriviaApi triviaApi = RetrofitClientInstance.getRetrofitInstance().create(TriviaApi.class);
            Call<TriviaResponseObject> call = triviaApi.getQuestions(quizListModel.getQuestions(), quizListModel.getCategory(), quizListModel.getLevel(), quizListModel.getType());
            call.enqueue(new Callback<TriviaResponseObject>() {
                @Override
                public void onResponse(Call<TriviaResponseObject> call, Response<TriviaResponseObject> response) {
                    if (response.isSuccessful()) {

                        Log.d("TAG", "DATA: " + response.body().getResponse_code());
                        TriviaResponseObject triviaResponseObject = response.body();

                        //get convert it to array of our questions object
                        for (TriviaQuestionObject question : triviaResponseObject.getResults()) {
                            //for each questions object, map it to the questions object we desire
                            QuestionsModel questionsModel = new QuestionsModel();

                            questionsModel.setQuestion(question.getQuestion());
                            questionsModel.setAnswer(question.getCorrect_answer());
                            questionsModel.setOption_a(question.getIncorrect_answers()[0]);
                            questionsModel.setOption_b(question.getIncorrect_answers()[1]);
                            questionsModel.setOption_c(question.getIncorrect_answers()[2]);

                            //add it to questions array
                            questionsModels.add(questionsModel);
                        }

                        //get final questions array list


                    } else {
                        Log.d("TAG", "Error Data: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<TriviaResponseObject> call, Throwable t) {
                    Log.d("TAG", "Error: " + t.getMessage());
                }
            });
        } else {
            Log.d("TAG", "Error: " + "QuizListModel is Empty");
        }
    }
}
