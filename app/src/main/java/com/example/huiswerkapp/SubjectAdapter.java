package com.example.huiswerkapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SubjectAdapter extends ArrayAdapter<Subject> {
        public SubjectAdapter(Context context, List<Subject> subjects)
        {
                super(context, 0, subjects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Subject subject = getItem(position);
                if(convertView == null)
                {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_cell, parent, false);
                }

                TextView name = convertView.findViewById(R.id.cellName);

                name.setText(subject.getName());

                return convertView;
        }
}
