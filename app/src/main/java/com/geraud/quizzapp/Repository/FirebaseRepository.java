package com.geraud.quizzapp.Repository;

import androidx.annotation.NonNull;

import com.geraud.quizzapp.Model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseRepository {

    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    private ArrayList<Category> categories = new ArrayList<>();

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
                    categories.add(ds.getValue(Category.class));
                }
                //return all items
                onFirestoreTaskComplete.quizListDataAdded(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFirestoreTaskComplete.onError(databaseError.getMessage());
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void quizListDataAdded(ArrayList<Category> quizListModelsList);

        void onError(String e);
    }

}
