package com.geraud.quizzapp;

import java.io.Serializable;

public class QuizListModel implements Serializable {

    private String name, desc, image, level, type;
    private int questions, category;

    public QuizListModel() {
    }

    public QuizListModel(String name, String desc, String image, String level, String type, int questions, int category) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.level = level;
        this.type = type;
        this.questions = questions;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
