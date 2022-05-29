package com.example.huiswerkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class FinishedTaskDetailActivity extends AppCompatActivity {

    private Button unfinishButton;
    private Task selectedTask;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_task_detail);
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
        unfinishButton = findViewById(R.id.unfinishedTaskButton);
    }

    private void checkForEditTask() {
        Intent previousIntent = getIntent();

        int passedTaskID = previousIntent.getIntExtra(Task.TASK_EDIT_EXTRA, -1);
        selectedTask = Task.getTaskForID(passedTaskID);
    }

    public void unfinishTask(View view) {
        if(SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        selectedTask.setDone(false);
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateTaskInDB(selectedTask);
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}