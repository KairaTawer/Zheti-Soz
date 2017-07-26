package com.zerotoone.n17r.zhetisoz.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by асус on 21.07.2017.
 */
public class AnsweredQuestion implements Parcelable {
    String question,correctAnswer,selectedAnswer;

    public AnsweredQuestion(String question, String correctAnswer, String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    protected AnsweredQuestion(Parcel in) {
        question = in.readString();
        correctAnswer = in.readString();
        selectedAnswer = in.readString();
    }

    public static final Creator<AnsweredQuestion> CREATOR = new Creator<AnsweredQuestion>() {
        @Override
        public AnsweredQuestion createFromParcel(Parcel in) {
            return new AnsweredQuestion(in);
        }

        @Override
        public AnsweredQuestion[] newArray(int size) {
            return new AnsweredQuestion[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(correctAnswer);
        dest.writeString(selectedAnswer);
    }
}
