package com.geraud.quizzapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class QuizListViewModel extends ViewModel implements FirebaseRepository.OnFirestoreTaskComplete {

    private MutableLiveData<ArrayList<QuizListModel>> quizListModelData = new MutableLiveData<>();

    public LiveData<ArrayList<QuizListModel>> getQuizListModelData() {
        return quizListModelData;
    }

    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);

    public QuizListViewModel() {
        firebaseRepository.getQuizData();
    }

    @Override
    public void quizListDataAdded(ArrayList<QuizListModel> quizListModelsList) {
        quizListModelData.setValue(quizListModelsList);
    }

    @Override
    public void onError(String e) {
        Log.e("QUIZLISTVIEWMODEL", e);
    }
}
