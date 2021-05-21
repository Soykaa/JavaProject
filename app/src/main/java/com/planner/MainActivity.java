package com.planner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    protected DrawerLayout drawer;
    private TextView name;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WelcomeScreen()).commit();
            navigationView.setCheckedItem(R.id.nav_getting_started);
        }

        name = navigationView.getHeaderView(0).findViewById(R.id.profile_name);
        image = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            if (name != null) {
                name.setText(signInAccount.getDisplayName());
            }
            if (image != null) {
                Uri profilePhotoUrl = signInAccount.getPhotoUrl();
                if (profilePhotoUrl != null) {
                    String imageStr = profilePhotoUrl.toString();
                    Picasso.get()
                            .load(imageStr)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(image);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_getting_started:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WelcomeScreen()).commit();
                break;
            case R.id.nav_sign_out:
                logOut();
                break;
            case R.id.nav_new_task:
                createTask();
                break;
            case R.id.nav_all_tasks:
                viewAllTasks();
                break;
            case R.id.nav_all_wishes:
                viewAllWishes();
                break;
            case R.id.nav_friends:
                friends();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void viewAllTasks() {
        Intent intent = new Intent(this, ViewTasksActivity.class);
        startActivity(intent);
    }

    private void viewAllWishes() {
        Intent intent = new Intent(this, WishActivity.class);
        startActivity(intent);
    }

    private void friends() {
        Intent intent = new Intent(this, FriendActivity.class);
        startActivity(intent);
    }

    private void createTask() {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }
}