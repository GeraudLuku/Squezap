package com.geraud.quizzapp.Model;

import java.util.List;

public class TriviaResponseObject {
    private int response_code;
    private List<TriviaQuestionObject> results;

    //empty constructor
    public TriviaResponseObject() {
    }

    public TriviaResponseObject(int response_code, List<TriviaQuestionObject> results) {
        this.response_code = response_code;
        this.results = results;
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public List<TriviaQuestionObject> getResults() {
        return results;
    }

    public void setResults(List<TriviaQuestionObject> results) {
        this.results = results;
    }
}
