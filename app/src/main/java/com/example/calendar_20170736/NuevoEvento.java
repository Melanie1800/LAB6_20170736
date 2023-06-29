package com.example.calendar_20170736;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.calendar_20170736.dto.Evento;
import com.example.calendar_20170736.dto.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NuevoEvento extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento);
        inciarV();
        fecha_Actual();
        listaEventos=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        System.out.println("nuevoooo" +user.getUid());
        System.out.println(user.getUid());
        dia=calendario.get(Calendar.DAY_OF_MONTH);
        mes=calendario.get(Calendar.MONTH);
        anio=calendario.get(Calendar.YEAR);
        hora=calendario.get(Calendar.DAY_OF_MONTH);
        minutos=calendario.get(Calendar.MONTH);
        segundos=calendario.get(Calendar.YEAR);
        Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");



        btn_calendario.setOnClickListener(v->{
            DatePickerDialog datePickerDialog=new DatePickerDialog(NuevoEvento.this, new DatePickerDialog.OnDateSetListener() {
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
                    Fecha.setText(diaFormateado+"/"+mesformateado+"/"+anioformateado);

                }
            },anio,mes,dia);
            datePickerDialog.show();
        });

        btn_hora1.setOnClickListener(v->{
            TimePickerDialog timePickerDialog=new TimePickerDialog(NuevoEvento.this,new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourDay, int minute){
                    hora_inicio=String.valueOf(hourDay)+":"+String.valueOf(minute);

                    Hora1.setText(hourDay+":"+minute);
                }
            },hora,minutos,false);
            timePickerDialog.show();

        });

        btn_hora2.setOnClickListener(v->{
            TimePickerDialog timePickerDialog=new TimePickerDialog(NuevoEvento.this,new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourDay, int minute){
                    hora_fin=String.valueOf(hourDay)+":"+String.valueOf(minute);
                    Hora2.setText(hourDay+":"+minute);
                }
            },hora,minutos,false);
            timePickerDialog.show();

        });



        //seteo eventos

        btn_guardar.setOnClickListener(v->{
            fecha_hora_final1=fecha_final+" "+hora_inicio;
            fecha_hora_final2=fecha_final+" "+hora_fin;
            evento=new Evento();
            evento.setDescripcion(descripcion.getText().toString());
            evento.setTitulo(Titulo.getText().toString());
            evento.setFecha_inicio(fecha_hora_final1);
            evento.setFecha_fin(fecha_hora_final2);
            evento.setUid(user.getUid());
            String id= Titulo.getText().toString().trim();
            evento.setIde(id);
            evento.setImagen("https://previews.123rf.com/images/jemastock" +
                    "/jemastock1705/jemastock170504684/77792450-color-contorno-de-dibujos-animados-calendario-con-el-día-indicado.jpg");
            eventos= FirebaseDatabase.getInstance().getReference();
            eventos.child("Eventos").child(id).setValue(evento)
                    .addOnSuccessListener(aVoid ->{
                        Log.d("msg", "evento registrado exitosamente");
                        Toast.makeText(this, "evento registrado exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e->{
                        Log.d("msg",e.getMessage());
                    });
        });

    }

    public void fecha_Actual(){
        int dia1,mes1,anio1, hora1, minutos1, segundos1;
        //seteo de fecha de hoy
        dia1=calendario.get(Calendar.DAY_OF_MONTH);
        mes1=calendario.get(Calendar.MONTH);
        anio1=calendario.get(Calendar.YEAR);
        hora1 = calendario.get(Calendar.HOUR_OF_DAY);
        minutos1 = calendario.get(Calendar.MINUTE);
        segundos1= calendario.get(Calendar.SECOND);
        String fechaHoraActual = String.format("%02d/%02d/%d %02d:%02d:%02d", dia1, mes1, anio1, hora1, minutos1, segundos1);
        fecha_actual.setText(fechaHoraActual);
    }

    public void inciarV(){
        fecha_actual=findViewById(R.id.fecha_actual);
        Fecha=findViewById(R.id.Fecha1);
        Titulo=findViewById(R.id.titulo);
        descripcion=findViewById(R.id.descripcion);
        btn_calendario=findViewById(R.id.btn_calendario1);
        btn_hora1=findViewById(R.id.btn_hora1);
        btn_hora2=findViewById(R.id.btn_hora2);
        btn_guardar=findViewById(R.id.guardar);
        Hora1=findViewById(R.id.Hora1);
        Hora2=findViewById(R.id.Hora2);

    }

}