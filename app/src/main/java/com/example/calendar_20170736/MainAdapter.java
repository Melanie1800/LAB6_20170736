package com.example.calendar_20170736;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calendar_20170736.dto.Evento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainAdapter  extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    FirebaseDatabase firebaseDatabase;
    private List<Evento> listaEventos;
    private List<Evento> listaTotalEventos;
    private Context context;

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        Evento evento=listaEventos.get(position);
        holder.evento = evento;
        View view = holder.itemView.findViewById(R.id.rl2);
        Button btn_borrar=holder.itemView.findViewById(R.id.btn_borrar);
        Button btn_actualizar=holder.itemView.findViewById(R.id.btn_actualizar);
        view.setBackgroundColor(Color.GRAY);
        TextView tituloVista = holder.itemView.findViewById(R.id.tituloVista);
        tituloVista.setText (evento.getTitulo());
        TextView fInicio = holder.itemView.findViewById(R.id.fInicio);
        fInicio.setText ("Fecha inicio:" +evento.getFecha_inicio());
        TextView fFin = holder.itemView.findViewById(R.id.fFin);
        fFin.setText ("Fecha fin:"+evento.getFecha_fin());
        ImageView img1 = holder.itemView.findViewById(R.id.img1);
        Glide.with(context).load(evento.getImagen()).into(img1);



        //BTN CORRESPONDIENTE
        btn_borrar.setOnClickListener( v ->{
        delete(evento.getIde());
        });

        btn_actualizar.setOnClickListener( v ->{
            Bundle bundle = new Bundle();
            String id=String.valueOf(evento.getIde());
            bundle.putString("id", id);
            navController.navigate(R.id.action_listaEventosFragment_to_actualizarFragment, bundle);
        });


    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Evento evento;
        public ViewHolder(@NonNull View itemView){

            super(itemView);
        }
    }

    public void delete(String id){
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Eventos").child(id);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Se elimino correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No se elimino", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public List<Evento> getListaEventos() {
        return listaEventos;
    }

    public void setListaEventos(List<Evento> listaTotalEventos) {
        this.listaTotalEventos = listaTotalEventos;
        filtrarEvento("");
    }

    public void filtrarEvento(String texto) {
        if (listaEventos == null) {
            listaEventos = new ArrayList<>();
        } else {
            listaEventos.clear();
        }

        if (TextUtils.isEmpty(texto)) {
            listaEventos.addAll(listaTotalEventos);
        } else {
            texto = texto.toLowerCase(Locale.getDefault());
            for (Evento evento : listaTotalEventos) {

                if (evento.getTitulo().toLowerCase().contains(texto) || evento.getFecha_inicio().toLowerCase().contains(texto)
                        || evento.getFecha_fin().toLowerCase().contains(texto)) {
                    listaEventos.add(evento);
                }
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    private NavController navController;

    public NavController getNavController() {
        return navController;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }
}
