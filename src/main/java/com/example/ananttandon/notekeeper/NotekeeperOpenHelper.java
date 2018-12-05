package com.example.ananttandon.notekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//adb exec-out "run-as com.example.ananttandon.notekeeper cat database/Notekeeper"> Notekeeper.db


public class NotekeeperOpenHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME ="Notekeeper.bd";//name of the file that will have our database
    public static final int DATABaSE_VERSION = 1;//version of our database

    public NotekeeperOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABaSE_VERSION);
    }

    //this method allows us to interact with database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NotekeeperDatabaseContract.CourseInfoEntry.SQL_CREATE_TABLE);//database creation
        db.execSQL(NotekeeperDatabaseContract.NoteInfoEntry.SQL_CREATE_TABLE);
        DatabaseDataWorker worker = new DatabaseDataWorker(db);
        worker.insertCourses();
        worker.insertSampleNotes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
