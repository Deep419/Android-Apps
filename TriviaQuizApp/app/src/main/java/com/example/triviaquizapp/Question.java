package com.example.triviaquizapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dghag on 2/19/2018.
 */

public class Question implements Serializable{

    private int number;
    private String question;
    private String url;
    private ArrayList<String> options;

    @Override
    public String toString() {
        return "Question{" +
                "number=" + number +
                ", question='" + question + '\'' +
                ", url='" + url + '\'' +
                ", options=" + options +
                ", answer=" + answer +
                '}';
    }

    private int answer;

    public Question(int number, String question, String url, ArrayList<String> options, int answer) {
        this.number = number;
        this.question = question;
        this.url = url;
        this.options = options;
        this.answer = answer;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
