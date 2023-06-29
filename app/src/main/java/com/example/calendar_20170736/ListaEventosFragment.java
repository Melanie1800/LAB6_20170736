package com.example.calendar_20170736;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.example.calendar_20170736.databinding.FragmentListaEventosBinding;
import com.example.calendar_20170736.dto.Evento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ListaEventosFragment extends Fragment {
    FragmentListaEventosBinding binding;
    FirebaseDatabase firebaseDatabase;
    MainAdapter mainAdapter;
    private List<Evento> listaTotalEventos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        binding = FragmentListaEventosBinding.inflate(inflater, container, false);
        mainAdapter = new MainAdapter();
        mainAdapter.setContext(getContext());
        mainAdapter.setListaEventos(new ArrayList<>());
        NavController navController = NavHostFragment.findNavController(ListaEventosFragment.this);
        mainAdapter.setNavController(navController);
        /*
        androidx.appcompat.widget.SearchView searchView=binding.searchView;
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscar(newText);
                return true;
            }
        });

         */

        binding.filtro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainAdapter.filtrarEvento(s.toString());
            }
        });
        databaseReference.child("Eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTotalEventos.clear();
                for (DataSnapshot children : snapshot.getChildren()) {
                    Evento evento = children.getValue(Evento.class);
                    listaTotalEventos.add(evento);
                }
                mainAdapter.setListaEventos(listaTotalEventos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.rv.setAdapter(mainAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));



        return binding.getRoot();
    }

    private void buscar (String s){
        ArrayList<Evento> milista=new ArrayList<>();
        for(Evento evento:listaTotalEventos){
            if (evento.getTitulo().toLowerCase(Locale.getDefault()).contains(s)
                    || evento.getDescripcion().toLowerCase(Locale.getDefault()).contains(s)|| evento.getFecha_inicio().toLowerCase(Locale.getDefault()).contains(s)) {
                milista.add(evento);
            }
        }
        mainAdapter.setListaEventos(milista);
    }

}