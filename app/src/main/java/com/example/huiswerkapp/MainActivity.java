package com.example.huiswerkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initwidgets();
        loadFromDBToMemory();
        setTaskAdapter();
        setOnClickListener();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Opdrachten");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_seeTasks);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_seeFinishedTasks:

                break;
            case R.id.nav_addTask:
                Intent newTaskIntent2 = new Intent(this, TaskDetailActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(newTaskIntent2);
                break;
            case R.id.nav_seeSubjects:
                Intent newTaskIntent3 = new Intent(this, SubjectView.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(newTaskIntent3);
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
        taskListView = findViewById(R.id.taskListView);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateTaskListArray();
        sqLiteManager.populateSubjectListArray();
    }

    private void setTaskAdapter() {
        TaskAdapter taskAdapter = new TaskAdapter(getApplicationContext(), Task.nonDeletedTasks());
        taskListView.setAdapter(taskAdapter);
    }

    private void setOnClickListener() {
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Task selectedTask = (Task) taskListView.getItemAtPosition(position);
                Intent editTaskIntent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                editTaskIntent.putExtra(Task.TASK_EDIT_EXTRA, selectedTask.getId());
                startActivity(editTaskIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        super.onResume();
        setTaskAdapter();
        navigationView.setCheckedItem(R.id.nav_seeTasks);
    }
}