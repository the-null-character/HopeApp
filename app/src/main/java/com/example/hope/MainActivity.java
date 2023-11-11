package com.example.hope;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView t;
    private Button logout;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private double latitude;
    private double longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(MainActivity.this,Login.class));


        if(user==null){

            startActivity(new Intent(MainActivity.this,Login.class));

        }
        else {

            Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();


            startActivity(new Intent(MainActivity.this,MapsActivity.class));
          //  startActivity(new Intent(MainActivity.this, Sos.class));

            finish();





            }



        }




    }




