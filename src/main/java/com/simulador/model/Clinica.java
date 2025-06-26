package com.simulador.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Para generar IDs únicos si no vienen de Firebase

public class Clinica {

    private List<Paciente> pacientes;
    private List<Medico> medicos;
    private List<Consulta> consultas;

    // Aunque la persistencia se manejará en una clase aparte,
    // Clinica podría tener una referencia a la interfaz de persistencia
    // para coordinar las operaciones del modelo.
    private IPersistencia persistenciaService;

    public Clinica() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.consultas = new ArrayList<>();
    }

    // Setter para la persistencia (se inyectará desde Main o ViewModel)
    public void setPersistenciaService(IPersistencia persistenciaService) {
        this.persistenciaService = persistenciaService;
    }

    // Métodos para cargar datos iniciales (usando el servicio de persistencia)
    public void cargarDatosIniciales() throws Exception {
        if (persistenciaService != null) {
            this.pacientes = persistenciaService.cargarTodosLosPacientes();
            this.medicos = persistenciaService.cargarTodosLosMedicos();
            this.consultas = persistenciaService.cargarTodasLasConsultas();
            System.out.println("Datos cargados exitosamente desde Firebase.");
        } else {
            System.err.println("Servicio de persistencia no configurado.");
        }
    }

    // 1. Métodos para registrar nuevos pacientes y médicos [cite: 3]
    public void registrarPaciente(Paciente paciente) throws Exception {
        // Validación de campos vacíos (ejemplo, se mejorará con excepciones personalizadas)
        if (paciente.getId().isEmpty() || paciente.getNombre().isEmpty() || paciente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Campos de paciente incompletos.");
        }
        // Verificar si el paciente ya existe
        if (pacientes.stream().anyMatch(p -> p.getId().equals(paciente.getId()))) {
            throw new IllegalArgumentException("Paciente con ID " + paciente.getId() + " ya existe.");
        }
        pacientes.add(paciente);
        if (persistenciaService != null) {
            persistenciaService.guardarPaciente(paciente);
        }
    }

    public void registrarMedico(Medico medico) throws Exception {
        // Validación de campos vacíos
        if (medico.getId().isEmpty() || medico.getNombre().isEmpty() || medico.getEspecialidad().isEmpty()) {
            throw new IllegalArgumentException("Campos de médico incompletos.");
        }
        // Verificar si el médico ya existe
        if (medicos.stream().anyMatch(m -> m.getId().equals(medico.getId()))) {
            throw new IllegalArgumentException("Médico con ID " + medico.getId() + " ya existe.");
        }
        medicos.add(medico);
        if (persistenciaService != null) {
            persistenciaService.guardarMedico(medico);
        }
    }

    // 2. Método para asignar una consulta [cite: 4]
    public Consulta asignarConsulta(String idPaciente, String idMedico, String sintomas, String diagnostico, String tratamiento) throws Exception {
        Paciente paciente = buscarPacientePorId(idPaciente);
        Medico medico = buscarMedicoPorId(idMedico);

        if (paciente == null) {
            throw new IllegalArgumentException("Paciente con ID " + idPaciente + " no encontrado.");
        }
        if (medico == null) {
            throw new IllegalArgumentException("Médico con ID " + idMedico + " no encontrado.");
        }
        if (sintomas.isEmpty() || diagnostico.isEmpty() || tratamiento.isEmpty()) {
            throw new IllegalArgumentException("Campos de la consulta incompletos.");
        }

        String idConsulta = UUID.randomUUID().toString(); // Generar ID único para la consulta
        String fechaActual = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        Consulta nuevaConsulta = new Consulta(idConsulta, idPaciente, idMedico, fechaActual, sintomas, diagnostico, tratamiento);
        consultas.add(nuevaConsulta);

        if (persistenciaService != null) {
            persistenciaService.guardarConsulta(nuevaConsulta);
        }
        return nuevaConsulta;
    }

    // 3. Método para consultar el historial médico de un paciente [cite: 5]
    public List<Consulta> consultarHistorialPaciente(String idPaciente) {
        List<Consulta> historial = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getIdPaciente().equals(idPaciente)) {
                historial.add(consulta);
            }
        }
        return historial;
    }

    // 4. Método para listar todas las consultas realizadas por un médico [cite: 5]
    public List<Consulta> listarConsultasPorMedico(String idMedico) {
        List<Consulta> consultasMedico = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getIdMedico().equals(idMedico)) {
                consultasMedico.add(consulta);
            }
        }
        return consultasMedico;
    }

    // Métodos de búsqueda auxiliares
    public Paciente buscarPacientePorId(String id) {
        return pacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Medico buscarMedicoPorId(String id) {
        return medicos.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Paciente> getPacientes() {
        return new ArrayList<>(pacientes); // Devuelve una copia para evitar modificación externa directa
    }

    public List<Medico> getMedicos() {
        return new ArrayList<>(medicos); // Devuelve una copia
    }

    public List<Consulta> getConsultas() {
        return new ArrayList<>(consultas); // Devuelve una copia
    }
}
