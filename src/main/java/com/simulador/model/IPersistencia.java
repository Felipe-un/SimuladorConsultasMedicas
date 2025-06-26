
package com.simulador.model;

import java.util.List;

// Interfaz para la persistencia de datos
public interface IPersistencia {
    // Métodos para pacientes
    void guardarPaciente(Paciente paciente) throws Exception;
    Paciente cargarPaciente(String id) throws Exception;
    List<Paciente> cargarTodosLosPacientes() throws Exception;
    void eliminarPaciente(String id) throws Exception;

    // Métodos para médicos
    void guardarMedico(Medico medico) throws Exception;
    Medico cargarMedico(String id) throws Exception;
    List<Medico> cargarTodosLosMedicos() throws Exception;
    void eliminarMedico(String id) throws Exception;

    // Métodos para consultas
    void guardarConsulta(Consulta consulta) throws Exception;
    Consulta cargarConsulta(String id) throws Exception;
    List<Consulta> cargarTodasLasConsultas() throws Exception;
    void eliminarConsulta(String id) throws Exception;
}