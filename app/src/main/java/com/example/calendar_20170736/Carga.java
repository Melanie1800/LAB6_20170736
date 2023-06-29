package com.example.calendar_20170736;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Carga extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);


        firebaseAuth=FirebaseAuth.getInstance();
        int Tiempo =3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Carga.this,MainActivity.class));
                finish();
                verificarUser();
            }
        },Tiempo);

    }



    //VERIFICO INICIO DE SESION
    public void verificarUser(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            startActivity(new Intent(Carga.this,MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(Carga.this,MenuPrincipal.class));
            finish();
        }
    }
}