package com.example.huiswerkapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String name = String.valueOf(nameEditText.getText()).trim();

        final String regex = "\\A\\s*\\z";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher nameMatcher = pattern.matcher(name);

        if(name.equals("") || nameMatcher.find()) {
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
            String oldName = selectedSubject.getName();
            selectedSubject.setName(name);
            sqLiteManager.updateSubjectInDB(selectedSubject, oldName);
        }
        finish();
        Intent intent = new Intent(this, SubjectView.class);
        startActivity(intent);
    }

    public void deleteSubject(View view) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Je staat op het punt om het vak " +
                selectedSubject.getName() + " te verwijderen. \n" +
                "Dit kan niet teruggedraaid worden. \nWeet je het zeker?");
        StyleSpan bold = new StyleSpan(Typeface.BOLD);
        spannableStringBuilder.setSpan(bold, 32, 32 + selectedSubject.getName().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Verwijderen?")
                .setMessage(spannableStringBuilder)
                .setPositiveButton("Ja, verwijderen!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedSubject.setDeleted(new Date());
                        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(getApplicationContext());
                        sqLiteManager.updateSubjectInDB(selectedSubject, null);
                        finish();
                    }
                })
                .setNeutralButton("Nee, toch niet!", null)
                .setIcon(R.drawable.ic_baseline_delete_forever_24)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialogInterface)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setBackgroundColor(getResources().getColor(R.color.button_medium_red));
                positiveButton.setTextColor(getResources().getColor(R.color.darkGray));
                Button neutralButton = ((AlertDialog) dialogInterface)
                        .getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setBackgroundColor(getResources().getColor(R.color.button_medium_yellow));
                neutralButton.setTextColor(getResources().getColor(R.color.darkGray));
            }
        });

        alertDialog.show();
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