package com.example.todolistapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolistapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.PropertyReference0Impl;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE "+TODO_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +TASK+"TEXT, "+STATUS+" INTEGER)";
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         sqLiteDatabase.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the older tables
      sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TODO_TABLE);
      //create table again
        onCreate(sqLiteDatabase);
    }
    public void openDatabase() {
        sqLiteDatabase = getWritableDatabase();
    }
    public void insertTask(ToDoModel toDoModel) {
        ContentValues cv = new ContentValues();
        cv.put(TASK,toDoModel.getTask());
        cv.put(STATUS,toDoModel.getStatus());
        sqLiteDatabase.insert(TODO_TABLE,null,cv);
    }
    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        sqLiteDatabase.beginTransaction();
        try{
            cursor = sqLiteDatabase.query(TODO_TABLE,null,null,null,null,null,null);
            if (cursor!=null) {
                if (cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") ToDoModel toDoModel = new ToDoModel(cursor.getInt(cursor.getColumnIndex(ID)),cursor.getInt(cursor.getColumnIndex(STATUS)),cursor.getString(cursor.getColumnIndex(TASK)));
                        taskList.add(toDoModel);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return taskList;
    }
    public void updateStatus(int id,int status) {
       ContentValues cv = new ContentValues();
       cv.put(STATUS,status);
       sqLiteDatabase.update(TODO_TABLE,cv,ID+"=?",new String[] {String.valueOf(id)});
    }
    public void updateTask(int id,String task) {
      ContentValues cv = new ContentValues();
      cv.put(TASK,task);
      sqLiteDatabase.update(TODO_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id) {
       sqLiteDatabase.delete(TODO_TABLE,ID+"=?",new String[]{String.valueOf(id)});
    }
}
