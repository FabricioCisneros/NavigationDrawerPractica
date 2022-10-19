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
import androidx.fragment.app.DialogFragment;
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

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class DetallePersonaFragment extends DialogFragment {
    TextView nombre;

    ImageView imagen;
    Button entrega,btn_okEntrega,
            devolucionAccion,btn_okDevolucion,
            btn_InventarioAccion,btn_okInventario,btn_registroVenta;

    LinearLayout entrega_layout, devolucion_layout,layoutPrincipal;

    private FirebaseFirestore mfirestore;
    String dayOfWeek;
    RelativeLayout inventario_layout;
    ScrollView scroll;
    EditText txtEntregaElote,txtEntregaSinElote,txtDevolucionesElote,txtDevolucionesSinElote,
              etLunes,etMartes,etMiercoles,etJueves,etViernes;
    public String[] PrecioFrijoles = new String[2];
    private Handler mhandler= new Handler();
    private String NombreCliente,latitud,longitud;
    boolean entregable;
    String id_persona,id_sigPersona;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDayOfWeek();
        dayOfWeek= quitaDiacriticos(dayOfWeek);
        if(getArguments()!=null){
            id_persona=getArguments().getString("id_persona");
            id_sigPersona=getArguments().getString("id_sigPersona");
        }
    }

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
       ////OBTENER DATOS DE CLIENTE//////
        mfirestore.collection(dayOfWeek)
                .document(id_persona).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        NombreCliente=documentSnapshot.getString("nombre");
                        latitud=String.valueOf(documentSnapshot.getLong("lat"));
                        longitud=String.valueOf(documentSnapshot.getLong("longitud"));
                        entregable=documentSnapshot.getBoolean("entregable");
                        nombre.setText(NombreCliente);
                    }
                });


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

    String TotalFrijol (String cantidadFrijol, String devolucionFrijoles){
        String totalFrijol;
        int parseCountFrijol,Preciodb,parseCountDevolucionFrijol;

        parseCountDevolucionFrijol=Integer.parseInt(devolucionFrijoles);
        parseCountFrijol=Integer.parseInt(cantidadFrijol.trim());
        Preciodb=Integer.parseInt(PrecioFrijoles[0]);

        int integerTotal=((parseCountFrijol*Preciodb)-(parseCountDevolucionFrijol*Preciodb));
        totalFrijol=String.valueOf(integerTotal);
        return totalFrijol;
    }

    String TotalFrijolElote(String cantidadFrijolElote, String devolucionFrijoles){
        String totalFrijol;
        int parseCountFrijol,Preciodb,parseCountDevolucionFrijol;

        parseCountDevolucionFrijol=Integer.parseInt(devolucionFrijoles);
        parseCountFrijol=Integer.parseInt(cantidadFrijolElote.trim());
        Preciodb=Integer.parseInt(PrecioFrijoles[1]);

        int integerTotal=((parseCountFrijol*Preciodb)-(parseCountDevolucionFrijol*Preciodb));
        totalFrijol=String.valueOf(integerTotal);
        return totalFrijol;
    }

    public void EnvioDatos(){

        String entregaFrijolCount=txtEntregaSinElote.getText().toString();
        String entregaFrijolEloteCount=txtEntregaElote.getText().toString();
        String devolucionFrijolCount=txtDevolucionesSinElote.getText().toString();
        String devolucionFrijolEloteCount=txtDevolucionesElote.getText().toString();
        String lunesCount=etLunes.getText().toString();
        String martesCount=etMartes.getText().toString();
        String miercolesCount=etMiercoles.getText().toString();
        String juevesCount=etJueves.getText().toString();
        String viernesCount=etViernes.getText().toString();
        String fechaPedido=getFechaNormal(getFechaMilisegundos());
        String detalleFechaPedido=getFechaNormalHora(getFechaMilisegundos());
        Toast.makeText(getContext(), detalleFechaPedido, Toast.LENGTH_SHORT).show();

        String TotalFrijol=TotalFrijol(txtEntregaSinElote.getText().toString(),txtDevolucionesSinElote.getText().toString());
        String TotalFrijolElote=TotalFrijolElote(txtEntregaElote.getText().toString(),txtDevolucionesElote.getText().toString());
        String Total=String.valueOf((Integer.parseInt(TotalFrijol))+(Integer.parseInt(TotalFrijolElote)));
        /*
        Toast.makeText(getContext(), "precio frijoles sin elote: "+TotalFrijol, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "precio frijole con elote: "+TotalFrijolElote, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "precio Total: "+Total, Toast.LENGTH_SHORT).show();*/


        Intent intent= new Intent(getActivity(), PagoActivity.class);
        intent.putExtra("totalFrijolElote",TotalFrijolElote);//
        intent.putExtra("totalFrijol",TotalFrijol);//
        intent.putExtra("total",Total);
        intent.putExtra("entregaFrijolElote",entregaFrijolEloteCount);//
        intent.putExtra("entregaFrijol", entregaFrijolCount);//
        intent.putExtra("devolucionFrijol",devolucionFrijolCount);//
        intent.putExtra("devolucionFrijolElote",devolucionFrijolEloteCount);
        intent.putExtra("LunesInventario",lunesCount);//
        intent.putExtra("MartesInventario",martesCount);//
        intent.putExtra("MiercolesInvenario",miercolesCount);//
        intent.putExtra("JuevesInventario",juevesCount);//
        intent.putExtra("ViernesInventario",viernesCount);//
        intent.putExtra("fechaPedido",fechaPedido);
        intent.putExtra("nombreCliente", NombreCliente);//
        intent.putExtra("detalleFechaPedido", detalleFechaPedido);//
        intent.putExtra("latitud",latitud);
        intent.putExtra("longitud",longitud);
        intent.putExtra("entregable",String.valueOf(entregable));
        intent.putExtra("id_persona",id_persona);
        intent.putExtra("id_sigPersona",id_sigPersona);
        startActivity(intent);
    }
    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public void getDayOfWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfWeek = upperCaseFirst(sdf.format(d));
    }
    public static String quitaDiacriticos(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
