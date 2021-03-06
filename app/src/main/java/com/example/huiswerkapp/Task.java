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
    private Date ownDeadline;
    private Date actualDeadline;
    private String timeEstimated;
    private boolean done;
    private Date dateDone;
    private Date deleted;

    public Task(int id, String title, String description, String subject, Date ownDeadline,
                Date actualDeadline, String timeEstimated, boolean done, Date dateDone, Date deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.ownDeadline = ownDeadline;
        this.actualDeadline = actualDeadline;
        this.timeEstimated = timeEstimated;
        this.done = done;
        this.dateDone = dateDone;
        this.deleted = deleted;
    }

    public Task(int id, String title, String description, String subject, Date ownDeadline,
                Date actualDeadline, String timeEstimated, boolean done, Date dateDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.ownDeadline = ownDeadline;
        this.actualDeadline = actualDeadline;
        this.timeEstimated = timeEstimated;
        this.done = done;
        this.dateDone = dateDone;
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

    public static ArrayList<Task> finishedTasks() {
        ArrayList<Task> finished = new ArrayList<>();
        for(Task task : taskArrayList) {
            if(task.done) {
                finished.add(task);
            }
        }

        return finished;
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

    public Date getOwnDeadline() {
        return ownDeadline;
    }

    public void setOwnDeadline(Date ownDeadline) {
        this.ownDeadline = ownDeadline;
    }

    public Date getActualDeadline() {
        return actualDeadline;
    }

    public void setActualDeadline(Date actualDeadline) {
        this.actualDeadline = actualDeadline;
    }

    public String getTimeEstimated() {
        return timeEstimated;
    }

    public void setTimeEstimated(String timeEstimated) {
        this.timeEstimated = timeEstimated;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getDateDone() {
        return dateDone;
    }

    public void setDateDone(Date dateDone) {
        this.dateDone = dateDone;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
