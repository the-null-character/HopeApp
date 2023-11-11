package com.example.hope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*--------------VARIABLES--------------*/
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);

        /*----------HOOKS----------*/
        drawerLayout = findViewById(R.id.About_drawerLayout);
        navigationView = findViewById(R.id.About_NavView);
        toolbar = findViewById(R.id.ActionBar);

        /*----------ToolBar----------*/
        setSupportActionBar(toolbar);

        /*----------Navigation View----------*/
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_btnHome:
                startActivity(new Intent(About.this,MapsActivity.class));
                finish();
                break;
            case R.id.nav_btnAbout:
                break;
            case R.id.nav_btnForgotpass:
                startActivity(new Intent(About.this,resetpassword.class));
                break;
            case R.id.nav_btnLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(About.this,Login.class));
                break;
        }
        return true;
    }
}