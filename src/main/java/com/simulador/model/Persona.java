package com.simulador.model;

public abstract class Persona {

    protected String id;
    protected String nombre;
    protected String telefono;
    protected String email;

    public Persona(String id, String nombre, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método abstracto (ejemplo, puedes añadir más según necesidad)
    public abstract String getTipoPersona();

    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombre;
    }
}
