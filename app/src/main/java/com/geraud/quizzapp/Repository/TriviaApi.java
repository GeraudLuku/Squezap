package com.geraud.quizzapp.Repository;


import com.geraud.quizzapp.Model.TriviaResponseObject;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaApi {


    // /api.php?amount=10&category=10&difficulty=easy&type=multiple
    @GET("api.php?")
    Single<TriviaResponseObject> getQuestions(
            @Query("amount") int length,
            @Query("category") int category,
            @Query("difficulty") String level,
            @Query("type") String type
    );

}
