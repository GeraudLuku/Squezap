package com.geraud.quizzapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseRepository {

    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    private ArrayList<QuizListModel> quizListModels= new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("QuizCategories");


    public FirebaseRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }


    //get list of quiz categories from FIREBASE
    public void getQuizData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //store all values in a single list
                    quizListModels.add(ds.getValue(QuizListModel.class));
                }
                //return all items
                onFirestoreTaskComplete.quizListDataAdded(quizListModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFirestoreTaskComplete.onError(databaseError.getMessage());
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void quizListDataAdded(ArrayList<QuizListModel> quizListModelsList);

        void onError(String e);
    }

}
