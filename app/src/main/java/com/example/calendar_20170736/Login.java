package com.example.calendar_20170736;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    EditText Correo, Password;
    Button logueo;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    String correo="", pass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Correo=findViewById(R.id.correo);
        Password=findViewById(R.id.password);
        logueo=findViewById(R.id.logueo);
        firebaseAuth=FirebaseAuth.getInstance();

        logueo.setOnClickListener(v ->{
            firebaseAuth.signInWithEmailAndPassword(Correo.getText().toString(),Password.getText().toString())
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user=firebaseAuth.getCurrentUser();
                                startActivity(new Intent(Login.this,MenuPrincipal.class));
                                Toast.makeText(Login.this, "Inicio Sesion", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}