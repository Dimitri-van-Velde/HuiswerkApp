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
                TextView timeEstimated = convertView.findViewById(R.id.cellEstimatedTime);

                title.setText(task.getTitle());
                desc.setText(task.getDescription());
                subject.setText(task.getSubject());
                timeEstimated.setText(convertTimeEstimated(task));

                return convertView;
        }

        public String convertTimeEstimated(Task task) {
                String converted;
                int timeEstimated = Integer.parseInt(task.getTimeEstimated());
                int convertedTimeHours = (timeEstimated - (timeEstimated % 60)) / 60;
                int convertedTimeMinutes = timeEstimated % 60;

                if(convertedTimeMinutes == 0) {
                        converted = "(" + convertedTimeHours + "u)";
                        return converted;
                } else if(convertedTimeHours == 0) {
                        converted = "(" + convertedTimeMinutes + "m)";
                        return converted;
                } else {
                        converted = "(" + convertedTimeHours + "u " + convertedTimeMinutes + "m)";
                        return converted;
                }
        }
}
