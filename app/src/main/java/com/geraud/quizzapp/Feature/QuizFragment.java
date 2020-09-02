package com.geraud.quizzapp.Feature;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geraud.quizzapp.Model.Category;
import com.geraud.quizzapp.Model.Question;
import com.geraud.quizzapp.Model.Result;
import com.geraud.quizzapp.R;
import com.geraud.quizzapp.ViewModel.QuizListViewModel;

import java.util.ArrayList;


public class QuizFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "QUIZ_FRAGMENT_LOG";

    private NavController navController;
    private QuizListViewModel quizListViewModel;

    private Category category = null;

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

    private ArrayList<Question> questions = new ArrayList<>();

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

        //UI Initialize...
        quizTitle = view.findViewById(R.id.quiz_title);
        optionOneBtn = view.findViewById(R.id.quiz_option_one);
        optionTwoBtn = view.findViewById(R.id.quiz_option_two);
        closeBtn = view.findViewById(R.id.quiz_close_btn);
        optionThreeBtn = view.findViewById(R.id.quiz_option_three);
        optionFourBtn = view.findViewById(R.id.quiz_option_four);
        nextBtn = view.findViewById(R.id.quiz_next_btn);
        questionFeedback = view.findViewById(R.id.quiz_question_feedback);

        questionText = view.findViewById(R.id.quiz_question);
        questionText.setMovementMethod(new ScrollingMovementMethod());

        questionTime = view.findViewById(R.id.quiz_question_time);
        questionProgress = view.findViewById(R.id.quiz_question_progress);
        questionNumber = view.findViewById(R.id.quiz_question_number);

        //get quizListModel object from Details Fragment
        navController = Navigation.findNavController(view);
        category = QuizFragmentArgs.fromBundle(getArguments()).getCategory();

        Log.d("Quiz Fragment", String.format("%s %s %s %s",category.getName(),category.getType(),category.getType(),category.getImage()));

        //get questions from Trivia API
        quizListViewModel = new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuestions(category.getQuestions(), category.getCategory(), category.getLevel(), category.getType()).observe(getViewLifecycleOwner(), new Observer<ArrayList<Question>>() {
            @Override
            public void onChanged(ArrayList<Question> questionArrayList) {
                Log.d("Quiz Frag",questionArrayList.get(0).getTitle());
                questions = questionArrayList;
                loadUI();
            }
        });

        //Set Button Click Listeners
        optionOneBtn.setOnClickListener(this);
        optionTwoBtn.setOnClickListener(this);
        optionThreeBtn.setOnClickListener(this);
        optionFourBtn.setOnClickListener(this);

        nextBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

    }

    private void loadUI() {
        //Quiz Data Loaded, Load the UI
        quizTitle.setText(questions.get(0).getTitle());
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
        questionText.setText(questions.get(questNum - 1).getQuestion());

        //Load Options
        optionOneBtn.setText(questions.get(questNum - 1).getOption_a());
        optionTwoBtn.setText(questions.get(questNum - 1).getOption_b());
        optionThreeBtn.setText(questions.get(questNum - 1).getOption_c());
        optionFourBtn.setText(questions.get(questNum - 1).getOption_d());

        //Question Loaded, Set Can Answer
        canAnswer = true;
        currentQuestion = questNum;

        //Start Question Timer
        startTimer();
    }

    private void startTimer() {

        //Set Timer Text
        //long timeToAnswer = questions.get(currentQuestion - 1).getTimer(); //10 seconds
        long timeToAnswer = 31;
        questionTime.setText(timeToAnswer + "");

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
                if (currentQuestion == questions.size()) {
                    //Load Results
                    submitResults();
                } else {
                    currentQuestion++;
                    loadQuestion(currentQuestion);
                    resetOptions();
                }
                break;
            case R.id.quiz_close_btn:
                //leave fragment and goto results screen

                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to leave this quiz?")
                        .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with leaving operation
                                //total questions - (correct answered + wrong answered)
                                Result result = new Result(questions.get(0).getTitle(), correctAnswers, wrongAnswers,
                                        (category.getQuestions() - (correctAnswers + wrongAnswers)));

                                //navigate to result screen
                                QuizFragmentDirections.ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment(result);
                                navController.navigate(action);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
        }
    }

    private void submitResults() {
        //result object
        Result result = new Result(questions.get(0).getTitle(), correctAnswers, wrongAnswers, notAnswered);

        //navigate to result screen
        QuizFragmentDirections.ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment(result);
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
            if (questions.get(currentQuestion - 1).getAnswer().equals(selectedAnswerBtn.getText())) {
                //Correct Answer
                Log.d(TAG, "Correct Answer");
                correctAnswers += 1;
                selectedAnswerBtn.setBackground(getResources().getDrawable(R.drawable.correct_answer_btn_bg, null));
                //Set Feedback Text
                questionFeedback.setText("Correct Answer");
                questionFeedback.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                //Wrong Answer
                Log.d(TAG, "Wrong Answer");
                //Wrong Answer
                wrongAnswers += 1;
                selectedAnswerBtn.setBackground(getResources().getDrawable(R.drawable.wrong_answer_btn_bg, null));

                //Set Feedback Text
                questionFeedback.setText("Wrong Answer \n Correct Answer : " + questions.get(currentQuestion - 1).getAnswer());
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
        if (currentQuestion == questions.size()) {
            nextBtn.setText("Submit Results");
        }
        questionFeedback.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setEnabled(true);
    }

}
