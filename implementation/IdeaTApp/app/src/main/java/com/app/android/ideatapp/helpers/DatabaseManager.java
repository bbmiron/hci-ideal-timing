package com.app.android.ideatapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.android.ideatapp.home.models.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "tasksdb";
    private static final String TABLE_Tasks = "taskdetails";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_STATUS= "status";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Tasks + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_SOURCE + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Tasks);
        // Create tables again
        onCreate(db);
    }

    // Adding new Task Details
    public long addNewTask(String title, String source, String date, String time, String status) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_TITLE, title);
        cValues.put(KEY_SOURCE, source);
        cValues.put(KEY_STATUS, status);
        if (date != null) {
            cValues.put(KEY_DATE, date);
        }
        if (time != null) {
            cValues.put(KEY_TIME, time);
        }
        // Insert the new row, returning the primary key value of the new row
        long id = db.insert(TABLE_Tasks,null, cValues);
        db.close();
        return id;
    }

    // Adding new Task Details
    public long addNewTask(ItemModel model) {
        return addNewTask(model.getTitle(), model.getSource(), model.getDate(),
                model.getTime(), model.getStatus());
    }

    // Get Task Details
    public List<ItemModel> getTasks() {
        SQLiteDatabase db = getWritableDatabase();
        List<ItemModel> taskList = new ArrayList<>();
        String query = "SELECT title, source, status, date, time FROM "+ TABLE_Tasks;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            ItemModel item = new ItemModel(cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                    cursor.getString(cursor.getColumnIndex(KEY_SOURCE)),
                    cursor.getString(cursor.getColumnIndex(KEY_STATUS)),
                    cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            taskList.add(item);
        }
        cursor.close();
        return taskList;
    }

    // Get Task Details based on taskId
    public List<ItemModel> getTaskById(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<ItemModel> taskList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_Tasks, new String[]{KEY_TITLE, KEY_SOURCE, KEY_STATUS,
                        KEY_DATE, KEY_TIME},
                KEY_ID+ "=?",new String[]{String.valueOf(taskId)},
                null, null, null, null);
        if (cursor.moveToNext()) {
            ItemModel item = new ItemModel(cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                    cursor.getString(cursor.getColumnIndex(KEY_SOURCE)),
                    cursor.getString(cursor.getColumnIndex(KEY_STATUS)),
                    cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_TIME)));
            taskList.add(item);
        }
        cursor.close();
        return taskList;
    }

    // Delete Task Details
    public void deleteTask(long taskId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_Tasks, KEY_ID+" = ?",new String[]{String.valueOf(taskId)});
        db.close();
    }

    // Update Task Details
    public int updateTaskDateTime(long id, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_DATE, date);
        cVals.put(KEY_TIME, time);
        return db.update(TABLE_Tasks, cVals, KEY_ID+" = ?",
                new String[]{String.valueOf(id)});
    }

    public int updateTaskStatus(long id, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_STATUS, status);
        return db.update(TABLE_Tasks, cVals, KEY_ID+" = ?",
                new String[]{String.valueOf(id)});
    }
}
