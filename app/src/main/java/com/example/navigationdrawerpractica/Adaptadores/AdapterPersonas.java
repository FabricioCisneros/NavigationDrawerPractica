package com.example.navigationdrawerpractica.Adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationdrawerpractica.Entidades.Persona;
import com.example.navigationdrawerpractica.Fragments.DetallePersonaFragment;
import com.example.navigationdrawerpractica.Interfaces.MainActivity;
import com.example.navigationdrawerpractica.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterPersonas extends FirestoreRecyclerAdapter<Persona, AdapterPersonas.ViewHolder> {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String dayOfWeek;
    Persona persona;
    public int cont=0;

    FragmentManager fm;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterPersonas(@NonNull FirestoreRecyclerOptions<Persona> options,FragmentManager fm) {
        super(options);

        this.fm=fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Persona model) {
        cont++;
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getBindingAdapterPosition());
        AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
        alpha.setDuration(0); // Make animation instant
        alpha.setFillAfter(true);
        final String id= documentSnapshot.getId();
        holder.Nombre.setText(model.getNombre());
        holder.direccion.setText(model.getDirection());
        holder.latitud.setText(String.valueOf(model.getLat()));
        holder.longitud.setText(String.valueOf(model.getLongitud()));
        holder.entregable.setText(String.valueOf(model.isEntregable()));
        holder.index.setText(String.valueOf(cont));
        if(model.isEntregable()!=true){
            holder.ic_pedido.setVisibility(View.INVISIBLE);
            holder.card.setAnimation(alpha);
        }
        holder.ic_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cont!=Integer.parseInt(holder.index.getText().toString())){
                    if(model.isEntregable()) {
                        DocumentSnapshot documentSnapshot2 = getSnapshots().getSnapshot((holder.getPosition())+ 1);
                        final String id2 = documentSnapshot2.getId();
                        Toast.makeText(holder.card.getContext(), "pisicion " + id2, Toast.LENGTH_SHORT).show();
                        DetallePersonaFragment detallePersonaFragment = new DetallePersonaFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("id_persona", id);
                        bundle.putString("id_sigPersona",id2);
                        detallePersonaFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.container_fragment, detallePersonaFragment).commit();
                    }
                }else{
                    if(model.isEntregable()) {
                        DetallePersonaFragment detallePersonaFragment = new DetallePersonaFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("id_persona", id);
                        bundle.putString("id_sigPersona","end");
                        detallePersonaFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.container_fragment, detallePersonaFragment).commit();
                    }
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_personas, parent, false);
        persona=new Persona();
        getDayOfWeek();
        dayOfWeek=quitaDiacriticos(dayOfWeek);

        return new ViewHolder(view);
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        RelativeLayout card;
        TextView Nombre,direccion,latitud,longitud,entregable,index;
        ImageView ic_pedido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Nombre =itemView.findViewById(R.id.nombres);
            direccion=itemView.findViewById(R.id.fechanacimiento);
            latitud=itemView.findViewById(R.id.latitud);
            longitud=itemView.findViewById(R.id.longitud);
            entregable=itemView.findViewById(R.id.entregable);
            ic_pedido=itemView.findViewById(R.id.ic_pedido);
            card=itemView.findViewById(R.id.card);
            index=itemView.findViewById(R.id.index);

        }
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
