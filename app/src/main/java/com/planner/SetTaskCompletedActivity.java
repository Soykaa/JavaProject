package com.planner;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class SetTaskCompletedActivity extends AppCompatActivity {
    private String title;
    private String desc;
    private int reward;
    private int pos;
    private Uri fileImageUri;
    private ImageView imageFile;
    private String uploadId = null;
    private StorageReference storageReference;
    private DatabaseReference databaseSReference;
    private static final String TAG = "SetTaskCompleted";

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


        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseSReference = FirebaseDatabase.getInstance().getReference("uploads");

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
            addRewardPoints(reward);
            uploadFile(intent);
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(Intent intent) {
        if (fileImageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(fileImageUri));
            fileReference.putFile(fileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SetTaskCompletedActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UploadFile upload = new UploadFile("picture", uri.toString());
                            Log.d(TAG, "uploadFile " + uri.toString());
                            uploadId = databaseSReference.push().getKey();
                            databaseSReference.child(uploadId).setValue(upload);
                            intent.putExtra("uploadId", uploadId);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(SetTaskCompletedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, intent);
            finish();
        }
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

    private void addRewardPoints(int reward) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userID)
                .child("points");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Long points = snapshot.getValue(Long.class);
                databaseRef.setValue(points + reward);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}