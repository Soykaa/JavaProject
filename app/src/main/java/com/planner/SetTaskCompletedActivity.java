package com.planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SetTaskCompletedActivity extends AppCompatActivity {
    private String title;
    private String desc;
    private int reward;
    private boolean activityRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_task_completed);
        ImageView imageSetTaskCompleted = findViewById(R.id.imageChangeStatusTask);
        ImageView imageBack = findViewById(R.id.imageBackSetTaskCompleted);

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        reward = getIntent().getIntExtra("reward", 0);

        imageBack.setOnClickListener(v -> onBackPressed());
        imageSetTaskCompleted.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("isOk", true);
            intent.putExtra("title", title);
            intent.putExtra("desc", desc);
            intent.putExtra("reward", reward);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}