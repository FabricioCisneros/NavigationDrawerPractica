package com.example.navigationdrawerpractica.Entidades;

import java.io.Serializable;

public class Persona implements Serializable {
    private String nombre;
    private String direction;
    private Long lat;
    private Long longitud;
    private boolean entregable;
    private int TamCollection;
    private int index;

    public Persona(){

    }

    public Persona(String nombre, String direction, Long lat, Long longitud, boolean entregable,
                   int TamCollection,int index) {
        this.nombre = nombre;
        this.direction = direction;
        this.lat = lat;
        this.longitud = longitud;
        this.entregable = entregable;
        this.TamCollection=TamCollection;
        this.index=index;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirection() {
        return direction;
    }

    public void setDireccion(String direccion) {
        this.direction = direction;
    }

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Long getLongitud() {
        return longitud;
    }

    public void setLongitud(Long longitud) {
        this.longitud = longitud;
    }

    public boolean isEntregable() {
        return entregable;
    }

    public int getTamCollection() {
        return TamCollection;
    }

    public void setTamCollection(int tamCollection) {
        TamCollection = tamCollection;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setEntregable(boolean entregable) {
        this.entregable = entregable;
    }
}
