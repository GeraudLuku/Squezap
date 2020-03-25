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

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geraud.quizzapp.Model.QuestionsModel;
import com.geraud.quizzapp.Model.TriviaQuestionObject;
import com.geraud.quizzapp.Model.TriviaResponseObject;
import com.geraud.quizzapp.Repository.RetrofitClientInstance;
import com.geraud.quizzapp.Repository.TriviaApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QuizFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "QUIZ_FRAGMENT_LOG";

    private NavController navController;
    private QuizListModel quizListModel;

    //UI Elements
    private TextView quizTitle;
    private Button optionOneBtn;
    private Button optionTwoBtn;
    private Button optionThreeBtn;
    private Button optionFourBtn;
    private Button nextBtn;
    private ImageButton closeBtn;
    private TextView questionFeedback;
    private TextView questionText;
    private TextView questionTime;
    private ProgressBar questionProgress;
    private TextView questionNumber;

    private CountDownTimer countDownTimer;

    private ArrayList<QuestionsModel> questionsModels = new ArrayList<>();

    private boolean canAnswer = false;
    private int currentQuestion = 0;


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

        //UI Initialize
        quizTitle = view.findViewById(R.id.quiz_title);
        optionOneBtn = view.findViewById(R.id.quiz_option_one);
        optionTwoBtn = view.findViewById(R.id.quiz_option_two);
        optionThreeBtn = view.findViewById(R.id.quiz_option_three);
        optionFourBtn = view.findViewById(R.id.quiz_option_four);
        nextBtn = view.findViewById(R.id.quiz_next_btn);
        questionFeedback = view.findViewById(R.id.quiz_question_feedback);
        questionText = view.findViewById(R.id.quiz_question);
        questionTime = view.findViewById(R.id.quiz_question_time);
        questionProgress = view.findViewById(R.id.quiz_question_progress);
        questionNumber = view.findViewById(R.id.quiz_question_number);

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
                            //load questions on screen
                            loadUI();
                        }

                        //get final questions array list


                    } else {
                        Log.d(TAG, "Error Data: " + response.errorBody());
                        quizTitle.setText("Error Loading Data");
                    }
                }

                @Override
                public void onFailure(Call<TriviaResponseObject> call, Throwable t) {
                    Log.d(TAG, "Error: " + t.getMessage());
                }
            });
        } else {
            Log.d(TAG, "Error: " + "QuizListModel is Empty");
        }

        //Set Button Click Listeners
        optionOneBtn.setOnClickListener(this);
        optionTwoBtn.setOnClickListener(this);
        optionThreeBtn.setOnClickListener(this);
        optionFourBtn.setOnClickListener(this);

    }

    private void loadUI() {
        //Quiz Data Loaded, Load the UI
        quizTitle.setText("Quiz Data Loaded");
        questionText.setText("Load First Question");

        //Enable Options
        enableOptions();

        //Load First Question
        loadQuestion(1);
    }

    private void loadQuestion(int questNum) {
        //Set Question Number
        questionNumber.setText(questNum + "");

        //Load Question Text
        questionText.setText(questionsModels.get(questNum).getQuestion());

        //Load Options
        optionOneBtn.setText(questionsModels.get(questNum).getOption_a());
        optionTwoBtn.setText(questionsModels.get(questNum).getOption_b());
        optionThreeBtn.setText(questionsModels.get(questNum).getOption_c());
        optionFourBtn.setText(questionsModels.get(questNum).getAnswer());   //answer will always be last

        //Question Loaded, Set Can Answer
        canAnswer = true;
        currentQuestion = questNum;

        //Start Question Timer
        startTimer();
    }

    private void startTimer() {

        //Set Timer Text
        int timer = new Random().nextInt(10 + 1) + 10; //get random number from 10-20
        final Long timeToAnswer = Long.valueOf(timer);
        questionTime.setText(timeToAnswer.toString());

        //Show Timer ProgressBar
        questionProgress.setVisibility(View.VISIBLE);

        //Start CountDown
        countDownTimer = new CountDownTimer(timeToAnswer * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Update Time
                questionTime.setText(millisUntilFinished / 1000 + "");

                //Progress in percent
                Long percent = millisUntilFinished / (timeToAnswer * 10);
                questionProgress.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                //Time Up, Cannot Answer Question Anymore
                canAnswer = false;
            }
        };

        countDownTimer.start();
    }

    private void enableOptions() {
        //Show All Option Buttons
        optionOneBtn.setVisibility(View.VISIBLE);
        optionTwoBtn.setVisibility(View.VISIBLE);
        optionThreeBtn.setVisibility(View.VISIBLE);
        optionFourBtn.setVisibility(View.VISIBLE);

        //Enable Option Buttons
        optionOneBtn.setEnabled(true);
        optionTwoBtn.setEnabled(true);
        optionThreeBtn.setEnabled(true);
        optionFourBtn.setEnabled(true);

        //Hide Feedback and next Button
        questionFeedback.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        nextBtn.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz_option_one:
                answerSelected(optionOneBtn.getText());
                break;
            case R.id.quiz_option_two:
                answerSelected(optionTwoBtn.getText());
                break;
            case R.id.quiz_option_three:
                answerSelected(optionThreeBtn.getText());
                break;
            case R.id.quiz_option_four:
                answerSelected(optionFourBtn.getText());
                break;
        }
    }

    private void answerSelected(CharSequence selectedAnswer) {
        //Check Answer
        if (canAnswer) {
            if (questionsModels.get(currentQuestion).getAnswer().equals(selectedAnswer)) {
                //Correct Answer
                Log.d(TAG, "Correct Answer");
            } else {
                //Wrong Answer
                Log.d(TAG, "Wrong Answer");
            }
            //Set Can answer to false
            canAnswer = false;
        }
    }

}
