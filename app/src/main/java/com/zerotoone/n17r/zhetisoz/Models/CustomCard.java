package com.zerotoone.n17r.zhetisoz.Models;

/**
 * Created by асус on 16.07.2017.
 */
public class CustomCard {

    int tag;
    String word;

    public CustomCard(int tag, String word) {
        this.tag = tag;
        this.word = word;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
