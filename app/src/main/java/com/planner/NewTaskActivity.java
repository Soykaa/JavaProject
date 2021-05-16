package com.planner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        ImageView imageBack = findViewById(R.id.imageBackTask);
        TextView dateDeadline = findViewById(R.id.deadlineTextTask);
        TextView timeDeadline = findViewById(R.id.timeTextTask);

        imageBack.setOnClickListener(v -> onBackPressed());

        dateDeadline.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        timeDeadline.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        });

        ImageView saveButton = findViewById(R.id.imageSaveTask);
        saveButton.setOnClickListener(v -> {
            EditText title = findViewById(R.id.inputTitleTask);
            EditText cost = findViewById(R.id.priceTextTask);
            EditText description = findViewById(R.id.inputDescTask);
            Task task = new Task(title.getText().toString(),
                                 cost.getText().toString(),
                                 description.getText().toString());
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference taskRef = database.child("tasks").push();
            taskRef.setValue(task);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String currentUserID = mAuth.getCurrentUser().getUid();
            database.child("users").child(currentUserID).child("taskIDs").push().setValue(taskRef.getKey());

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
        TextView textView = findViewById(R.id.deadlineTextTask);
        textView.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hours, int minutes) {
        TextView textView = findViewById(R.id.timeTextTask);
        textView.setText(hours +":" + minutes);
    }
}