package com.zerotoone.n17r.zhetisoz.Models;

import java.util.List;

/**
 * Created by асус on 19.07.2017.
 */
public class Question {
    String question,correctAnswer;
    List<String> otherVariances;

    public Question(String questions, String correctAnswer, List<String> otherVariances) {
        this.question = questions;
        this.correctAnswer = correctAnswer;
        this.otherVariances = otherVariances;
    }

    public Question() {}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String questions) {
        this.question = questions;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOtherVariances() {
        return otherVariances;
    }

    public void setOtherVariances(List<String> otherVariances) {
        this.otherVariances = otherVariances;
    }
}
