package com.example.navigationdrawerpractica.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navigationdrawerpractica.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CorteFragment extends DialogFragment{
    private FirebaseFirestore mfirestore;
    TextView totalVentasFrijolElote,totalVentasFrijol,totalVentasCorte;
    String fechaOperacion;
    public CorteFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_corte, container, false);
        mfirestore = FirebaseFirestore.getInstance();
        totalVentasFrijolElote = view.findViewById(R.id.txtVentasFrijolElote);
        totalVentasFrijol = view.findViewById(R.id.txtVentasFrijol);
        totalVentasCorte = view.findViewById(R.id.txtVentasTotales);
        fechaOperacion=getFechaNormal(getFechaMilisegundos());
        insertValue();
        return view;
    }

    public void insertValue(){
        mfirestore.collection("Corte").document(fechaOperacion).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                totalVentasFrijolElote.setText("$"+String.valueOf(documentSnapshot.getLong("totalVendidoFrijolElote")));
                totalVentasFrijol.setText("$"+String.valueOf(documentSnapshot.getLong("totalVendidoFrijol")));
                totalVentasCorte.setText("$"+String.valueOf(documentSnapshot.getLong("totalVendido")));
            }
        });
    }

    public long getFechaMilisegundos(){
        Calendar calendar =Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();
        return tiempounix;
    }
    public String getFechaNormal(long fechamilisegundos){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = sdf.format(fechamilisegundos);
        return fecha;
    }
}