package com.planner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RequestDialog extends AppCompatDialogFragment {
    private static final String TAG = "Request Dialog";
    private EditText editTextUsername;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_request, null);
        editTextUsername = view.findViewById(R.id.edit_username);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        AlertDialog dialog = builder.setView(view)
                .setTitle("Type in user name")
                .setNegativeButton("Cancel", (dialog1, which) -> {
                })
                .setPositiveButton("Send request", null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            String requestName = editTextUsername.getText().toString();
            if (!requestName.equals(currentUser.getDisplayName())) {
                PlannerCostants.userRef.orderByChild("name").equalTo(requestName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String userId = "";
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                Log.i(TAG, "get user id " + datas.getKey());
                                userId = datas.getKey();
                            }
                            String finalUserId = userId;
                            PlannerCostants.userRef.child(currentUser.getUid()).child("friends").child(finalUserId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        //Username exists and isn't already a friend
                                        Log.i(TAG, "send request to " + finalUserId);
                                        String userId = currentUser.getUid();
                                        PlannerCostants.userRef.child(finalUserId).child("requests").child(userId).setValue(true);
                                        dialog.dismiss();

                                    } else {
                                        Snackbar.make(view, "Already a friend", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Log.i(TAG, "no such user");
                            //Username does not exist
                            Snackbar.make(view, "No user with such name", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                Snackbar.make(view, "Cannot send request to yourself ", Snackbar.LENGTH_LONG).show();
            }
        });
        return dialog;
    }
}
