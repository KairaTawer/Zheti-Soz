package com.zerotoone.n17r.zhetisoz.Models;

import android.provider.BaseColumns;

/**
 * Created by асус on 17.07.2017.
 */
public final class UsedWordsContract {

    private UsedWordsContract() {}

    public static class WordsEntry implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME_ENGLISH = "in_english";
        public static final String COLUMN_NAME_KAZAKH = "in_kazakh";
    }
}