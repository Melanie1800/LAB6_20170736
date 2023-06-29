package com.example.calendar_20170736;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.calendar_20170736.databinding.FragmentActualizarBinding;
import com.example.calendar_20170736.dto.Evento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActualizarFragment extends Fragment {

    FragmentActualizarBinding binding;
    FirebaseDatabase firebaseDatabase;

    private String id;

    TextView fecha_actual,Fecha,Hora1,Hora2;
    EditText Titulo, descripcion;
    Button btn_calendario, agregar_nota,btn_hora1,btn_hora2,btn_guardar;
    final Calendar calendario=Calendar.getInstance();
    int dia,mes,anio, hora, minutos, segundos;

    DatabaseReference eventos;
    DatabaseReference Usuarios;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    List<Evento> listaEventos;
    Evento evento;
    String hora_inicio;
    String hora_fin,fecha_final,fecha_hora_final1,fecha_hora_final2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActualizarBinding.inflate(inflater, container, false);
        id = getArguments().getString("id");
        System.out.println("id");
        System.out.println("eventoooo"+ id);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        NavController navController = NavHostFragment.findNavController(this);
        //MUESTRA EL EVENTO
        databaseReference.child("Eventos").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue(Evento.class)+ "AAAAAAAAAAAAA");
                Evento evento = snapshot.getValue(Evento.class);
                //Glide.with(getContext()).load(evento.getImagen()).into(binding.img1);
                //textViewnombre,email,costo, usuario, ciudad2, edad, telefono, nacionalidad
                binding.titulo1.setText(evento.getTitulo());
                binding.descripcion1.setText(evento.getDescripcion());
                System.out.println("FECHAAAA_INICIOOO"+ evento.getFecha_fin());
                System.out.println("FECHAAAA_FIN"+ evento.getFecha_inicio());
                String[] fechaHora1 = evento.getFecha_fin().split(" ");
                String[] fechaHora2 = evento.getFecha_inicio().split(" ");

                binding.Fecha11.setText(String.valueOf(fechaHora1[0]));
                binding.Hora11.setText(String.valueOf(fechaHora1[1]));
                binding.Hora22.setText(String.valueOf(fechaHora2[1]));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnCalendario1.setOnClickListener(v->{
            DatePickerDialog datePickerDialog=new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int anio_select, int mes_select, int dia_select) {
                    String diaFormateado, mesformateado,anioformateado;
                    if(dia_select<10){
                        diaFormateado="0"+String.valueOf(dia_select);

                    }else{
                        diaFormateado=String.valueOf(dia_select);
                    }
                    int MES=mes_select+1;
                    if(MES<10){
                        mesformateado="0"+String.valueOf(MES);

                    }else{
                        mesformateado=String.valueOf(MES);
                    }
                    anioformateado=String.valueOf(anio_select);
                    //SETEO DE FECHA
                    fecha_final=diaFormateado+"/"+mesformateado+"/"+anioformateado;
                    if(fecha_final==null){
                        String[] fechaHora1 = evento.getFecha_fin().split(" ");
                        fecha_final=String.valueOf(fechaHora1[0]);
                    }
                    binding.Fecha11.setText(diaFormateado+"/"+mesformateado+"/"+anioformateado);

                }
            },anio,mes,dia);
            datePickerDialog.show();
        });

        binding.btnHora11.setOnClickListener(v->{
            TimePickerDialog timePickerDialog=new TimePickerDialog(requireContext(),new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourDay, int minute){
                    hora_inicio=String.valueOf(hourDay)+":"+String.valueOf(minute);

                    binding.Hora11.setText(hourDay+":"+minute);
                    if(hora_inicio==null){
                        String[] fechaHora1 = evento.getFecha_inicio().split(" ");
                        hora_inicio=String.valueOf(fechaHora1[1]);
                    }
                }
            },hora,minutos,false);
            timePickerDialog.show();

        });

        binding.btnHora22.setOnClickListener(v->{
            TimePickerDialog timePickerDialog=new TimePickerDialog(requireContext(),new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourDay, int minute){
                    hora_fin=String.valueOf(hourDay)+":"+String.valueOf(minute);
                    binding.Hora22.setText(hourDay+":"+minute);
                    if(hora_fin==null){
                        String[] fechaHora2 = evento.getFecha_fin().split(" ");
                        hora_fin=String.valueOf(fechaHora2[1]);
                    }
                }
            },hora,minutos,false);
            timePickerDialog.show();
        });

        binding.guardar1.setOnClickListener(v->{
            fecha_hora_final1=fecha_final+" "+hora_inicio;
            System.out.println("HORAAAA INICIO " + fecha_hora_final1);
            fecha_hora_final2=fecha_final+" "+hora_fin;
            System.out.println("HORAAAA INICIO " + fecha_hora_final2);
            databaseReference.child("Eventos").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println(snapshot.getValue(Evento.class)+ "AAAAAAAAAAAAA");
                    Evento evento = snapshot.getValue(Evento.class);
                    //textViewnombre,email,costo, usuario, ciudad2, edad, telefono, nacionalidad
                    evento.setTitulo(binding.titulo1.getText().toString());
                    evento.setDescripcion(binding.descripcion1.getText().toString());
                    System.out.println("FECHAAAA_INICIOOO"+ evento.getFecha_fin());
                    System.out.println("FECHAAAA_FIN"+ evento.getFecha_inicio());
                    evento.setFecha_inicio(fecha_hora_final1);
                    evento.setFecha_fin(fecha_hora_final2);
                    //String id= binding.titulo1.getText().toString().trim();
                    evento.setImagen("https://previews.123rf.com/images/jemastock" +
                            "/jemastock1705/jemastock170504684/77792450-color-contorno-de-dibujos-animados-calendario-con-el-día-indicado.jpg");
                    eventos= FirebaseDatabase.getInstance().getReference();
                    eventos.child("Eventos").child(evento.getIde()).setValue(evento)
                            .addOnSuccessListener(aVoid ->{
                                Log.d("msg", "evento registrado exitosamente");
                                Toast.makeText(requireContext(), "SE ACTUALIZÓ", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e->{
                                Log.d("msg",e.getMessage());
                            });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            /*
            DatabaseReference nodoRef = databaseReference.child(id);

            Map<String, Object> actualizaciones = new HashMap<>();
            actualizaciones.put("titulo", binding.titulo1.getText().toString());
            actualizaciones.put("descripcion", binding.descripcion1.getText().toString());
            actualizaciones.put("fecha_inicio", fecha_hora_final1);
            actualizaciones.put("fecha_fin", fecha_hora_final2);


            nodoRef.updateChildren(actualizaciones, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    if (error == null) {
                        Log.d("msg", "evento registrado exitosamente");
                        Toast.makeText(requireContext(), "SE ACTUALIZÓ", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("msg","nose logro");
                    }
                }
            });*/



        });

        return binding.getRoot();
    }

}