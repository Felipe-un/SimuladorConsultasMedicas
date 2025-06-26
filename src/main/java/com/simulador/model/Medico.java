package com.simulador.model;

public class Medico extends Persona {

    private String especialidad;
    private String licenciaMedica;

    public Medico(String id, String nombre, String telefono, String email, String especialidad, String licenciaMedica) {
        super(id, nombre, telefono, email);
        this.especialidad = especialidad;
        this.licenciaMedica = licenciaMedica;
    }

    // Constructor vacío para Firebase
    public Medico() {
        super("", "", "", ""); // Llama al constructor de Persona con valores predeterminados
    }

    // Getters y Setters específicos de Medico
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getLicenciaMedica() {
        return licenciaMedica;
    }

    public void setLicenciaMedica(String licenciaMedica) {
        this.licenciaMedica = licenciaMedica;
    }

    @Override
    public String getTipoPersona() {
        return "Medico";
    }

    @Override
    public String toString() {
        return "Medico " + super.toString() + ", Especialidad: " + especialidad;
    }
}
