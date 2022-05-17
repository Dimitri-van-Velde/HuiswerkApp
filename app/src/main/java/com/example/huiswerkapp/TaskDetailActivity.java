package com.example.huiswerkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText titleEditText, descEditText, timeEstimatedText;
    private TextView titleEditTextError, descEditTextError, timeEstimatedTextError;
    private Button deleteButton, finishButton;
    private Spinner selectSubject;
    private DatePicker ownDeadline, actualDeadline;
    private Task selectedTask;
    private ScrollView scrollView;

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormatYear = new SimpleDateFormat("yyyy");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormatMonth = new SimpleDateFormat("MM");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormatDay = new SimpleDateFormat("dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        initWidgets();
        populateSpinner();
        checkForEditTask();
        setErrorResets();

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
        titleEditTextError = findViewById(R.id.titleEditTextError);
        descEditText = findViewById(R.id.descriptionEditText);
        descEditTextError = findViewById(R.id.descriptionEditTextError);
        deleteButton = findViewById(R.id.deleteTaskButton);
        finishButton = findViewById(R.id.finishedTaskButton);
        selectSubject = findViewById(R.id.selectSubject);
        ownDeadline = findViewById(R.id.datePickerOwnDeadline);
        actualDeadline = findViewById(R.id.datePickerActualDeadline);
        timeEstimatedText = findViewById(R.id.timeEstimatedEditText);
        timeEstimatedTextError = findViewById(R.id.timeEstimatedEditTextError);
        scrollView = findViewById(R.id.taskDetailScroll);
    }

    private void populateSpinner() {
        List<String> spinner = Subject.nonDeletedSubjectsString();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, spinner);

        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        selectSubject.setAdapter(adapter);
    }

    private void checkForEditTask() {
        Intent previousIntent = getIntent();

        int passedTaskID = previousIntent.getIntExtra(Task.TASK_EDIT_EXTRA, -1);
        selectedTask = Task.getTaskForID(passedTaskID);

        if(selectedTask != null) {
            titleEditText.setText(selectedTask.getTitle());
            descEditText.setText(selectedTask.getDescription());
            selectSubject.setSelection(getIndex(selectSubject, selectedTask.getSubject()));
            ownDeadline.updateDate(Integer.parseInt(dateFormatYear.format(new Date(selectedTask.getOwnDeadline().getTime()))),
                    Integer.parseInt(dateFormatMonth.format(new Date(selectedTask.getOwnDeadline().getTime()))) - 1,
                    Integer.parseInt(dateFormatDay.format(new Date(selectedTask.getOwnDeadline().getTime()))));
            actualDeadline.updateDate(Integer.parseInt(dateFormatYear.format(new Date(selectedTask.getActualDeadline().getTime()))),
                    Integer.parseInt(dateFormatMonth.format(new Date(selectedTask.getActualDeadline().getTime()))) - 1,
                    Integer.parseInt(dateFormatDay.format(new Date(selectedTask.getActualDeadline().getTime()))));
            timeEstimatedText.setText(selectedTask.getTimeEstimated());
        }
        else {
            deleteButton.setVisibility(View.INVISIBLE);
            finishButton.setVisibility(View.INVISIBLE);
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    public void saveTask(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());
        String subject = String.valueOf(selectSubject.getSelectedItem().toString());
        Date ownDead = pickerToDate(ownDeadline);
        Date actDead = pickerToDate(actualDeadline);
        String estTime = String.valueOf(timeEstimatedText.getText());
        Date dateDone = new Date();

        boolean anyErrors = false;

        if(title.equals("")) {
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
            ViewCompat.setBackgroundTintList(titleEditText, colorStateList);
            titleEditTextError.setVisibility(View.VISIBLE);
            titleEditTextError.setText("Vul eerst een titel in!");
            scrollView.scrollTo(0, 0);
            anyErrors = true;
        }

        if(desc.equals("")) {
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
            ViewCompat.setBackgroundTintList(descEditText, colorStateList);
            descEditTextError.setVisibility(View.VISIBLE);
            descEditTextError.setText("Vul eerst een beschrijving in!");
            scrollView.scrollTo(0, 0);
            anyErrors = true;
        }

        boolean estTimeHasText = Pattern.matches("[^0-9]", estTime);

        if(estTimeHasText) {
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
            ViewCompat.setBackgroundTintList(timeEstimatedText, colorStateList);
            timeEstimatedTextError.setVisibility(View.VISIBLE);
            timeEstimatedTextError.setText("Vul een getal in!");
            anyErrors = true;
        }

        if(anyErrors) return;

        if(selectedTask == null) {
            int id = Task.taskArrayList.size();
            Task newTask = new Task(id, title, desc, subject, ownDead, actDead, estTime, false, dateDone);
            Task.taskArrayList.add(newTask);
            sqLiteManager.addTaskToDatabase(newTask);
        }
        else {
            selectedTask.setTitle(title);
            selectedTask.setDescription(desc);
            selectedTask.setSubject(subject);
            selectedTask.setOwnDeadline(ownDead);
            selectedTask.setActualDeadline(actDead);
            selectedTask.setTimeEstimated(estTime);
            sqLiteManager.updateTaskInDB(selectedTask);
        }
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public Date pickerToDate(DatePicker datePicker) {
        return new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
    }

    public void deleteTask(View view) {
        selectedTask.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateTaskInDB(selectedTask);
        finish();
    }

    public void finishTask(View view) {
        selectedTask.setDone(true);
        selectedTask.setDateDone(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateTaskInDB(selectedTask);
        finish();
        Intent intent = new Intent(this, FinishedTasks.class);
        startActivity(intent);
    }

    public void setErrorResets() {
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
                ViewCompat.setBackgroundTintList(titleEditText, colorStateList);
                titleEditTextError.setVisibility(View.GONE);
            }
        });

        descEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
                ViewCompat.setBackgroundTintList(descEditText, colorStateList);
                descEditTextError.setVisibility(View.GONE);
            }
        });

        timeEstimatedText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
                ViewCompat.setBackgroundTintList(timeEstimatedText, colorStateList);
                timeEstimatedTextError.setVisibility(View.GONE);
            }
        });
    }

    public void resetTitleError(View view) {
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
        ViewCompat.setBackgroundTintList(titleEditText, colorStateList);
        titleEditTextError.setVisibility(View.GONE);
    }

    public void resetDescriptionError(View view) {
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
        ViewCompat.setBackgroundTintList(descEditText, colorStateList);
        descEditTextError.setVisibility(View.GONE);
    }

    public void resetTimeEstimatedError(View view) {
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
        ViewCompat.setBackgroundTintList(timeEstimatedText, colorStateList);
        timeEstimatedTextError.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        super.onResume();
    }
}