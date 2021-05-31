package com.planner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class SetTaskCompletedActivity extends AppCompatActivity {
    private String title;
    private String desc;
    private int reward;
    private int pos;
    private Uri fileImageUri;
    private ImageView imageFile;
    private boolean activityRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_task_completed);
        ImageView imageSetTaskCompleted = findViewById(R.id.imageChangeStatusTask);
        ImageView imageBack = findViewById(R.id.imageBackSetTaskCompleted);
        imageFile = findViewById(R.id.imageFile);
        Button chooseImageButton = findViewById(R.id.chooseFileButton);

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        reward = getIntent().getIntExtra("reward", 0);
        pos = getIntent().getIntExtra("pos", 0);

        TextView taskTitle = findViewById(R.id.titleTask);
        taskTitle.setText(title);

        TextView taskDesc = findViewById(R.id.descTask);
        taskDesc.setText(desc);

        TextView taskReward = findViewById(R.id.rewardTask);
        taskReward.setText("reward: " + reward);

        chooseImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, RequestCodes.REQUEST_CODE_ADD_IMAGE);
        });


        imageBack.setOnClickListener(v -> onBackPressed());
        imageSetTaskCompleted.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("isOk", true);
            intent.putExtra("title", title);
            intent.putExtra("desc", desc);
            intent.putExtra("reward", reward);
            intent.putExtra("pos", pos);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RequestCodes.REQUEST_CODE_ADD_IMAGE) {
            fileImageUri = data.getData();
            Picasso.get().load(fileImageUri).into(imageFile);
            imageFile.setImageURI(fileImageUri);
        }
    }
}