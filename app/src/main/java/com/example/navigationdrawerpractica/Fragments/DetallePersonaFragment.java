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
    Pedido p = new Pedido();
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
    public String[] PrecioFrijoles = new String[1];
    private Handler mhandler= new Handler();

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

        mfirestore.collection("Producto").
                document("Frijoles").get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String preciocb=documentSnapshot.getString("precio");
                            p.setPrecioFrijoles(preciocb);
                            System.out.println("precio desde el metodo "+preciocb);
                        }
                    }
                });
        mfirestore.collection("Producto")
                .document("FrijolesElote").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        p.setPrecioFrijolesElote(documentSnapshot.getString("precio"));
                    }});

        Bundle objetoPersona = getArguments();
        Persona persona = null;;
        if(objetoPersona !=null){
            persona = (Persona) objetoPersona.getSerializable("objeto");
            nombre.setText(persona.getNombre());
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
                btn_registroVenta.setEnabled(false);

                if(ValidarCampos(campos)){
                    Toast.makeText(getContext(), "faltan campos por completar", Toast.LENGTH_SHORT).show();
                }else{
                   // Toast.makeText(getContext(), "procesando pago", Toast.LENGTH_SHORT).show();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        String fecha = sdf.format(fechamilisegundos);
        return fecha;
    }

    String TotalFrijol (String cantidadFrijol){
        String totalFrijol;
        int parseCountFrijol,Preciodb;

        parseCountFrijol=Integer.parseInt(cantidadFrijol.trim());
        Preciodb=Integer.parseInt(p.getPrecioFrijoles().trim());

        int integerTotal=parseCountFrijol*Preciodb;
        totalFrijol=String.valueOf(integerTotal);
        return totalFrijol;
    }

    String TotalFrijolElote(String cantidadFrijolElote){
        String totalFrijol;
        int parseCountFrijol,Preciodb;

        parseCountFrijol=Integer.parseInt(cantidadFrijolElote.trim());
        Preciodb=Integer.parseInt(p.getPrecioFrijolesElote().trim());

        int integerTotal=parseCountFrijol*Preciodb;
        totalFrijol=String.valueOf(integerTotal);
        return totalFrijol;
    }

    String TotalCobrar(){
        String totalCobro;

        int TotalCobro =(Integer.parseInt(p.getTotalFrijoles())+(Integer.parseInt(p.getTotalFrijolesElote())));
        totalCobro=String.valueOf(TotalCobro);
        return totalCobro;
    }

    public void EnvioDatos(){

        Toast.makeText(getContext(), "precio"+p.getPrecioFrijoles(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "precio"+p.getPrecioFrijolesElote(), Toast.LENGTH_SHORT).show();
        String TotalFrijol=TotalFrijol(txtEntregaSinElote.getText().toString());
        String TotalFrijolElote=TotalFrijolElote(txtEntregaElote.getText().toString());
        String Totalcobro=TotalCobrar();
        String entregaFrijolCount=txtEntregaSinElote.getText().toString();
        String entregaFrijolEloteCount=txtEntregaSinElote.getText().toString();
        String devolucionFrijolCount=txtDevolucionesSinElote.getText().toString();
        String devolucionFrijolEloteCount=txtDevolucionesElote.getText().toString();
        String lunesCount=etLunes.getText().toString();
        String martesCount=etMartes.getText().toString();
        String miercolesCount=etMiercoles.getText().toString();
        String juevesCount=etJueves.getText().toString();
        String viernesCount=etViernes.getText().toString();

        p.setFecha(getFechaNormal(getFechaMilisegundos()));
        p.setTotalFrijoles(TotalFrijol);
        p.setTotalFrijolesElote(TotalFrijolElote);
        p.setTotalCobro(Totalcobro);
        p.setEntregaSinELote(entregaFrijolCount);
        p.setEntregaElote(entregaFrijolEloteCount);
        p.setDevolucionSinElote(devolucionFrijolCount);
        p.setDevolucionElote(devolucionFrijolEloteCount);
        p.setInventarioLunes(lunesCount);
        p.setInventarioMartes(martesCount);
        p.setInventarioMiercoles(miercolesCount);
        p.setInventarioJueves(juevesCount);
        p.setInventarioViernes(viernesCount);
        Intent intent= new Intent(getActivity(), PagoActivity.class);
        startActivity(intent);
    }
}
