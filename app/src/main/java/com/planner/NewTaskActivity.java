package com.planner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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

        String parentId = getIntent().getStringExtra("parentId");
        String adresseId = getIntent().getStringExtra("adresseId");

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
            EditText penalty = findViewById(R.id.penaltyText);
            EditText description = findViewById(R.id.inputDescTask);

            if (!inputIsCorrect(title, reward, description,
                    dateDeadline, timeDeadline)) {
                return;
            }

            String taskParentId = "me";

            if (parentId != null) {
                taskParentId = parentId;
            }

            DatabaseReference taskRef;
            if (parentId == null) {
                taskRef = PlannerCostants.databaseReference.child("tasks").push();
            } else {
                taskRef = PlannerCostants.databaseReference.child("offeredTasks").push();
            }
            String date = dateDeadline.getText().toString();
            String time = timeDeadline.getText().toString();

            Task task = new Task(
                    taskParentId,
                    title.getText().toString(),
                    date,
                    time,
                    Integer.parseInt(reward.getText().toString()),
                    Integer.parseInt(penalty.getText().toString()),
                    description.getText().toString(),
                    taskRef.getKey(),
                    getTimestamp(date, time)
            );
            taskRef.setValue(task);

            if (parentId == null) {
                String currentUserID = PlannerCostants.mAuth.getCurrentUser().getUid();
                PlannerCostants.databaseReference.child("users").child(currentUserID).child("taskIDs").push().setValue(taskRef.getKey());
            } else {
                PlannerCostants.databaseReference.child("users").child(adresseId).child("offeredTaskIDs").push().setValue(taskRef.getKey());
            }

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

    private long getTimestamp(String date, String time) {
        date = date.replace(",", "");
        date = date.replace(";", "");
        String[] s = date.split(" ");
        String year = s[3];
        String month = getMonth(s[1]);
        String day = s[2];
        if (day.length() == 1) {
            day = "0" + day;
        }

        String[] hs = time.split(":");
        String hh = hs[0];
        String mm = hs[1];
        if (mm.length() == 1) {
            mm = "0" + mm;
        }
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateTime = year + "-" + month + "-" + day + " " + hh + ":" + mm + ":59";
        Date dt = new Date();
        try {
            dt = dateTimeFormatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt.getTime();
    }

    private String getMonth(String month) {
        switch (month) {
            case "January":
                return "01";
            case "February":
                return "02";
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "August":
                return "08";
            case "September":
                return "09";
            case "October":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
        }
        return "";
    }

}