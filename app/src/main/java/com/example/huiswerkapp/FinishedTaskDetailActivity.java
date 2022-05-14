package com.example.huiswerkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class FinishedTaskDetailActivity extends AppCompatActivity {

    private Button unfinishButton;
    private Task selectedTask;

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

//        if(selectedTask != null) {
//            titleEditText.setText(selectedTask.getTitle());
//            descEditText.setText(selectedTask.getDescription());
//            selectSubject.setSelection(getIndex(selectSubject, selectedTask.getSubject()));
//            ownDeadline.updateDate(Integer.parseInt(dateFormatYear.format(new Date(selectedTask.getOwnDeadline().getTime()))),
//                    Integer.parseInt(dateFormatMonth.format(new Date(selectedTask.getOwnDeadline().getTime()))) - 1,
//                    Integer.parseInt(dateFormatDay.format(new Date(selectedTask.getOwnDeadline().getTime()))));
//            actualDeadline.updateDate(Integer.parseInt(dateFormatYear.format(new Date(selectedTask.getActualDeadline().getTime()))),
//                    Integer.parseInt(dateFormatMonth.format(new Date(selectedTask.getActualDeadline().getTime()))) - 1,
//                    Integer.parseInt(dateFormatDay.format(new Date(selectedTask.getActualDeadline().getTime()))));
//            timeEstimatedText.setText(selectedTask.getTimeEstimated());
//        }
//        else {
//            deleteButton.setVisibility(View.INVISIBLE);
//            finishButton.setVisibility(View.INVISIBLE);
//        }
    }

    public void unfinishTask(View view) {
        selectedTask.setDone(false);
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateTaskInDB(selectedTask);
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}