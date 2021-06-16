package com.planner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class NewTaskActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "NewTaskActivity";
    private Calendar calendar = Calendar.getInstance();

    private boolean inputIsCorrect(EditText title, EditText reward, EditText desc, TextView date, TextView time) {
        boolean flag = true;
        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError("You did not enter a title");
            flag = false;
        }

        if (TextUtils.isEmpty(desc.getText().toString())) {
            desc.setError("You did not enter a description");
            flag = false;
        }

        if (TextUtils.isEmpty(date.getText().toString())) {
            date.setError("You did not enter a deadline date");
            flag = false;
        }

        if (TextUtils.isEmpty(time.getText().toString())) {
            time.setError("You did not enter a deadline time");
            flag = false;
        }

        if (TextUtils.isEmpty(reward.getText().toString())) {
            reward.setError("You did not enter a reward");
            flag = false;
        } else if (Integer.parseInt(reward.getText().toString()) < 20) {
            reward.setError("Cost is too small");
            flag = false;
        }


        return flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        ImageView imageBack = findViewById(R.id.imageBackTask);
        TextView dateDeadline = findViewById(R.id.deadlineTextTask);
        TextView timeDeadline = findViewById(R.id.timeTextTask);

        imageBack.setOnClickListener(v -> onBackPressed());
        dateDeadline.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment(calendar);
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        timeDeadline.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment(calendar);
            timePicker.show(getSupportFragmentManager(), "time picker");
        });

        ImageView saveButton = findViewById(R.id.imageSaveTask);
        saveButton.setOnClickListener(v -> {
            EditText title = findViewById(R.id.inputTitleTask);
            EditText reward = findViewById(R.id.rewardTextTask);
            EditText description = findViewById(R.id.inputDescTask);

            if (!inputIsCorrect(title, reward, description,
                    dateDeadline, timeDeadline)) {
                return;
            }


            DatabaseReference taskRef = PlannerCostants.databaseReference.child("tasks").push();

            Task task = new Task(title.getText().toString(),
                    dateDeadline.getText().toString(),
                    timeDeadline.getText().toString(),
                    Integer.parseInt(reward.getText().toString()),
                    description.getText().toString(), taskRef.getKey());
            taskRef.setValue(task);

            String currentUserID = PlannerCostants.mAuth.getCurrentUser().getUid();
            PlannerCostants.databaseReference.child("users").child(currentUserID).child("taskIDs").push().setValue(taskRef.getKey());

            finish();
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textView = findViewById(R.id.deadlineTextTask);
        textView.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hours, int minutes) {
        TextView textView = findViewById(R.id.timeTextTask);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        textView.setText(hours + ":" + minutes);
    }
}