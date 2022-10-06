package com.example.navigationdrawerpractica.Interfaces;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationdrawerpractica.Entidades.Pedido;
import com.example.navigationdrawerpractica.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pago_activity);
        datos=getIntent().getExtras();
        mfirestore=FirebaseFirestore.getInstance();
        TextView TotalFrijol =findViewById(R.id.txtTotalFrijol);
        TotalFrijol.setText("$"+datos.getString("TotalFrijol"));
        TextView TotalFrijolElote=findViewById(R.id.txtTotalFrijolElote);
        TotalFrijolElote.setText("$"+datos.getString("TotalFrijolElote"));
        TextView total=findViewById(R.id.txtTotal);
        total.setText("$"+datos.getString("Total"));
        EditText PagoEfectivo =findViewById(R.id.etEfectivo);

        Button btnPago = findViewById(R.id.btnPago);


        Toast.makeText(this, "Fecha del Pedido "+datos.getString("FechaPedido"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Nombre de la tienda "+datos.getString("NombreCliente"), Toast.LENGTH_SHORT).show();
        btnPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PagoEfectivoString=PagoEfectivo.getText().toString();
                if(PagoEfectivoString.isEmpty()){
                    Toast.makeText(PagoActivity.this, "Ingresa el efectivo", Toast.LENGTH_SHORT).show();
                }else{
                    Cambio=Cambio(datos.getString("Total"),PagoEfectivoString);
                    if(Integer.parseInt(Cambio)<0){
                        Toast.makeText(PagoActivity.this, "Entrega de efectivo Invalida", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PagoActivity.this, "Procesando pago", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder altdial= new AlertDialog.Builder(PagoActivity.this);
                        altdial.setMessage("Pedido listo devuelva $"+Cambio+" al cliente").setCancelable(false).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                InsertarDatos();
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

    public void InsertarDatos(){
        CollectionReference venta = mfirestore.collection("ventas");
        Map<String,Object>pedido = new HashMap<>();
        Map<String,Object>detalleVenta = new HashMap<>();
        detalleVenta.put("Cliente",datos.getString("NombreCliente"));
            Map<String,Object>detalleEntrega=new HashMap<>();
                detalleEntrega.put("frijoles",datos.getString("EntregaFrijol"));
                detalleEntrega.put("frijolesElote",datos.getString("EntregaFrijolElote"));
                detalleVenta.put("Entregas",detalleEntrega);
            Map<String,Object>detalleDevolucion=new HashMap<>();
                detalleDevolucion.put("frijoles",datos.getString("DevolucionFrijol"));
                detalleDevolucion.put("frijolesElote",datos.getString("DevolucionFrijolElote"));
                detalleVenta.put("Devoluciones",detalleDevolucion);
            detalleEntrega.put("TotalFrijol",datos.getString("TotalFrijol"));
            detalleEntrega.put("TotalFrijolElote",datos.getString("TotalFrijolElote"));
            detalleEntrega.put("fecha",datos.getString("DetalleFechaPedido"));
            Map<String,Object>Inventario=new HashMap<>();
                Inventario.put("lunes", datos.getString("LunesInventario"));
                Inventario.put("martes",datos.getString("MartesInventario"));
                Inventario.put("miercoles",datos.getString("MiercolesInvenario"));
                Inventario.put("jueves", datos.getString("JuevesInventario"));
                Inventario.put("viernes",datos.getString("ViernesInventario"));
            detalleEntrega.put("inventarioDia",Inventario);
            detalleEntrega.put("EfectivoRecibido",Double.parseDouble(PagoEfectivoString));
            detalleEntrega.put("Cambio",Double.parseDouble(Cambio));
            detalleEntrega.put("Total",Double.parseDouble(datos.getString("Total")));
        pedido.put(datos.getString("NombreCliente"),detalleVenta);

        venta.document(datos.getString("FechaPedido")).set(pedido, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Conexion exitosa con la base de datos", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent= new Intent(PagoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public String Cambio(String total, String EfectivoRecibido){
        String Cambio;
        int calculoCambio=(Integer.parseInt(EfectivoRecibido)-(Integer.parseInt(total)));
        Cambio=String.valueOf(calculoCambio);
        return Cambio;
    }

}