package com.example.pokemongo;

public class Pokemon {
    private String nombre;
    private String tipo;
    private String estado;
    private String localizacion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public Pokemon(String nombre, String tipo, String estado, String localizacion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.localizacion = localizacion;
    }

    public Pokemon(){

    }

    @Override
    public String toString() {
        return nombre + " | " + tipo + " | " + estado + " | " + localizacion;
    }
}
