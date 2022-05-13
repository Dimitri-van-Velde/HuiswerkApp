package com.example.huiswerkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText titleEditText, descEditText;
    private Button deleteButton;
    private Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        initWidgets();
        checkForEditTask();

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (selectedTask != null) {
            toolbar.setTitle("Opdracht Aanpassen");
        } else {
            toolbar.setTitle("Opdracht Toevoegen");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteTaskButton);
    }

    private void checkForEditTask() {
        Intent previousIntent = getIntent();

        int passedTaskID = previousIntent.getIntExtra(Task.TASK_EDIT_EXTRA, -1);
        selectedTask = Task.getTaskForID(passedTaskID);

        if(selectedTask != null) {
            titleEditText.setText(selectedTask.getTitle());
            descEditText.setText(selectedTask.getDescription());
        }
        else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveTask(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());

        if(selectedTask == null) {
            int id = Task.taskArrayList.size();
            Task newTask = new Task(id, title, desc);
            Task.taskArrayList.add(newTask);
            sqLiteManager.addTaskToDatabase(newTask);
        }
        else {
            selectedTask.setTitle(title);
            selectedTask.setDescription(desc);
            sqLiteManager.updateTaskInDB(selectedTask);
        }
        finish();
    }

    public void deleteTask(View view) {
        selectedTask.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateTaskInDB(selectedTask);
        finish();
    }

    @Override
    protected void onResume() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        super.onResume();
        navigationView.setCheckedItem(R.id.nav_addTask);
    }
}