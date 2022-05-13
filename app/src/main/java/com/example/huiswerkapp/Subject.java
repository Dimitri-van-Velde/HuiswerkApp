package com.example.huiswerkapp;

import java.util.ArrayList;
import java.util.Date;

public class Subject {
    public static ArrayList<Subject> subjectArrayList = new ArrayList<>();
    public static String SUBJECT_EDIT_EXTRA = "subjectEdit";

    private int id;
    private String name;
    private Date deleted;

    public Subject(int id, String name, Date deleted) {
        this.id = id;
        this.name = name;
        this.deleted = deleted;
    }

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
        deleted = null;
    }

    public static Subject getSubjectForID(int passedSubjectID) {
        for(Subject subject : subjectArrayList) {
            if(subject.getId() == passedSubjectID) {
                return subject;
            }
        }
        return null;
    }

    public static ArrayList<Subject> nonDeletedSubjects() {
        ArrayList<Subject> nonDeleted = new ArrayList<>();
        for(Subject subject : subjectArrayList) {
            if(subject.deleted == null) {
                nonDeleted.add(subject);
            }
        }

        return nonDeleted;
    }

    public static ArrayList<String> nonDeletedSubjectsString() {
        ArrayList<String> nonDeletedString = new ArrayList<>();
        for(Subject subject : subjectArrayList) {
            if(subject.deleted == null) {
                nonDeletedString.add(subject.getName());
            }
        }

        return nonDeletedString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
