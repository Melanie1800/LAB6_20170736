package com.example.calendar_20170736;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.calendar_20170736.dto.Evento;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListaEventosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);


    }
}