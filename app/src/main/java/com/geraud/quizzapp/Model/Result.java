package com.geraud.quizzapp.Model;

import java.io.Serializable;

public class Result implements Serializable {
    private String title, observation;
    private int correct, wrong, unAnswered;

    public Result() {
    }

    public Result(String title, String observation, int correct, int wrong, int unAnswered) {
        this.title = title;
        this.observation = observation;
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

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
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
