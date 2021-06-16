package com.planner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlannerCostants {
    public static FirebaseAuth mAuth;
    public static DatabaseReference userRef, databaseReference;

    static {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userRef = databaseReference.child("users");
    }

}
