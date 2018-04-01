package com.zerotoone.n17r.zhetisoz.Models;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WordsDbHelper extends SQLiteOpenHelper{
    private static final String TAG = WordsDbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_PATH = "/data/data/com.zerotoone.n17r.zhetisoz/databases/";
    private static final String DATABASE_NAME = "zhetisoz.sqlite";
    private static final String TABLE_NAME = "words";
    private Context context;
    private SQLiteDatabase db;

    public WordsDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public int  getDataCount() {
        String query = "SELECT * FROM '" + TABLE_NAME + "'";

        Cursor cursor = db.rawQuery(query, null);

        return cursor.getCount();
    }




    public void openDataBase () throws SQLException{
        String path = DATABASE_PATH+DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void CopyDataBaseFromAsset() throws IOException {
        InputStream in  = context.getAssets().open(DATABASE_NAME);
        Log.e("sample", "Starting copying");
        String outputFileName = DATABASE_PATH+DATABASE_NAME;
        File databaseFile = new File( "/data/data/YOUR PACKAGE NAME/databases");
        // check if databases folder exists, if not create one and its subfolders
        if (!databaseFile.exists()){
            databaseFile.mkdir();
        }

        OutputStream out = new FileOutputStream(outputFileName);

        byte[] buffer = new byte[1024];
        int length;


        while ((length = in.read(buffer))>0){
            out.write(buffer,0,length);
        }
        Log.e("sample", "Completed" );
        out.flush();
        out.close();
        in.close();

    }

    public void deleteDb() {
        File file = new File(DATABASE_PATH);
        if(file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }
    public boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(DATABASE_PATH);
            checkDB = file.exists();
        } catch(SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;
    }
}