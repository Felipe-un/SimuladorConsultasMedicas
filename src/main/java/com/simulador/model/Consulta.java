package com.simulador.model;

import java.util.Date;

public class Consulta {

    private String idConsulta;
    private String idPaciente; // Referencia al paciente por ID
    private String idMedico;   // Referencia al médico por ID
    private String fechaConsulta; // Podría ser Date, pero String para simplicidad inicial con Firebase
    private String sintomas;
    private String diagnostico;
    private String tratamiento;

    public Consulta(String idConsulta, String idPaciente, String idMedico, String fechaConsulta, String sintomas, String diagnostico, String tratamiento) {
        this.idConsulta = idConsulta;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaConsulta = fechaConsulta;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    // Constructor vacío para Firebase
    public Consulta() {
    }

    // Getters y Setters
    public String getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(String idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(String idMedico) {
        this.idMedico = idMedico;
    }

    public String getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(String fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    @Override
    public String toString() {
        return "Consulta ID: " + idConsulta + ", Paciente ID: " + idPaciente + ", Medico ID: " + idMedico
                + ", Fecha: " + fechaConsulta + ", Diagnóstico: " + diagnostico;
    }
}
