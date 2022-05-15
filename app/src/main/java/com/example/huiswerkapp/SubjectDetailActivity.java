package com.example.huiswerkapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.navigation.NavigationView;

import java.util.Date;

public class SubjectDetailActivity extends AppCompatActivity {

    private EditText nameEditText;
    private TextView nameEditTextError;
    private Button deleteButton;
    private Subject selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        initWidgets();
        checkForEditSubject();
        setErrorResets();

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (selectedSubject != null) {
            toolbar.setTitle("Vak Aanpassen");
        } else {
            toolbar.setTitle("Vak Toevoegen");
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
        nameEditText = findViewById(R.id.nameEditText);
        nameEditTextError = findViewById(R.id.nameEditTextError);
        deleteButton = findViewById(R.id.deleteSubjectButton);
    }

    private void checkForEditSubject() {
        Intent previousIntent = getIntent();

        int passedSubjectID = previousIntent.getIntExtra(Subject.SUBJECT_EDIT_EXTRA, -1);
        selectedSubject = Subject.getSubjectForID(passedSubjectID);

        if(selectedSubject != null) {
            nameEditText.setText(selectedSubject.getName());
        }
        else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveSubject(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String name = String.valueOf(nameEditText.getText());

        if(name.equals("")) {
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.red));
            ViewCompat.setBackgroundTintList(nameEditText, colorStateList);
            nameEditTextError.setVisibility(View.VISIBLE);
            nameEditTextError.setText("Vul eerst een naam in!");
            return;
        }

        if(selectedSubject == null) {
            int id = Subject.subjectArrayList.size();
            Subject newSubject = new Subject(id, name);
            Subject.subjectArrayList.add(newSubject);
            sqLiteManager.addSubjectToDatabase(newSubject);
        }
        else {
            selectedSubject.setName(name);
            sqLiteManager.updateSubjectInDB(selectedSubject);
        }
        finish();
        Intent intent = new Intent(this, SubjectView.class);
        startActivity(intent);
    }

    public void deleteSubject(View view) {
        selectedSubject.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateSubjectInDB(selectedSubject);
        finish();
    }

    public void setErrorResets() {
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
                ViewCompat.setBackgroundTintList(nameEditText, colorStateList);
                nameEditTextError.setVisibility(View.GONE);
            }
        });
    }

    public void resetNameError(View view) {
        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.darkGray));
        ViewCompat.setBackgroundTintList(nameEditText, colorStateList);
        nameEditTextError.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        super.onResume();
    }
}