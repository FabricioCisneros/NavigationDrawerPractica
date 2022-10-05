package com.example.navigationdrawerpractica.Interfaces;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.os.Bundle;
import android.widget.Toast;

import com.example.navigationdrawerpractica.Entidades.Pedido;
import com.example.navigationdrawerpractica.R;

public class PagoActivity extends AppCompatActivity {

    Pedido p = new Pedido();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pago_activity);
        Toast.makeText(this, " total: "+p.getTotalFrijoles(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "total: "+p.getTotalFrijolesElote(), Toast.LENGTH_SHORT).show();
    }

}