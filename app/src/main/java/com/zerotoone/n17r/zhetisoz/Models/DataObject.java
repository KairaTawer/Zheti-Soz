package com.zerotoone.n17r.zhetisoz.Models;

public class DataObject {
    String inEnglish,inKazakh;

    public DataObject(String inEnglish, String inKazakh) {
        this.inEnglish = inEnglish;
        this.inKazakh = inKazakh;
    }

    public String getInEnglish() {
        return inEnglish;
    }

    public void setInEnglish(String inEnglish) {
        this.inEnglish = inEnglish;
    }

    public String getInKazakh() {
        return inKazakh;
    }

    public void setInKazakh(String inKazakh) {
        this.inKazakh = inKazakh;
    }
}