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

// New interface for Emergency Service Adapter
interface EmergencyServiceAdapter {
    void showEmergencyServicePopup(String serviceType, View view);
}

// Concrete implementation of EmergencyServiceAdapter
class EmergencyServiceAdapterImpl implements EmergencyServiceAdapter {
    private FirebaseFirestore db;

    public EmergencyServiceAdapterImpl() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void showEmergencyServicePopup(String serviceType, View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, 900, 1000, focusable);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView1 = inflater1.inflate(R.layout.confirmation_popup, null);
        final PopupWindow popupWindow1 = new PopupWindow(popupView1, 900, 1000, false);

        popupWindow.getContentView().findViewById(R.id.buttoncancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your existing logic
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

                popupWindow1.getContentView().findViewById(R.id.buttondismiss)
                        .setOnClickListener(new View.OnClickListener() {
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
}

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private EmergencyServiceAdapter emergencyServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize the EmergencyServiceAdapter
        emergencyServiceAdapter = new EmergencyServiceAdapterImpl();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (latitude != 0 && longitude != 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Location");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f));
            googleMap.addMarker(markerOptions);
        }

        // Example of using the adapter to show the fire emergency service popup
        emergencyServiceAdapter.showEmergencyServicePopup("fire", findViewById(R.id.map));
    }

    //click listeners to use the adapter

    fireb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            emergencyServiceAdapter.showEmergencyServicePopup("fire", view);
        }
    });

    policeb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            emergencyServiceAdapter.showEmergencyServicePopup("police", view);
        }
    });

    medicalbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            emergencyServiceAdapter.showEmergencyServicePopup("medical", view);
        }
    });

}
