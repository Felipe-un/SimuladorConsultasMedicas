package com.simulador.model;

public class Paciente extends Persona {

    private String fechaNacimiento;
    private String historialMedicoGeneral; // Podría ser una lista de condiciones, etc.

    public Paciente(String id, String nombre, String telefono, String email, String fechaNacimiento, String historialMedicoGeneral) {
        super(id, nombre, telefono, email);
        this.fechaNacimiento = fechaNacimiento;
        this.historialMedicoGeneral = historialMedicoGeneral;
    }

    // Constructor vacío para Firebase (muy importante para deserialización)
    public Paciente() {
        super("", "", "", ""); // Llama al constructor de Persona con valores predeterminados
    }

    // Getters y Setters específicos de Paciente
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getHistorialMedicoGeneral() {
        return historialMedicoGeneral;
    }

    public void setHistorialMedicoGeneral(String historialMedicoGeneral) {
        this.historialMedicoGeneral = historialMedicoGeneral;
    }

    @Override
    public String getTipoPersona() {
        return "Paciente";
    }

    @Override
    public String toString() {
        return "Paciente " + super.toString() + ", Fecha Nacimiento: " + fechaNacimiento;
    }
}
