package com.example.hope;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
   Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private double latitude;
    private double longitude;
    private Button logout;
    private ConstraintLayout medicalbutton;
    boolean t = false;
    private Button dismiss;
    private ImageButton sos;
    boolean hasCameraFlash = false;
    boolean flashOn = false;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    NavigationView navigationView;




    private ConstraintLayout fireb;
    private ConstraintLayout policeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        drawerLayout=findViewById(R.id.drawerLayout);
        dismiss = findViewById(R.id.buttoncancel);
        fireb = findViewById(R.id.firebutton);
        policeb = findViewById(R.id.policebutton);
        toolbar= findViewById(R.id.ActionBar);

        navigationView=findViewById(R.id.Main_NavView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);






//        logout=findViewById(R.id.logout);

        sos = findViewById(R.id.SOSbut);



        medicalbutton = findViewById(R.id.medicalbuton);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation(new LatLng(0, 0));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findViewById(R.id.refreshlocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLocation(new LatLng(0, 0));
            }
        });
        fireb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                medicalbutton.setVisibility(View.INVISIBLE);
                fireb.setVisibility(View.INVISIBLE);
                policeb.setVisibility(View.INVISIBLE);


                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup, null);

                // create the popup window
                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = false; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, 900, 1000, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


                // dismiss the popup window when touched


                LayoutInflater inflater1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView1 = inflater1.inflate(R.layout.confirmation_popup, null);
                final PopupWindow popupWindow1 = new PopupWindow(popupView1, 900, 1000, false);

//

                popupWindow.getContentView().findViewById(R.id.buttoncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        medicalbutton.setVisibility(View.VISIBLE);
                        fireb.setVisibility(View.VISIBLE);
                        policeb.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();


                    }
                });


                popupWindow.getContentView().findViewById(R.id.buttonconfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        popupWindow.dismiss();

                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);


                        TextView t = popupWindow1.getContentView().findViewById(R.id.namea);
                        TextView t2 = popupWindow1.getContentView().findViewById(R.id.phonea);
                        t.setTextSize(18);
                        String id = "1";


                        DocumentReference result = db.collection("fire_stations").document(id);
                        result.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String firename, fire_no;
                                DocumentSnapshot reference;
                                Log.d("tagisthis", "message");
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    reference = task.getResult();
                                    if (document.exists()) {

                                        firename = Objects.requireNonNull(document.get("name")).toString();
                                        fire_no = (String) document.get("number");


                                        t.setText(firename);
                                        t2.setText(fire_no);


                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });

                        popupWindow1.getContentView().findViewById(R.id.buttondismiss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow1.dismiss();

                                medicalbutton.setVisibility(View.VISIBLE);
                                fireb.setVisibility(View.VISIBLE);
                                policeb.setVisibility(View.VISIBLE);
                            }
                        });


                    }
                });

            }
        });


//
//

        sos.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(hasCameraFlash){
                    if(flashOn){
                        flashOn = false;
                        //sos.setImageResource(R.drawable.refresh_icon);

                        try {
                            flashLightOff();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    else{
                        flashOn = true;
                        //sos.setImageResource(R.drawable.call_icon);
                        try {
                            flashLightOn();
                        } catch (CameraAccessException | InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

                else{
                    Toast.makeText(MapsActivity.this, "no Flash!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        policeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                medicalbutton.setVisibility(View.INVISIBLE);
                fireb.setVisibility(View.INVISIBLE);
                policeb.setVisibility(View.INVISIBLE);


                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup, null);

                // create the popup window
                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = false; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, 900, 1000, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


                // dismiss the popup window when touched


                LayoutInflater inflater1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView1 = inflater1.inflate(R.layout.confirmation_popup, null);
                final PopupWindow popupWindow1 = new PopupWindow(popupView1, 900, 1000, false);

//

                popupWindow.getContentView().findViewById(R.id.buttoncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        medicalbutton.setVisibility(View.VISIBLE);
                        fireb.setVisibility(View.VISIBLE);
                        policeb.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();


                    }
                });


                popupWindow.getContentView().findViewById(R.id.buttonconfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        popupWindow.dismiss();

                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);


                        TextView t = popupWindow1.getContentView().findViewById(R.id.namea);
                        TextView t2 = popupWindow1.getContentView().findViewById(R.id.phonea);
                        t.setTextSize(18);
                        String id = "1";
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        DocumentReference result = db.collection("police_stations").document(id);
                        result.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String policename, police_no;
                                DocumentSnapshot reference;
                                Log.d("tagisthis", "message");
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    reference = task.getResult();
                                    if (document.exists()) {

                                        policename = Objects.requireNonNull(document.get("name")).toString();
                                        police_no = (String) document.get("number");


                                        t.setText(policename);
                                        t2.setText(police_no);


                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });



                        popupWindow1.getContentView().findViewById(R.id.buttondismiss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow1.dismiss();

                                medicalbutton.setVisibility(View.VISIBLE);
                                fireb.setVisibility(View.VISIBLE);
                                policeb.setVisibility(View.VISIBLE);
                            }
                        });


                    }
                });

            }
        });


        medicalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                medicalbutton.setVisibility(View.INVISIBLE);
                fireb.setVisibility(View.INVISIBLE);
                policeb.setVisibility(View.INVISIBLE);


                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup, null);

                // create the popup window
                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = false; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, 900, 1000, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


                // dismiss the popup window when touched


                LayoutInflater inflater1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView1 = inflater1.inflate(R.layout.confirmation_popup, null);
                final PopupWindow popupWindow1 = new PopupWindow(popupView1, 900, 1000, false);

//

                popupWindow.getContentView().findViewById(R.id.buttoncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        medicalbutton.setVisibility(View.VISIBLE);
                        fireb.setVisibility(View.VISIBLE);
                        policeb.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();


                    }
                });


                popupWindow.getContentView().findViewById(R.id.buttonconfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        popupWindow.dismiss();

                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
//                        if(m!=""){
//                            m="jiii";
//                        }


                        TextView t = popupWindow1.getContentView().findViewById(R.id.namea);
                        t.setTextSize(18);
                        TextView t2 = popupWindow1.getContentView().findViewById(R.id.phonea);
                        String id = "1";
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        DocumentReference result = db.collection("ambulances").document(id);
                        result.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String driver, driver_no;
                                DocumentSnapshot reference;
                                Log.d("tagisthis", "message");
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();

                                    if (document.exists()) {

                                        driver = Objects.requireNonNull(document.get("driver_name")).toString();
                                        driver_no = (String) document.get("driver_number");


                                        t.setText(driver);
                                        t2.setText(driver_no);


                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });


                        popupWindow1.getContentView().findViewById(R.id.buttondismiss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow1.dismiss();


                                medicalbutton.setVisibility(View.VISIBLE);
                                fireb.setVisibility(View.VISIBLE);
                                policeb.setVisibility(View.VISIBLE);
                            }
                        });


                    }
                });
//                        popupView.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View v, MotionEvent event) {
//                                popupWindow.dismiss();
//                                return true;
//                            }
//                        });

            }
        });

        findViewById(R.id.emercontactbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
                    return;
                }

               String number="";
                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            String number;

                            DocumentSnapshot doc = task.getResult();
                            number = doc.get("emergency_contact").toString();
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:"+number));
                            startActivity(i);

                        }
                        else{
                            Toast.makeText(MapsActivity.this, "No emergency contact", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






            }
        });

    }

    private void fetchLocation(LatLng l) {
        if (l.latitude == 0 && l.longitude == 0) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            ((Task) task).addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        latitude = currentLocation.getLatitude();
                        longitude = currentLocation.getLongitude();

                        Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync(MapsActivity.this::onMapReady);
                    }
                }
            });
        } else {

            longitude = l.latitude;
            longitude = l.longitude;

            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync(MapsActivity.this::onMapReady);

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


//        latitude=currentLocation.getLatitude();
//        longitude=currentLocation.getLongitude();
        if (latitude != 0 && longitude != 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Location");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f));
            googleMap.addMarker(markerOptions);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOn() throws CameraAccessException, InterruptedException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraID = cameraManager.getCameraIdList()[0];

        int fltime = 1000;

        for(int i = 0; i<3; i++)
        {
            for(int j = 0; i<9; i++)
            {
                if(j>2 && j<6)
                    fltime = 3000;

                cameraManager.setTorchMode(cameraID, true);
                Thread.sleep(fltime);
                cameraManager.setTorchMode(cameraID, false);
                Thread.sleep(fltime);
            }


        }



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraID = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraID, false);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
                case R.id.nav_btnHome:
//                    Toast.makeText(MapsActivity.this, "home button", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_btnAbout:
                    startActivity(new Intent(MapsActivity.this,About.class));
                    break;
            case R.id.nav_btnLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapsActivity.this,Login.class));
                finish();
                break;
            case R.id.nav_btnForgotpass:
                startActivity(new Intent(MapsActivity.this,resetpassword.class));
                finish();

        }
        return true;
    }
}
