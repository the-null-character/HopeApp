package com.example.hope;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Observer interface
interface AmbulanceMessageObserver {
    void update(String message);
}

// Subject class
public class AmbulanceMessage {

    private TextView t;
    private String message, driver, driver_no;
    private DocumentSnapshot reference;
    private List<AmbulanceMessageObserver> observers = new ArrayList<>();

    public AmbulanceMessage(TextView t1) {
        this.t = t1;
    }

    // Method to register observers
    public void addObserver(AmbulanceMessageObserver observer) {
        observers.add(observer);
    }

    // Method to remove observers
    public void removeObserver(AmbulanceMessageObserver observer) {
        observers.remove(observer);
    }

    // Method to notify observers
    private void notifyObservers() {
        for (AmbulanceMessageObserver observer : observers) {
            observer.update(message);
        }
    }

    public void displayMessage(String Driver, String DriverNm) {
        message = Driver + " is booked and the number is" + DriverNm;
        notifyObservers();
    }

    public String message() {
        return message;
    }

    public void getMes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> ids = new ArrayList<>();

        DataVariables m = new DataVariables();
        db.collection("ambulances")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ids.add(document.getId());
                            }
                        } else {
                            Log.w("error", "Error getting documents.", task.getException());
                        }
                    }
                });

        String id = "1";
        DocumentReference result = db.collection("ambulances").document(id);
        result.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("tagisthis", "message");
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    reference = task.getResult();
                    if (document.exists()) {
                        driver = Objects.requireNonNull(document.get("driver_name")).toString();
                        driver_no = (String) document.get("driver_number");
                        t.setText(driver);
                        notifyObservers(); // Notify observers when data is updated
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}
