package com.geraud.quizzapp.Feature;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

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
import com.geraud.quizzapp.Model.QuizListModel;
import com.geraud.quizzapp.Model.Result;
import com.geraud.quizzapp.Model.TriviaQuestionObject;
import com.geraud.quizzapp.Model.TriviaResponseObject;
import com.geraud.quizzapp.QuizFragmentArgs;
import com.geraud.quizzapp.QuizFragmentDirections;
import com.geraud.quizzapp.QuizFragmentDirections.ActionQuizFragmentToResultFragment;
import com.geraud.quizzapp.R;
import com.geraud.quizzapp.Repository.RetrofitClientInstance;
import com.geraud.quizzapp.Repository.TriviaApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class QuizFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "QUIZ_FRAGMENT_LOG";

    private NavController navController;
    private QuizListModel quizListModel = null;

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

    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int notAnswered = 0;


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
            //testing RxJava here
            TriviaApi triviaApi = RetrofitClientInstance.getRetrofitInstance().create(TriviaApi.class);

            //fetching questions
            triviaApi.getQuestions(10, 11, "medium", "multiple")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<TriviaResponseObject, ArrayList<QuestionsModel>>() {
                        @Override
                        public ArrayList<QuestionsModel> apply(TriviaResponseObject triviaResponseObject) throws Exception {

                            //List of formatted questions
                            ArrayList<QuestionsModel> finalQuestions = new ArrayList<>();

                            //Get each individual questions object
                            for (TriviaQuestionObject triviaQuestionObject : triviaResponseObject.getResults()) {
                                //Transform the questions
                                QuestionsModel questionsModel = new QuestionsModel();

                                //random number from 0 to 3
                                int answerIndex = new Random().nextInt(4);

                                //set question
                                questionsModel.setQuestion(triviaQuestionObject.getQuestion());

                                //set answer
                                questionsModel.setAnswer(triviaQuestionObject.getCorrect_answer());

                                //answer choices
                                ArrayList<String> answerChoices = triviaQuestionObject.getIncorrect_answers();
                                answerChoices.add(answerIndex, triviaQuestionObject.getCorrect_answer());
                                //set answer options
                                questionsModel.setOption_a(answerChoices.get(0));
                                questionsModel.setOption_b(answerChoices.get(1));
                                questionsModel.setOption_c(answerChoices.get(2));
                                questionsModel.setOption_d(answerChoices.get(3));

                                //set title category of question
                                questionsModel.setTitle(triviaQuestionObject.getCategory());

                                //add question to list of questions
                                finalQuestions.add(questionsModel);

                            }

                            return finalQuestions;

                        }
                    })
                    .subscribeWith(new DisposableSingleObserver<ArrayList<QuestionsModel>>() {
                        @Override
                        public void onSuccess(ArrayList<QuestionsModel> questions) {
                            //recieve all questions
                            Log.d(TAG, "Success : " + questionsModels.get(0).getQuestion());

                            questionsModels = questions;
                            loadUI();
                        }

                        @Override
                        public void onError(Throwable e) {
                            //Network Error
                            Log.d(TAG, "Network Error : " + e.getMessage());
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

        nextBtn.setOnClickListener(this);

    }

    private void loadUI() {
        //Quiz Data Loaded, Load the UI
        quizTitle.setText(questionsModels.get(0).getTitle());
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
        questionText.setText(questionsModels.get(questNum - 1).getQuestion());

        //Load Options
        optionOneBtn.setText(questionsModels.get(questNum - 1).getOption_a());
        optionTwoBtn.setText(questionsModels.get(questNum - 1).getOption_b());
        optionThreeBtn.setText(questionsModels.get(questNum - 1).getOption_c());
        optionFourBtn.setText(questionsModels.get(questNum - 1).getOption_d());

        //Question Loaded, Set Can Answer
        canAnswer = true;
        currentQuestion = questNum;

        //Start Question Timer
        startTimer();
    }

    private void startTimer() {

        //Set Timer Text
        int timer = new Random().nextInt(10 + 1) + 10; //get random number from 10-20
        final Long timeToAnswer = (long) timer;
        questionTime.setText(timeToAnswer.toString());

        //Show Timer ProgressBar
        questionProgress.setVisibility(View.VISIBLE);

        //Start CountDown
        countDownTimer = new CountDownTimer(timeToAnswer * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Update Time
                questionTime.setText(String.format("%d", millisUntilFinished / 1000));

                //Progress in percent
                long percent = millisUntilFinished / (timeToAnswer * 10);
                questionProgress.setProgress((int) percent);
            }

            @Override
            public void onFinish() {
                //Time Up, Cannot Answer Question Anymore
                canAnswer = false;

                questionFeedback.setText("Time Up! No answer was submitted.");
                questionFeedback.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                notAnswered++;
                showNextBtn();
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
                verifyAnswer(optionOneBtn);
                break;
            case R.id.quiz_option_two:
                verifyAnswer(optionTwoBtn);
                break;
            case R.id.quiz_option_three:
                verifyAnswer(optionThreeBtn);
                break;
            case R.id.quiz_option_four:
                verifyAnswer(optionFourBtn);
                break;
            case R.id.quiz_next_btn:
                if(currentQuestion == 10){
                    //Load Results
                    submitResults();
                } else {
                    currentQuestion++;
                    loadQuestion(currentQuestion);
                    resetOptions();
                }
                break;
        }
    }

    private void submitResults() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswers);
        resultMap.put("wrong", wrongAnswers);
        resultMap.put("unanswered", notAnswered);

        Result result = new Result(questionsModels.get(0).getTitle(),correctAnswers,wrongAnswers,notAnswered);

        //navigate to result screen
        ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment(Result.class);
        action.setResult(result);
        navController.navigate(action);
    }

    private void resetOptions() {
        optionOneBtn.setBackground(getResources().getDrawable(R.drawable.outline_light_btn_bg, null));
        optionTwoBtn.setBackground(getResources().getDrawable(R.drawable.outline_light_btn_bg, null));
        optionThreeBtn.setBackground(getResources().getDrawable(R.drawable.outline_light_btn_bg, null));

        optionOneBtn.setTextColor(getResources().getColor(R.color.colorLightText, null));
        optionTwoBtn.setTextColor(getResources().getColor(R.color.colorLightText, null));
        optionThreeBtn.setTextColor(getResources().getColor(R.color.colorLightText, null));

        questionFeedback.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        nextBtn.setEnabled(false);
    }


    private void verifyAnswer(Button selectedAnswerBtn) {
        //Check Answer
        if (canAnswer) {
            //Set Answer Btn Text Color to Black
            selectedAnswerBtn.setTextColor(getResources().getColor(R.color.colorDark, null));
            if (questionsModels.get(currentQuestion - 1).getAnswer().equals(selectedAnswerBtn.getText())) {
                //Correct Answer
                Log.d(TAG, "Correct Answer");
                correctAnswers++;
                selectedAnswerBtn.setBackground(getResources().getDrawable(R.drawable.correct_answer_btn_bg, null));
                //Set Feedback Text
                questionFeedback.setText("Correct Answer");
                questionFeedback.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                //Wrong Answer
                Log.d(TAG, "Wrong Answer");
                //Wrong Answer
                wrongAnswers++;
                selectedAnswerBtn.setBackground(getResources().getDrawable(R.drawable.wrong_answer_btn_bg, null));

                //Set Feedback Text
                questionFeedback.setText("Wrong Answer \n \n Correct Answer : " + questionsModels.get(currentQuestion-1).getAnswer());
                questionFeedback.setTextColor(getResources().getColor(R.color.colorAccent, null));
            }
            //Set Can answer to false
            canAnswer = false;

            //Stop The Timer
            countDownTimer.cancel();

            //Show Next Button
            showNextBtn();
        }
    }

    private void showNextBtn() {
        if (currentQuestion == 10) {
            nextBtn.setText("Submit Results");
        }
        questionFeedback.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setEnabled(true);
    }

}
