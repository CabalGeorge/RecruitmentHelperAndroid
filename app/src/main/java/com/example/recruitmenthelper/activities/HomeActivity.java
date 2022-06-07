package com.example.recruitmenthelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.recruitmenthelper.R;
import com.example.recruitmenthelper.config.SessionManager;
import com.example.recruitmenthelper.popups.FabPopUp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "session";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        updateNavigationHeader();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.floatingButton);

        if(!sessionManager.getSessionRole().equals("ADMIN")) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            fab.setLayoutParams(p);
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, FabPopUp.class)));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            logout(menuItem);
            return true;
        });

        String role = sessionManager.getSessionRole();
        switch (role) {
            case "ADMIN":
                navigationView.getMenu().findItem(R.id.nav_future_interviews).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_past_interviews).setVisible(false);
                break;
            case "HR_REPRESENTATIVE":
                navigationView.getMenu().findItem(R.id.nav_users).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_interviews).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_reports).setVisible(false);
                break;
            case "TECHNICAL_INTERVIEWER":
                navigationView.getMenu().findItem(R.id.nav_users).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_archived).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_interviews).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_reports).setVisible(false);
                break;
            case "PTE":
                navigationView.getMenu().findItem(R.id.nav_future_interviews).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_past_interviews).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_reports).setVisible(false);
                break;
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_candidates, R.id.nav_users, R.id.nav_archived, R.id.nav_interviews, R.id.nav_future_interviews, R.id.nav_past_interviews, R.id.reports)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        switch (menuItem.getItemId()) {
            case R.id.nav_logout: {
                logout(menuItem);
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout(MenuItem menuItem) {

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.removeSession();
        moveToLogin();
    }

    private void moveToLogin() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void updateNavigationHeader() {

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String name = sessionManager.getSessionUsername();
        String email = sessionManager.getSessionEmail();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navEmail = headerView.findViewById(R.id.nav_email);

        navUsername.setText(name);
        navEmail.setText(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}