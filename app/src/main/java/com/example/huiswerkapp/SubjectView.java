package com.example.huiswerkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

public class SubjectView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    private ListView subjectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);
        initwidgets();
        loadFromDBToMemory();
        noSubjectsAdded();
        setSubjectAdapter();
        setOnClickListener();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Vakken");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_seeSubjects);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_seeTasks:
                Intent newTaskIntent1 = new Intent(this, MainActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(newTaskIntent1);
                break;
            case R.id.nav_seeFinishedTasks:
                Intent newTaskIntent2 = new Intent(this, FinishedTasks.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(newTaskIntent2);
                break;
            case R.id.nav_addTask:
                if(noSubjectsAdded()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle("Geen vakken!")
                            .setMessage("Er zijn nog geen vakken toegevoegd! Voeg eerst een vak toe, of ga verder zonder.")
                            .setPositiveButton("Vak toevoegen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), SubjectDetailActivity.class);
                                    drawer.closeDrawer(GravityCompat.START);
                                    startActivity(intent);
                                }
                            })
                            .setNeutralButton("Ga verder", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                                    drawer.closeDrawer(GravityCompat.START);
                                    startActivity(intent);
                                }
                            })
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .create();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Button positiveButton = ((AlertDialog) dialogInterface)
                                    .getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setBackgroundColor(getResources().getColor(R.color.button_medium_green));
                            positiveButton.setTextColor(getResources().getColor(R.color.darkGray));
                            Button neutralButton = ((AlertDialog) dialogInterface)
                                    .getButton(AlertDialog.BUTTON_NEUTRAL);
                            neutralButton.setBackgroundColor(getResources().getColor(R.color.button_medium_yellow));
                            neutralButton.setTextColor(getResources().getColor(R.color.darkGray));
                        }
                    });

                    alertDialog.show();
                } else {
                    Intent newTaskIntent3 = new Intent(this, TaskDetailActivity.class);
                    drawer.closeDrawer(GravityCompat.START);
                    startActivity(newTaskIntent3);
                }
                break;
            case R.id.nav_addSubject:
                Intent newTaskIntent4 = new Intent(this, SubjectDetailActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(newTaskIntent4);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initwidgets() {
        subjectListView = findViewById(R.id.subjectListView);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateSubjectListArray();
    }

    public boolean noSubjectsAdded() {
        return Subject.nonDeletedSubjects().isEmpty();
    }

    private void setSubjectAdapter() {
        SubjectAdapter subjectAdapter = new SubjectAdapter(getApplicationContext(), Subject.nonDeletedSubjects());
        subjectListView.setAdapter(subjectAdapter);
    }

    private void setOnClickListener() {
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Subject selectedSubject = (Subject) subjectListView.getItemAtPosition(position);
                Intent editSubjectIntent = new Intent(getApplicationContext(), SubjectDetailActivity.class);
                editSubjectIntent.putExtra(Subject.SUBJECT_EDIT_EXTRA, selectedSubject.getId());
                startActivity(editSubjectIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        super.onResume();
        setSubjectAdapter();
        navigationView.setCheckedItem(R.id.nav_seeSubjects);
    }
}