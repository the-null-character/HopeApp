package com.example.hope;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class bio extends AppCompatActivity {

    private EditText gender,dob,height,weight,bloodg,medicalc,allergy,medications,emername,emernum;
    private Button signup;

    private String genders,dobs,bloodgs,medicalcs,allergys,medicaltionss,emernames,emernums,heights,weights;

    FirebaseUser user;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);
        gender=findViewById(R.id.genderin);
        dob=findViewById(R.id.dobin);
        height=findViewById(R.id.heightin);
        weight=findViewById(R.id.weightin);
        bloodg=findViewById(R.id.bloodgin);
        medicalc=findViewById(R.id.medicalcin);
        allergy=findViewById(R.id.allergyandreactionin);
        medications=findViewById(R.id.medicationin);
        emername=findViewById(R.id.emergencycnamein);
        emernum=findViewById(R.id.emergencycnumberin);
        signup=findViewById(R.id.signupbutton);
        db = FirebaseFirestore.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genders=gender.getText().toString().trim();
                dobs=dob.getText().toString().trim();
                heights=height.getText().toString().trim();
                weights=weight.getText().toString().trim();
                bloodgs=bloodg.getText().toString().trim();
                medicalcs=medicalc.getText().toString().trim();
                allergys=allergy.getText().toString().trim();
                medicaltionss=medications.getText().toString().trim();
                emernames=emername.getText().toString().trim();
                emernums=emernum.getText().toString().trim();

                Map<String, Object> users = new HashMap<>();
                assert user != null;

                users.put("gender",genders);
                users.put("dob",dobs);
                users.put("height", heights);
                users.put("weight",weights);
                users.put("blood_group",bloodgs);
                users.put("medical_conditions",medicalcs);
                users.put("allergies",allergys);
                users.put("medcications",medicaltionss);
                users.put("emergency_contanct_name",emernames);
                users.put("emergency_contact",emernums);


                db.collection("users")
                        .document(user.getUid()).update(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(bio.this, "Sucess", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                startActivity(new Intent(bio.this,MapsActivity.class));

            }
        });





    }
}