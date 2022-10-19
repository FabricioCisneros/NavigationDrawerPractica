package com.example.navigationdrawerpractica.Interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationdrawerpractica.Entidades.Pedido;
import com.example.navigationdrawerpractica.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PagoActivity extends AppCompatActivity {


    Bundle datos;
    private FirebaseFirestore mfirestore;
    public String PagoEfectivoString;
    public String Cambio;
    private LocationManager ubicacion;
    //double LatitudCliente=20.735016785031025,LongitudCliente=-103.41271473536557,LatitudActual,LongitudActual;
    double LatitudCliente,LongitudCliente,LatitudActual,LongitudActual;
    public int[] getDatos = new int[6];
    public double[] getCorte= new double[3];
    Range<Double> rangeLat;
    Range<Double> rangeLong;
    String dayOfWeek;

    Long timeint =  System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    //System.out("fecha y hora actuales: "timeint);
    Date time = new Date(timeint);
    //java.sql.Timestamp tiempo= new java.sql.Timestamp(timeint);
    Timestamp tiempo = new Timestamp(time);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pago_activity);
        datos = getIntent().getExtras();
        mfirestore = FirebaseFirestore.getInstance();
        LatitudCliente=Double.parseDouble(datos.getString("latitud"));
        LongitudCliente=Double.parseDouble(datos.getString("longitud"));
        System.out.println(LatitudCliente+"latitud<----------<");
        rangeLat = Range.closed(LatitudCliente-.001, LatitudCliente+.001);
        rangeLong = Range.closed(LongitudCliente-.002, LongitudCliente+.002);
        getDayOfWeek();
        dayOfWeek=quitaDiacriticos(dayOfWeek);
        Toast.makeText(this, "date: "+tiempo, Toast.LENGTH_SHORT).show();
        System.out.println("TIEMPO:::>"+tiempo);

        TextView TotalFrijol = findViewById(R.id.txtTotalFrijol);
        TotalFrijol.setText("$" + datos.getString("totalFrijol"));
        TextView TotalFrijolElote = findViewById(R.id.txtTotalFrijolElote);
        TotalFrijolElote.setText("$" + datos.getString("totalFrijolElote"));
        TextView total = findViewById(R.id.txtTotal);
        total.setText("$" + datos.getString("total"));
        EditText PagoEfectivo = findViewById(R.id.etEfectivo);
        Button btnPago = findViewById(R.id.btnPago);


        //Toast.makeText(this, "Fecha del Pedido " + datos.getString("FechaPedido"), Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "Nombre de la tienda " + datos.getString("NombreCliente"), Toast.LENGTH_SHORT).show();

        btnPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PagoEfectivoString = PagoEfectivo.getText().toString();
                if (PagoEfectivoString.isEmpty()) {
                    Toast.makeText(PagoActivity.this, "Ingresa el efectivo", Toast.LENGTH_SHORT).show();
                } else {
                    Cambio = Cambio(datos.getString("total"), PagoEfectivoString);
                    if (Integer.parseInt(Cambio) < 0) {
                        Toast.makeText(PagoActivity.this, "Entrega de efectivo Invalida", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(PagoActivity.this, "Procesando pago", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder altdial = new AlertDialog.Builder(PagoActivity.this);
                        altdial.setMessage("Pedido listo devuelva $" + Cambio + " al cliente").setCancelable(false).
                                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Localizacion();
                                       // InsertarDatos();
                                    }
                                });
                        AlertDialog alert = altdial.create();
                        alert.setTitle("PAGO");
                        alert.show();
                    }
                }
            }
        });
    }

    public void InsertarDatos() {
        CollectionReference venta = mfirestore.collection("ventas");

        Map<String, Object> pedido = new HashMap<>();
        Map<String, Object> detalleVenta = new HashMap<>();
        detalleVenta.put("cliente", datos.getString("nombreCliente"));
        detalleVenta.put("frijolesEntrega", Integer.parseInt(datos.getString("entregaFrijol")));
        detalleVenta.put("frijolesEloteEntrega", Integer.parseInt(datos.getString("entregaFrijolElote")));
        detalleVenta.put("frijolesDevolucion", Integer.parseInt(datos.getString("devolucionFrijol")));
        detalleVenta.put("frijolesEloteDevolucion", Integer.parseInt(datos.getString("devolucionFrijolElote")));
        detalleVenta.put("totalFrijol", Integer.parseInt(datos.getString("totalFrijol")));
        detalleVenta.put("totalFrijolElote", Integer.parseInt(datos.getString("totalFrijolElote")));
        detalleVenta.put("fecha", FieldValue.serverTimestamp());

        Map<String, Object> Inventario = new HashMap<>();
        Inventario.put("lunes",Integer.parseInt(datos.getString("LunesInventario")));
        Inventario.put("martes", Integer.parseInt(datos.getString("MartesInventario")));
        Inventario.put("miercoles", Integer.parseInt(datos.getString("MiercolesInvenario")));
        Inventario.put("jueves", Integer.parseInt(datos.getString("JuevesInventario")));
        Inventario.put("viernes", Integer.parseInt(datos.getString("ViernesInventario")));
        detalleVenta.put("inventarioDia", Inventario);

      /*  detalleVenta.put("lunes", datos.getString("LunesInventario"));
        detalleVenta.put("martes",datos.getString("MartesInventario"));
        detalleVenta.put("miercoles",datos.getString("MiercolesInvenario"));
        detalleVenta.put("jueves", datos.getString("JuevesInventario"));
        detalleVenta.put("viernes",datos.getString("ViernesInventario"));*/

        detalleVenta.put("efectivoRecibido", Double.parseDouble(PagoEfectivoString));
        detalleVenta.put("cambio", Double.parseDouble(Cambio));
        detalleVenta.put("total", Double.parseDouble(datos.getString("total")));
        pedido.put(datos.getString("nombreCliente"), detalleVenta);

        venta.document(datos.getString("fechaPedido")).set(pedido, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Conexion exitosa con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });

        if((datos.getString("id_sigPersona")!="end")){
            CollectionReference lista = mfirestore.collection(dayOfWeek);
            lista.document(datos.getString("id_persona")).update("entregable",false);

            CollectionReference lista2 = mfirestore.collection(dayOfWeek);
            lista2.document(datos.getString("id_sigPersona")).update("entregable",true);
        }else{
            CollectionReference lista = mfirestore.collection(dayOfWeek);
            lista.document(datos.getString("id_persona")).update("entregable",false);
        }
        finish();
        Intent intent = new Intent(PagoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public String Cambio(String total, String EfectivoRecibido) {
        String Cambio;
        int calculoCambio = (Integer.parseInt(EfectivoRecibido) - (Integer.parseInt(total)));
        Cambio = String.valueOf(calculoCambio);
        return Cambio;
    }

    private void Localizacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
            },1000);
        }
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(ubicacion!=null){
           /* LatitudActual=loc.getLatitude();
            LongitudActual=loc.getLongitude();*/
            LatitudActual=12;
            LongitudActual=12;
            System.out.println("Lat:"+LatitudActual);
            System.out.println("Lat:"+LongitudActual);
            if(rangeLat.contains(LatitudActual) && rangeLong.contains(LongitudActual)){
                System.out.println(rangeLat.contains(LatitudActual));
                System.out.println(rangeLat.contains(LongitudActual));
                Toast.makeText(this, "dentro del rango", Toast.LENGTH_SHORT).show();
                getStockData();
                getCorteData();
                InsertarDatos();
            }else{
                Toast.makeText(this, "no estas dentro del rango", Toast.LENGTH_SHORT).show();
            }
           // Log.d("Latitud",String.valueOf(loc.getLatitude()));
           // Log.d("Longitud",String.valueOf(loc.getLongitude()));
        }


    }

    public void getStockData(){

        String fecha=getFechaNormal(getFechaMilisegundos());

        mfirestore.collection("produccion").document(fecha).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    getDatos[0]=Integer.parseInt(String.valueOf(documentSnapshot.getLong("devolucionesFrijol")));
                    getDatos[1]=Integer.parseInt(String.valueOf(documentSnapshot.getLong("devolucionesFrijolElote")));
                    getDatos[2] =Integer.parseInt(String.valueOf(documentSnapshot.getLong("entregasFrijol")));
                    getDatos[3] =Integer.parseInt(String.valueOf(documentSnapshot.getLong("entregasFrijolElote")));
                    getDatos[4]=Integer.parseInt(String.valueOf(documentSnapshot.getLong("stockFrijol")));
                    getDatos[5]=Integer.parseInt(String.valueOf(documentSnapshot.getLong("stockFrijolElote")));
                    System.out.println("::::>>ARRAY::"+Arrays.toString(getDatos));
                    Actualizar();
                }
            }
        });

    }
    public void Actualizar(){
        System.out.println("DATOS STOCK::::>>>>"+Arrays.toString(getDatos));
        CollectionReference updateData =mfirestore.collection("produccion");
        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("devolucionesFrijol",
                        (getDatos[0]+(Integer.parseInt(datos.getString("devolucionFrijol")))));

        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("devolucionesFrijolElote",
                        (getDatos[1]+(Integer.parseInt(datos.getString("devolucionFrijolElote")))));

        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("entregasFrijol",
                        (getDatos[2]+(Integer.parseInt(datos.getString("entregaFrijol")))));

        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("entregasFrijolElote",
                        (getDatos[3]+(Integer.parseInt(datos.getString("entregaFrijolElote")))));

        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("stockFrijol",
                        (getDatos[4]-(Integer.parseInt(datos.getString("entregaFrijol")))));

        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("stockFrijolElote",
                        (getDatos[5]-(Integer.parseInt(datos.getString("entregaFrijolElote")))));

    }

    public void getCorteData(){
        String fecha=getFechaNormal(getFechaMilisegundos());

        mfirestore.collection("Corte").document(fecha).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    getCorte[0]=Double.parseDouble(String.valueOf(documentSnapshot.getLong("totalVendido")));
                    getCorte[1]=Double.parseDouble(String.valueOf(documentSnapshot.getLong("totalVendidoFrijol")));
                    getCorte[2]=Double.parseDouble(String.valueOf(documentSnapshot.getLong("totalVendidoFrijolElote")));
                    ActualizarCorte();
                }
            }
        });
    }
    public void ActualizarCorte(){
        CollectionReference updateData =mfirestore.collection("Corte");
        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("totalVendido",
                        (getDatos[0]+(Double.parseDouble(datos.getString("total")))));

        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("totalVendidoFrijol",
                        (getDatos[1]+(Double.parseDouble(datos.getString("totalFrijol")))));

        updateData.document(getFechaNormal(getFechaMilisegundos())).
                update("totalVendidoFrijolElote",
                        (getDatos[2]+(Double.parseDouble(datos.getString("totalFrijolElote")))));

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