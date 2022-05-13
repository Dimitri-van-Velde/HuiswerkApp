package com.example.huiswerkapp;

import java.util.ArrayList;
import java.util.Date;

public class Task {
    public static ArrayList<Task> taskArrayList = new ArrayList<>();
    public static String TASK_EDIT_EXTRA = "taskEdit";

    private int id;
    private String title;
    private String description;
    private String subject;
    private String timeEstimated;
    private Date deleted;

    public Task(int id, String title, String description, String subject, String timeEstimated, Date deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.timeEstimated = timeEstimated;
        this.deleted = deleted;
    }

    public Task(int id, String title, String description, String subject, String timeEstimated) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.timeEstimated = timeEstimated;
        deleted = null;
    }

    public static Task getTaskForID(int passedTaskID) {
        for(Task task : taskArrayList) {
            if(task.getId() == passedTaskID) {
                return task;
            }
        }
        return null;
    }

    public static ArrayList<Task> nonDeletedTasks() {
        ArrayList<Task> nonDeleted = new ArrayList<>();
        for(Task task : taskArrayList) {
            if(task.deleted == null) {
                nonDeleted.add(task);
            }
        }

        return nonDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTimeEstimated() {
        return timeEstimated;
    }

    public void setTimeEstimated(String timeEstimated) {
        this.timeEstimated = timeEstimated;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
