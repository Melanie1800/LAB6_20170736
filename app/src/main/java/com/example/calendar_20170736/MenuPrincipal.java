package com.example.calendar_20170736;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar_20170736.dto.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {
    Button cerrarSesion, add_event, revisar_eventos;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView nombre;

    DatabaseReference Usuarios;

    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        cerrarSesion=findViewById(R.id.cerrarSesion);
        nombre=findViewById(R.id.nombre);
        add_event=findViewById(R.id.add_event);
        revisar_eventos=findViewById(R.id.revisar_eventos);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        System.out.println("IMPRIMIENDO ID_USER MAIN" + user.getUid());

        Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");
        datos();
        add_event.setOnClickListener(v -> {
            startActivity(new Intent(MenuPrincipal.this,NuevoEvento.class));
        });

        revisar_eventos.setOnClickListener(v -> {
            startActivity(new Intent(MenuPrincipal.this,ListaEventosActivity.class));
        });




        cerrarSesion.setOnClickListener(v ->{
            SalirAplicacion();
        });

    }



    private void datos(){
        Usuarios.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Usuario usuario=snapshot.getValue(Usuario.class);
                    //String nombre1=""+snapshot.child("nombre");
                    System.out.println("USERRRRR" + usuario.getNombre());
                    nombre.setText(usuario.getNombre());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this,MainActivity.class));
        Toast.makeText(this, "Cerraste Sesión", Toast.LENGTH_SHORT).show();
    }
}