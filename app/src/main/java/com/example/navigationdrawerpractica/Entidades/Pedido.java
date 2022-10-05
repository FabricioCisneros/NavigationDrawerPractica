package com.example.navigationdrawerpractica.Entidades;

public class Pedido {
    String EntregaElote;

    public String getEntregaSinELote() {
        return EntregaSinELote;
    }

    public void setEntregaSinELote(String entregaSinELote) {
        EntregaSinELote = entregaSinELote;
    }

    public String getDevolucionElote() {
        return DevolucionElote;
    }

    public void setDevolucionElote(String devolucionElote) {
        DevolucionElote = devolucionElote;
    }

    public String getDevolucionSinElote() {
        return DevolucionSinElote;
    }

    public void setDevolucionSinElote(String devolucionSinElote) {
        DevolucionSinElote = devolucionSinElote;
    }

    public String getInventarioLunes() {
        return inventarioLunes;
    }

    public void setInventarioLunes(String inventarioLunes) {
        this.inventarioLunes = inventarioLunes;
    }

    public String getInventarioMartes() {
        return inventarioMartes;
    }

    public void setInventarioMartes(String inventarioMartes) {
        this.inventarioMartes = inventarioMartes;
    }

    public String getInventarioMiercoles() {
        return inventarioMiercoles;
    }

    public void setInventarioMiercoles(String inventarioMiercoles) {
        this.inventarioMiercoles = inventarioMiercoles;
    }

    public String getInventarioJueves() {
        return inventarioJueves;
    }

    public void setInventarioJueves(String inventarioJueves) {
        this.inventarioJueves = inventarioJueves;
    }

    public String getInventarioViernes() {
        return inventarioViernes;
    }

    public void setInventarioViernes(String inventarioViernes) {
        this.inventarioViernes = inventarioViernes;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    String EntregaSinELote;
    String DevolucionElote;
    String DevolucionSinElote;
    String inventarioLunes;
    String inventarioMartes;
    String inventarioMiercoles;
    String inventarioJueves;
    String inventarioViernes;
    String fecha;
    String Cliente;

    public String getTotalFrijoles() {
        return totalFrijoles;
    }

    public void setTotalFrijoles(String totalFrijoles) {
        this.totalFrijoles = totalFrijoles;
    }

    public String getTotalFrijolesElote() {
        return totalFrijolesElote;
    }

    public void setTotalFrijolesElote(String totalFrijolesElote) {
        this.totalFrijolesElote = totalFrijolesElote;
    }

    String totalFrijoles;
    String totalFrijolesElote;

    public String getEntregaElote() {
        return EntregaElote;
    }

    public void setEntregaElote(String entregaElote) {
        EntregaElote = entregaElote;
    }

    public String getPrecioFrijoles() {
        return precioFrijoles;
    }

    public void setPrecioFrijoles(String precioFrijoles) {
        this.precioFrijoles = precioFrijoles;
    }

    public String getPrecioFrijolesElote() {
        return precioFrijolesElote;
    }

    public void setPrecioFrijolesElote(String precioFrijolesElote) {
        this.precioFrijolesElote = precioFrijolesElote;
    }

    String precioFrijoles;
    String precioFrijolesElote;

    public String getTotalCobro() {
        return TotalCobro;
    }

    public void setTotalCobro(String totalCobro) {
        TotalCobro = totalCobro;
    }

    String TotalCobro;
}
