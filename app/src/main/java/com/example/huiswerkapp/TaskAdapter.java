package com.example.huiswerkapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(Context context, List<Task> tasks)
        {
                super(context, 0, tasks);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Task task = getItem(position);
                if(convertView == null)
                {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_cell, parent, false);
                }

                TextView title = convertView.findViewById(R.id.cellTitle);
                TextView desc = convertView.findViewById(R.id.cellDesc);
                TextView subject = convertView.findViewById(R.id.cellSubject);

                title.setText(task.getTitle());
                desc.setText(task.getDescription());
                subject.setText(task.getSubject());

                return convertView;
        }
}
