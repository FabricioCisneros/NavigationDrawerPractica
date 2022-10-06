package com.example.navigationdrawerpractica.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.navigationdrawerpractica.Entidades.Pedido;
import com.example.navigationdrawerpractica.Entidades.Persona;
import com.example.navigationdrawerpractica.Interfaces.PagoActivity;
import com.example.navigationdrawerpractica.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class DetallePersonaFragment extends Fragment {
    TextView nombre;

    ImageView imagen;
    Button entrega,btn_okEntrega,
            devolucionAccion,btn_okDevolucion,
            btn_InventarioAccion,btn_okInventario,btn_registroVenta;
    LinearLayout entrega_layout, devolucion_layout,layoutPrincipal;

    private FirebaseFirestore mfirestore;

    RelativeLayout inventario_layout;
    ScrollView scroll;
    EditText txtEntregaElote,txtEntregaSinElote,txtDevolucionesElote,txtDevolucionesSinElote,
              etLunes,etMartes,etMiercoles,etJueves,etViernes;
    public String[] PrecioFrijoles = new String[2];
    private Handler mhandler= new Handler();
    private String NombreCliente;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detalle_persona_fragment,container,false);


        txtEntregaElote=view.findViewById(R.id.txtEntregaElote);
        txtEntregaSinElote=view.findViewById(R.id.txtEntregaSinElote);
        txtDevolucionesElote=view.findViewById(R.id.txtDevolucionesElote);
        txtDevolucionesSinElote=view.findViewById(R.id.txtDevolucionesSinElote);
        etLunes=view.findViewById(R.id.etLunes);
        etMartes=view.findViewById(R.id.etMartes);
        etMiercoles=view.findViewById(R.id.etMiercoles);
        etJueves=view.findViewById(R.id.etJueves);
        etViernes=view.findViewById(R.id.etViernes);

        nombre = view.findViewById(R.id.txt_DetailNombre);
        entrega =view.findViewById(R.id.btn_entrega);
        entrega_layout=view.findViewById(R.id.linearLayoutAgregar);
        btn_okEntrega=view.findViewById(R.id.btn_Okentrega);

        devolucion_layout=view.findViewById(R.id.linearLayoutDevoluciones);
        devolucionAccion=view.findViewById(R.id.txtDevolucion);
        btn_okDevolucion=view.findViewById(R.id.btn_Okdevolucion);


        inventario_layout=view.findViewById(R.id.constraintLayoutInventario);
        btn_InventarioAccion=view.findViewById(R.id.btn_inventario);
        btn_okInventario=view.findViewById(R.id.btn_OkInventario);
        layoutPrincipal=view.findViewById(R.id.layoutPrincipal);

        btn_registroVenta=view.findViewById(R.id.btn_venta);
        scroll=view.findViewById(R.id.scrollView);

        EditText[] campos={txtEntregaElote,txtEntregaSinElote,txtDevolucionesElote,txtDevolucionesSinElote,
                etLunes,etMartes,etMiercoles,etJueves,etViernes};
        mfirestore=FirebaseFirestore.getInstance();


        //TOMANDO DATOS DEL PRODUCTO DESDE LA BASE DATOS
        mfirestore.collection("Producto").
                document("Frijoles").get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String preciocb=documentSnapshot.getString("precio");
                            PrecioFrijoles[0]=preciocb;
                            System.out.println("precio desde el metodo "+preciocb);
                        }
                    }
                });
        //TOMANDO DATOS DEL PRODUCTO CON ELOTE DESDE LA BASE DE DATOS
        mfirestore.collection("Producto")
                .document("FrijolesElote").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        PrecioFrijoles[1]=documentSnapshot.getString("precio");
                    }});
////////////////////////////////////////

        Bundle objetoPersona = getArguments();
        Persona persona = null;;
        if(objetoPersona !=null){
            persona = (Persona) objetoPersona.getSerializable("objeto");
            nombre.setText(persona.getNombre());
            NombreCliente=nombre.getText().toString();
        }
        ///ACCIONES DE ENTREGAS////
        btn_okEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrega_layout.setVisibility(View.GONE);
            }
        });
        entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrega_layout.setVisibility(
                        entrega_layout.getVisibility()==View.VISIBLE ? View.GONE:View.VISIBLE);
            }
        });

        ////ACCIONES DE DEVOLUCIONES//////
        btn_okDevolucion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                devolucion_layout.setVisibility(View.GONE);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.scrollTo(0,devolucionAccion.getBottom());
                    }
                });

            }
        });

        devolucionAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devolucion_layout.setVisibility(
                        devolucion_layout.getVisibility()==View.VISIBLE ? View.GONE:View.VISIBLE);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.scrollTo(0,devolucionAccion.getBottom());
                    }
                });

            }
        });
        ///ACCIONES DE INVENTARIO/////
        btn_InventarioAccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventario_layout.setVisibility(
                        inventario_layout.getVisibility()==View.VISIBLE ? View.GONE:View.VISIBLE);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.scrollTo(0,btn_InventarioAccion.getBottom()+20);
                    }
                });
            }

        });

        btn_okInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventario_layout.setVisibility(View.GONE);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.scrollTo(0,btn_InventarioAccion.getBottom());
                    }
                });
            }
        });

        btn_registroVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ValidarCampos(campos)==true){
                    Toast.makeText(getContext(), "faltan campos por completar", Toast.LENGTH_SHORT).show();

                }else{
                    AlertDialog.Builder altdial= new AlertDialog.Builder(getContext());
                    altdial.setMessage("Estas seguro de mandar estos datos?").setCancelable(false).
                            setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(), "Cargando los datos", Toast.LENGTH_SHORT).show();
                                    mhandler.postDelayed(WaitEnvioDatos, 5000);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = altdial.create();
                    alert.setTitle("CONFIRMACION");
                    alert.show();
                }

            }
        });

        return view;
    }
    private Runnable WaitEnvioDatos =new Runnable() {
        @Override
        public void run() {

            EnvioDatos();
        }
    };
    public boolean ValidarCampos (EditText[] campos){

        for(int i=0;i<campos.length;i++){
            String cadena=campos[i].getText().toString();
            if(cadena.trim().isEmpty()){
                return true;
            }
        }
        return false;
    }

    /*TOMAR LA FECHA*/

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
    public String getFechaNormalHora(long fechamilisegundos){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        String fecha = sdf.format(fechamilisegundos);
        return fecha;
    }

    String TotalFrijol (String cantidadFrijol){
        String totalFrijol;
        int parseCountFrijol,Preciodb;

        parseCountFrijol=Integer.parseInt(cantidadFrijol.trim());
        Preciodb=Integer.parseInt(PrecioFrijoles[0]);

        int integerTotal=parseCountFrijol*Preciodb;
        totalFrijol=String.valueOf(integerTotal);
        return totalFrijol;
    }

    String TotalFrijolElote(String cantidadFrijolElote){
        String totalFrijol;
        int parseCountFrijol,Preciodb;

        parseCountFrijol=Integer.parseInt(cantidadFrijolElote.trim());
        Preciodb=Integer.parseInt(PrecioFrijoles[1]);
        int integerTotal=parseCountFrijol*Preciodb;
        totalFrijol=String.valueOf(integerTotal);
        return totalFrijol;
    }

    public void EnvioDatos(){

        String entregaFrijolCount=txtEntregaSinElote.getText().toString();
        String entregaFrijolEloteCount=txtEntregaSinElote.getText().toString();
        String devolucionFrijolCount=txtDevolucionesSinElote.getText().toString();
        String devolucionFrijolEloteCount=txtDevolucionesElote.getText().toString();
        String lunesCount=etLunes.getText().toString();
        String martesCount=etMartes.getText().toString();
        String miercolesCount=etMiercoles.getText().toString();
        String juevesCount=etJueves.getText().toString();
        String viernesCount=etViernes.getText().toString();
        String fechaPedido=getFechaNormal(getFechaMilisegundos());
        String detalleFechaPedido=getFechaNormalHora(getFechaMilisegundos());


        String TotalFrijol=TotalFrijol(txtEntregaSinElote.getText().toString());
        String TotalFrijolElote=TotalFrijolElote(txtEntregaElote.getText().toString());
        String Total=String.valueOf((Integer.parseInt(TotalFrijol))+(Integer.parseInt(TotalFrijolElote)));
        /*
        Toast.makeText(getContext(), "precio frijoles sin elote: "+TotalFrijol, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "precio frijole con elote: "+TotalFrijolElote, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "precio Total: "+Total, Toast.LENGTH_SHORT).show();*/


        Intent intent= new Intent(getActivity(), PagoActivity.class);
        intent.putExtra("TotalFrijolElote",TotalFrijolElote);//
        intent.putExtra("TotalFrijol",TotalFrijol);//
        intent.putExtra("Total",Total);
        intent.putExtra("EntregaFrijolElote",entregaFrijolEloteCount);//
        intent.putExtra("EntregaFrijol", entregaFrijolCount);//
        intent.putExtra("DevolucionFrijol",devolucionFrijolCount);//
        intent.putExtra("DevolucionFrijolElote",devolucionFrijolEloteCount);//
        intent.putExtra("LunesInventario",lunesCount);//
        intent.putExtra("MartesInventario",martesCount);//
        intent.putExtra("MiercolesInvenario",miercolesCount);//
        intent.putExtra("JuevesInventario",juevesCount);//
        intent.putExtra("ViernesInventario",viernesCount);//
        intent.putExtra("FechaPedido",fechaPedido);
        intent.putExtra("NombreCliente", NombreCliente);//
        intent.putExtra("DetalleFechaPedido", detalleFechaPedido);//
        startActivity(intent);
    }
}
