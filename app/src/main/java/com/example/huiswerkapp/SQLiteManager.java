package com.example.huiswerkapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "DB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Task";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DESC_FIELD = "desc";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if(sqLiteManager  == null) {
            sqLiteManager = new SQLiteManager(context);
        }

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addTaskToDatabase(Task task)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, task.getId());
        contentValues.put(TITLE_FIELD, task.getTitle());
        contentValues.put(DESC_FIELD, task.getDescription());
        contentValues.put(DELETED_FIELD, getStringFromDate(task.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateTaskListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String desc = result.getString(3);
                    String stringDeleted = result.getString(4);
                    Date deleted = getDateFromString(stringDeleted);
                    Task task = new Task(id, title, desc, deleted);
                    Task.taskArrayList.add(task);
                }
            }
        }
    }

    public void updateTaskInDB(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, task.getId());
        contentValues.put(TITLE_FIELD, task.getTitle());
        contentValues.put(DESC_FIELD, task.getDescription());
        contentValues.put(DELETED_FIELD, getStringFromDate(task.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(task.getId())});
    }

    private String getStringFromDate(Date date) {
        if(date == null)
        {
            return null;
        }
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
