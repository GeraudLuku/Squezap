package com.geraud.quizzapp.Model;

import java.io.Serializable;

public class Result implements Serializable {
    private String title;
    private int correct, wrong, unAnswered;

    public Result() {
    }

    public Result(String title, int correct, int wrong, int unAnswered) {
        this.title = title;
        this.correct = correct;
        this.wrong = wrong;
        this.unAnswered = unAnswered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getUnAnswered() {
        return unAnswered;
    }

    public void setUnAnswered(int unAnswered) {
        this.unAnswered = unAnswered;
    }
}
