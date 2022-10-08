package com.example.navigationdrawerpractica.Entidades;

import java.io.Serializable;

public class Persona implements Serializable {
    private String nombre;
    private String fechanacimiento;
    private int imagenid;



    public Persona(String nombre, String fechanacimiento, int imagenid) {
        this.nombre = nombre;
        this.fechanacimiento = fechanacimiento;
        this.imagenid = imagenid;
    }

    public String getNombre() {
        return nombre;
    }


    public String getFechanacimiento() {
        return fechanacimiento;
    }


    public int getImagenid() {
        return imagenid;
    }


}
