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

    private static final String DATABASE_NAME = "DB6";
    private static final int DATABASE_VERSION = 1;
    private static final String TASK_TABLE_NAME = "Task";
    private static final String SUBJECT_TABLE_NAME = "Subject";
    private static final String TASK_COUNTER = "Counter";
    private static final String SUBJECT_COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DESC_FIELD = "desc";
    private static final String SUBJECT_FIELD = "subject";
    private static final String OWN_DEADLINE_FIELD = "own_deadline";
    private static final String ACTUAL_DEADLINE_FIELD = "actual_deadline";
    private static final String ESTIMATED_FIELD = "time_estimated";
    private static final String DELETED_FIELD = "deleted";
    private static final String NAME_FIELD = "name";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormatDeadline = new SimpleDateFormat("MM-dd-yyyy");

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
        StringBuilder task_sql;
        task_sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TASK_TABLE_NAME)
                .append("(")
                .append(TASK_COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT, ")
                .append(SUBJECT_FIELD)
                .append(" TEXT, ")
                .append(OWN_DEADLINE_FIELD)
                .append(" TEXT, ")
                .append(ACTUAL_DEADLINE_FIELD)
                .append(" TEXT, ")
                .append(ESTIMATED_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(task_sql.toString());

        StringBuilder subject_sql;
        subject_sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(SUBJECT_TABLE_NAME)
                .append("(")
                .append(SUBJECT_COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(NAME_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(subject_sql.toString());
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
        contentValues.put(SUBJECT_FIELD, task.getSubject());
        contentValues.put(OWN_DEADLINE_FIELD, getStringFromDeadlineDate(task.getOwnDeadline()));
        contentValues.put(ACTUAL_DEADLINE_FIELD, getStringFromDeadlineDate(task.getActualDeadline()));
        contentValues.put(ESTIMATED_FIELD, task.getTimeEstimated());
        contentValues.put(DELETED_FIELD, getStringFromDate(task.getDeleted()));

        sqLiteDatabase.insert(TASK_TABLE_NAME, null, contentValues);
    }

    public void addSubjectToDatabase(Subject subject)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, subject.getId());
        contentValues.put(NAME_FIELD, subject.getName());
        contentValues.put(DELETED_FIELD, getStringFromDate(subject.getDeleted()));

        sqLiteDatabase.insert(SUBJECT_TABLE_NAME, null, contentValues);
    }

    public void populateTaskListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Task.taskArrayList.clear();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TASK_TABLE_NAME +
                " ORDER BY " + OWN_DEADLINE_FIELD + " ASC, " +
                ACTUAL_DEADLINE_FIELD + " ASC", null)) {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String desc = result.getString(3);
                    String subject = result.getString(4);
                    String stringOwnDeadline = result.getString(5);
                    String stringActualDeadline = result.getString(6);
                    String timeEstimated = result.getString(7);
                    String stringDeleted = result.getString(8);
                    Date ownDeadline = getDeadlineDateFromString(stringOwnDeadline);
                    Date actualDeadline = getDeadlineDateFromString(stringActualDeadline);
                    Date deleted = getDateFromString(stringDeleted);
                    Task task = new Task(id, title, desc, subject, ownDeadline, actualDeadline, timeEstimated, deleted);
                    Task.taskArrayList.add(task);
                }
            }
        }
    }

    public void populateSubjectListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Subject.subjectArrayList.clear();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + SUBJECT_TABLE_NAME, null)) {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(1);
                    String name = result.getString(2);
                    String stringDeleted = result.getString(3);
                    Date deleted = getDateFromString(stringDeleted);
                    Subject subject = new Subject(id, name, deleted);
                    Subject.subjectArrayList.add(subject);
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
        contentValues.put(SUBJECT_FIELD, task.getSubject());
        contentValues.put(OWN_DEADLINE_FIELD, getStringFromDeadlineDate(task.getOwnDeadline()));
        contentValues.put(ACTUAL_DEADLINE_FIELD, getStringFromDeadlineDate(task.getActualDeadline()));
        contentValues.put(ESTIMATED_FIELD, task.getTimeEstimated());
        contentValues.put(DELETED_FIELD, getStringFromDate(task.getDeleted()));

        sqLiteDatabase.update(TASK_TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(task.getId())});
    }

    public void updateSubjectInDB(Subject subject) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, subject.getId());
        contentValues.put(NAME_FIELD, subject.getName());
        contentValues.put(DELETED_FIELD, getStringFromDate(subject.getDeleted()));

        sqLiteDatabase.update(SUBJECT_TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(subject.getId())});
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

    private String getStringFromDeadlineDate(Date date) {
        if(date == null)
        {
            return null;
        }
        return dateFormatDeadline.format(date);
    }

    private Date getDeadlineDateFromString(String string) {
        try {
            return dateFormatDeadline.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
