package com.example.huiswerkapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(Context context, List<Task> tasks)
        {
                super(context, 0, tasks);
        }

        @SuppressLint("SimpleDateFormat")
        private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        @SuppressLint("SimpleDateFormat")
        private static final DateFormat dateFormatDeadline = new SimpleDateFormat("dd-MM-yyyy");

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
                TextView ownDeadline = convertView.findViewById(R.id.cellOwnDeadline);
                TextView actualDeadline = convertView.findViewById(R.id.cellActualDeadline);
                TextView timeEstimated = convertView.findViewById(R.id.cellEstimatedTime);
                TextView dateDone = convertView.findViewById(R.id.cellDateFinished);
                ImageView greenCheck = convertView.findViewById(R.id.greenCheck);

                title.setText(task.getTitle());
                desc.setText(task.getDescription());
                if(task.getSubject().equals("")) {
                        subject.setText("-");
                } else {
                        subject.setText(task.getSubject());
                }
                ownDeadline.setText(dateFormatDeadline.format(task.getOwnDeadline()));
                actualDeadline.setText(dateFormatDeadline.format(task.getActualDeadline()));
                if(task.isDone()) {
                        dateDone.setText(dateFormat.format(task.getDateDone()));
                        timeEstimated.setText("");
                        greenCheck.setImageResource(R.drawable.ic_baseline_check_24_green);
                } else {
                        dateDone.setText("");
                        timeEstimated.setText(convertTimeEstimated(task));
                }

                return convertView;
        }

        public String convertTimeEstimated(Task task) {
                if(task.getTimeEstimated().isEmpty()) return "";

                String converted;
                int timeEstimated = Integer.parseInt(task.getTimeEstimated());
                int convertedTimeHours = (timeEstimated - (timeEstimated % 60)) / 60;
                int convertedTimeMinutes = timeEstimated % 60;

                if(convertedTimeMinutes == 0 & convertedTimeHours == 0) {
                        converted = "";
                        return converted;
                } else if(convertedTimeMinutes == 0) {
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
