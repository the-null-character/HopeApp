package com.example.hope;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText pass;
    private TextView singupl,resetl;
    private View signinb;
    private String emails;
    private String passs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        email = findViewById(R.id.emailin);
        pass = findViewById(R.id.passin);
        singupl=findViewById(R.id.signinlink);
        signinb=findViewById(R.id.signinbut);
        mAuth = FirebaseAuth.getInstance();
        resetl= findViewById(R.id.resetlink);



        signinb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emails=email.getText().toString();
                passs=pass.getText().toString();

                String n = null;


                mAuth.signInWithEmailAndPassword(emails,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Signed ", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(Login.this,MapsActivity.class);

//                            i.putExtra("email",emails);

                            startActivity(i);


                        }
                        else{

                            Toast.makeText(Login.this, "Incorrect Email or Password,Try Again", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }


        });

        resetl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,resetpassword.class));
            }
        });

        singupl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(Login.this,Signup.class));

            }
        });

    }
}