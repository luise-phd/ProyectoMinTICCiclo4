package com.ingluise.ProyectoMinTICCiclo4;

public class Producto {
    private String nombre;
    private int precio;

    public Producto() {
        this.nombre = "";
        this.precio = 0;
    }

    public Producto(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
