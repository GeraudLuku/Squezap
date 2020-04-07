package com.geraud.quizzapp.Repository;

import android.util.Log;

import com.geraud.quizzapp.Model.Question;
import com.geraud.quizzapp.Model.TriviaQuestionObject;
import com.geraud.quizzapp.Model.TriviaResponseObject;

import java.util.ArrayList;
import java.util.Random;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RetrofitRepository {

    private static RetrofitRepository retrofitRepository;
    private TriviaApi triviaApi;

    public static RetrofitRepository getInstance() {
        if (retrofitRepository == null) {
            retrofitRepository = new RetrofitRepository();
        }
        return retrofitRepository;
    }

    public RetrofitRepository() {
        triviaApi = RetrofitClientInstance.getRetrofitInstance().create(TriviaApi.class);
    }

    public MutableLiveData<ArrayList<Question>> getQuestions(int length, int category, String level, String type) {
        final MutableLiveData<ArrayList<Question>> questions = new MutableLiveData<>();
        triviaApi.getQuestions(length, category, level, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<TriviaResponseObject, ArrayList<Question>>() {
                    @Override
                    public ArrayList<Question> apply(TriviaResponseObject triviaResponseObject) throws Exception {

                        //List of formatted questions
                        ArrayList<Question> finalQuestions = new ArrayList<>();

                        //Get each individual questions object
                        for (TriviaQuestionObject triviaQuestionObject : triviaResponseObject.getResults()) {
                            //Transform the questions
                            Question question = new Question();

                            //random number from 0 to 3
                            int answerIndex = new Random().nextInt(4);

                            //set question
                            question.setQuestion(triviaQuestionObject.getQuestion());

                            //set answer
                            question.setAnswer(triviaQuestionObject.getCorrect_answer());

                            //answer choices
                            ArrayList<String> answerChoices = triviaQuestionObject.getIncorrect_answers();
                            answerChoices.add(answerIndex, triviaQuestionObject.getCorrect_answer());
                            //set answer options
                            question.setOption_a(answerChoices.get(0));
                            question.setOption_b(answerChoices.get(1));
                            question.setOption_c(answerChoices.get(2));
                            question.setOption_d(answerChoices.get(3));

                            //set title category of question
                            question.setTitle(triviaQuestionObject.getCategory());

                            //add question to list of questions
                            finalQuestions.add(question);

                        }

                        return finalQuestions;

                    }
                })
                .subscribeWith(new DisposableSingleObserver<ArrayList<Question>>() {
                    @Override
                    public void onSuccess(ArrayList<Question> questionArrayList) {
                        //received all questions
                        Log.d(getClass().getSimpleName(), "Success : Received questions");
                        //return question object
                        questions.setValue(questionArrayList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Network Error
                        Log.d(getClass().getSimpleName(), "Network Error : " + e.getMessage());
                    }
                });
        return questions;
    }
}
