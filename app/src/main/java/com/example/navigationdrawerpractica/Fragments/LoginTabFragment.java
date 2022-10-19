package com.example.navigationdrawerpractica.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigationdrawerpractica.Interfaces.MainActivity;
import com.example.navigationdrawerpractica.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

public class LoginTabFragment extends Fragment {
    EditText email,password;
    Button btnLogin;
    FirebaseAuth firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root =(ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        email=root.findViewById(R.id.email);
        password=root.findViewById(R.id.pass);
        btnLogin=root.findViewById(R.id.btn_login);
        firebaseAuth=FirebaseAuth.getInstance();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser=email.getText().toString().trim();
                String passUser=password.getText().toString().trim();

                if(emailUser.isEmpty()){
                    Toast.makeText(getContext(), "Ingresa la cuenta de correo", Toast.LENGTH_SHORT).show();
                }else{
                    if(passUser.isEmpty()){
                        Toast.makeText(getContext(), "Ingresa la contraseña", Toast.LENGTH_SHORT).show();
                    }else{
                        LoginUser(emailUser,passUser);
                    }
                }
            }
        });
        return root;
    }
    private void LoginUser(String emailUser, String passUser) {
        firebaseAuth.signInWithEmailAndPassword(emailUser,passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }else{
                    Toast.makeText(getContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "error al iniciar sesion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
