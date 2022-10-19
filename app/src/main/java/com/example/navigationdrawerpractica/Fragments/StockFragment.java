package com.example.navigationdrawerpractica.Fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.navigationdrawerpractica.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class StockFragment extends DialogFragment {
    private FirebaseFirestore mfirestore;
    TextView stockInicialFrijolElote, stockInicialFrijol,stockFrijol,stockFrijolElote
            ,devolucionFrijol,devolucionFrijolElote,entregasFrijol,entregasFrijolElote;
    String fechaOperacion;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock,container,false);
        mfirestore=FirebaseFirestore.getInstance();
        stockInicialFrijolElote=view.findViewById(R.id.txtInicialFrijolElote);
        stockInicialFrijol=view.findViewById(R.id.txtInicialFrijol);
        stockFrijolElote=view.findViewById(R.id.txtstockConElote);
        stockFrijol=view.findViewById(R.id.txtstockSinElote);
        devolucionFrijolElote=view.findViewById(R.id.txtDevolucionesElote);
        devolucionFrijol=view.findViewById(R.id.txtDevolucionesSinElote);
        entregasFrijolElote=view.findViewById(R.id.txtEntregaElote);
        entregasFrijol=view.findViewById(R.id.txtEntregaSinElote);
        fechaOperacion=getFechaNormal(getFechaMilisegundos());
        insertValue();
        return view;
    }

    public void insertValue(){
        mfirestore.collection("produccion").document(fechaOperacion).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    stockInicialFrijolElote.setText(String.valueOf(documentSnapshot.getLong("stockInicialFrijolElote")));
                    stockInicialFrijol.setText(String.valueOf(documentSnapshot.getLong("stockInicialFrijol")));
                    stockFrijolElote.setText(String.valueOf(documentSnapshot.getLong("stockFrijolElote")));
                    stockFrijol.setText(String.valueOf(documentSnapshot.getLong("stockFrijol")));
                    devolucionFrijolElote.setText(String.valueOf(documentSnapshot.getLong("devolucionesFrijolElote")));
                    devolucionFrijol.setText(String.valueOf(documentSnapshot.getLong("devolucionesFrijol")));
                    entregasFrijolElote.setText(String.valueOf(documentSnapshot.getLong("entregasFrijolElote")));
                    entregasFrijol.setText(String.valueOf(documentSnapshot.getLong("entregasFrijol")));
                }
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