package com.example.navigationdrawerpractica.Interfaces;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Range;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PagoActivity extends AppCompatActivity {


    Bundle datos;
    private FirebaseFirestore mfirestore;
    public String PagoEfectivoString;
    public String Cambio;
    private LocationManager ubicacion;
    //double LatitudCliente=20.735016785031025,LongitudCliente=-103.41271473536557,LatitudActual,LongitudActual;
    double LatitudCliente=20.73524270248981,LongitudCliente=-103.41223790359507,LatitudActual,LongitudActual;
    Range<Double> rangeLat = Range.open(LatitudCliente-.0002, LatitudCliente+.0002);
    Range<Double> rangeLong = Range.open(LongitudCliente-.0002, LongitudCliente+.0002);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pago_activity);
        datos = getIntent().getExtras();
        mfirestore = FirebaseFirestore.getInstance();
        TextView TotalFrijol = findViewById(R.id.txtTotalFrijol);
        TotalFrijol.setText("$" + datos.getString("TotalFrijol"));
        TextView TotalFrijolElote = findViewById(R.id.txtTotalFrijolElote);
        TotalFrijolElote.setText("$" + datos.getString("TotalFrijolElote"));
        TextView total = findViewById(R.id.txtTotal);
        total.setText("$" + datos.getString("Total"));
        EditText PagoEfectivo = findViewById(R.id.etEfectivo);
        Button btnPago = findViewById(R.id.btnPago);


        Toast.makeText(this, "Fecha del Pedido " + datos.getString("FechaPedido"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Nombre de la tienda " + datos.getString("NombreCliente"), Toast.LENGTH_SHORT).show();

        btnPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PagoEfectivoString = PagoEfectivo.getText().toString();
                if (PagoEfectivoString.isEmpty()) {
                    Toast.makeText(PagoActivity.this, "Ingresa el efectivo", Toast.LENGTH_SHORT).show();
                } else {
                    Cambio = Cambio(datos.getString("Total"), PagoEfectivoString);
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
        detalleVenta.put("Cliente", datos.getString("NombreCliente"));
        detalleVenta.put("frijolesEntrega", Integer.parseInt(datos.getString("EntregaFrijol")));
        detalleVenta.put("frijolesEloteEntrega", Integer.parseInt(datos.getString("EntregaFrijolElote")));
        detalleVenta.put("frijolesDevolucion", Integer.parseInt(datos.getString("DevolucionFrijol")));
        detalleVenta.put("frijolesEloteDevolucion", Integer.parseInt(datos.getString("DevolucionFrijolElote")));
        detalleVenta.put("frijolesSobrantes", Integer.parseInt(datos.getString("SobranteFrijol")));
        detalleVenta.put("frijolesEloteSobrantes", Integer.parseInt(datos.getString("SobranteFrijolElote")));
        detalleVenta.put("TotalFrijol", Integer.parseInt(datos.getString("TotalFrijol")));
        detalleVenta.put("TotalFrijolElote", Integer.parseInt(datos.getString("TotalFrijolElote")));
        detalleVenta.put("fecha", datos.getString("DetalleFechaPedido"));
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

        detalleVenta.put("EfectivoRecibido", Double.parseDouble(PagoEfectivoString));
        detalleVenta.put("Cambio", Double.parseDouble(Cambio));
        detalleVenta.put("Total", Double.parseDouble(datos.getString("Total")));
        pedido.put(datos.getString("NombreCliente"), detalleVenta);

        venta.document(datos.getString("FechaPedido")).set(pedido, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Conexion exitosa con la base de datos", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(PagoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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
            LatitudActual=loc.getLatitude();
            LongitudActual=loc.getLongitude();
            System.out.println("Lat:"+LatitudActual);
            System.out.println("Lat:"+LongitudActual);
            if(rangeLat.contains(LatitudActual) && rangeLong.contains(LongitudActual)){
                System.out.println(rangeLat.contains(LatitudActual));
                System.out.println(rangeLat.contains(LongitudActual));
                Toast.makeText(this, "dentro del rango", Toast.LENGTH_SHORT).show();
                InsertarDatos();
            }else{
                Toast.makeText(this, "no estas dentro del rango", Toast.LENGTH_SHORT).show();
            }
           // Log.d("Latitud",String.valueOf(loc.getLatitude()));
           // Log.d("Longitud",String.valueOf(loc.getLongitude()));
        }


    }

}