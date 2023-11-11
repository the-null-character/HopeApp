package com.example.hope;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private View signup;
    private TextView signinl;
    private TextView Fname,Phone,email,pass,pass1;
    private String Fnames,Phones,emails,passs,pass1s;
    private FirebaseAuth mAuth;
    FirebaseFirestore db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.signupbut);
        signinl=findViewById(R.id.signinlink);
        Fname = findViewById(R.id.namee);
        Phone = findViewById(R.id.phonee);
        email = findViewById(R.id.emaile);
        pass= findViewById(R.id.pass1e);
        pass1 = findViewById(R.id.confirmpass1);
        db = FirebaseFirestore.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fnames = Fname.getText().toString().trim();
                Phones = Phone.getText().toString().trim();
                emails = email.getText().toString().trim();
                passs = pass.getText().toString().trim();
                pass1s = pass1.getText().toString().trim();
                mAuth = FirebaseAuth.getInstance();

                if(TextUtils.isEmpty(emails)){

                    email.setError("Enter email");
                    return;


                }

                if(TextUtils.isEmpty(passs)){

                    pass.setError("Enter Pasword");
                    return;


                }

                if (!(passs.equals(pass1s))) {

                    pass1.setError("Passwords do not match");

                    return;


                }

                mAuth.createUserWithEmailAndPassword(emails,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            Map<String, Object> users = new HashMap<>();
                            assert user != null;

                            users.put("fullname", Fnames);
                            users.put("phone",Phones);
                            users.put("email", emails);

                            db.collection("users")
                                    .document(user.getUid()).set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Signup.this, "Sucess", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            startActivity(new Intent(Signup.this,bio.class));

                        }
                        else {
                            Toast.makeText(Signup.this, "Sign Up failed, Try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });









            }
        });

        signinl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        });
    }
}