package com.planner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        ImageView imageBack = findViewById(R.id.imageBack);
        TextView dateDeadline = findViewById(R.id.deadlineText);
        TextView timeDeadline = findViewById(R.id.timeText);

        imageBack.setOnClickListener(v -> onBackPressed());

        dateDeadline.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        timeDeadline.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        });

        ImageView saveButton = findViewById(R.id.imageSave);
        saveButton.setOnClickListener(v -> {
            EditText title = findViewById(R.id.inputTaskTitle);
            EditText cost = findViewById(R.id.priceText);
            EditText description = findViewById(R.id.inputTaskDesc);
            Task task = new Task(title.getText().toString(),
                                 cost.getText().toString(),
                                 description.getText().toString());
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child("tasks").push().setValue(task);
            finish();
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.deadlineText);
        textView.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hours, int minutes) {
        TextView textView = (TextView) findViewById(R.id.timeText);
        textView.setText(hours +":" + minutes);
    }
}