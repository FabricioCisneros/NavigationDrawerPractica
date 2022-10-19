package com.example.navigationdrawerpractica.Interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.navigationdrawerpractica.Entidades.Persona;
import com.example.navigationdrawerpractica.Fragments.DetallePersonaFragment;
import com.example.navigationdrawerpractica.Fragments.MainFragment;
import com.example.navigationdrawerpractica.Fragments.PersonasFragment;
import com.example.navigationdrawerpractica.R;
import com.google.android.material.navigation.NavigationView;

public class StockActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iComunicaFragments{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    //variable del fragment detalle
    DetallePersonaFragment detallePersonaFragment;
    TextView stockInicialFrijoles, stockInicialFrijolesElote,stockFrijol,stockFrijolElote,
             devolucionesFrijoles, devolucionesFrijolesElote,entregaSinElote,entregaConElote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        //lo sgt se implementa luego de haber implementado NavigationView.OnNavigationItemSelectedListener
        navigationView.setNavigationItemSelectedListener(this);
        ///////////
        stockInicialFrijolesElote=findViewById(R.id.txtInicialFrijolElote);
        stockInicialFrijoles=findViewById(R.id.txtInicialFrijol);
        stockFrijolElote=findViewById(R.id.txtstockConElote);
        stockFrijol=findViewById(R.id.txtstockSinElote);
        devolucionesFrijolesElote=findViewById(R.id.txtDevolucionesElote);
        devolucionesFrijoles=findViewById(R.id.txtDevolucionesSinElote);
        entregaConElote=findViewById(R.id.txtEntregaElote);
        entregaSinElote=findViewById(R.id.txtEntregaSinElote);
        //////////
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar fragment principal en la actividad
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new MainFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void enviarPersona(Persona persona) {
        //gracias a hbaer implementado de la interface "iComunicaFragments" se tiene la implementacion del metodo enviarPersona
        //o mejor dicho este metodo.
        //Aqui se realiza toda la logica necesaria para poder realizar el envio
        detallePersonaFragment = new DetallePersonaFragment();
        //objeto bundle para transportar la informacion
        Bundle bundleEnvio = new Bundle();
        //se manda el objeto que le esta llegando:
        bundleEnvio.putSerializable("objeto",persona);
        detallePersonaFragment.setArguments(bundleEnvio);

        //CArgar fragment en el activity
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, detallePersonaFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //***Luego pasar a programar al fragmentdetalle
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //para cerrar automaticamente el menu
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new MainFragment());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId() == R.id.personas){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new PersonasFragment());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.stock){
            Intent intent= new Intent(this,StockActivity.class);
            startActivity(intent);
        }
        return false;
    }


}