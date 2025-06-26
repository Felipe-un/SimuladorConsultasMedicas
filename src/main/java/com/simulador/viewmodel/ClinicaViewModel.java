package com.simulador.viewmodel;

// Paquete model
import com.simulador.model.Clinica;
import com.simulador.model.Consulta;
import com.simulador.model.Medico;
import com.simulador.model.Paciente;
import com.simulador.model.IPersistencia;

// Paquete persistencia
import com.simulador.persistencia.PersistenciaFirebase; // Importamos nuestra implementación de persistencia

// Paquete excepciones
import com.simulador.excepciones.UsuarioNoEncontradoException;
import com.simulador.excepciones.CampoVacioException;

import java.util.List;

import java.util.ArrayList; // Necesario para la lista mutable que devuelve el ViewModel

public class ClinicaViewModel {

    private Clinica clinicaModel; // La instancia de nuestro modelo de negocio

    // Constructor que recibe el servicio de persistencia
    public ClinicaViewModel() {
        this.clinicaModel = new Clinica();
        // Inyectamos la dependencia de persistencia al modelo
        this.clinicaModel.setPersistenciaService(new PersistenciaFirebase());
        try {
            // Cargamos los datos al iniciar el ViewModel
            this.clinicaModel.cargarDatosIniciales();
        } catch (Exception e) {
            System.err.println("Error al cargar datos iniciales en ViewModel: " + e.getMessage());
            // En una aplicación real, esto se mostraría en la UI
        }
    }

    // --- Métodos para el Login de usuario (Requisito funcional 1) ---
    // Aunque no tenemos un sistema de autenticación completo, podemos simular
    // un "login" verificando si el ID existe en pacientes o médicos.
    // Esto se podría expandir a tener roles (médico/administrativo).
    // --- Métodos para el Login de usuario (Requisito funcional 1) ---
    // Ahora, este método solo permitirá el "login" a Médicos (considerados administrativos)
    // Los pacientes serán rechazados si intentan usar esta función de login.
    public String login(String idUsuario) throws UsuarioNoEncontradoException, CampoVacioException {
        try {
            if (idUsuario == null || idUsuario.trim().isEmpty()) {
                throw new CampoVacioException("El ID de usuario no puede estar vacío.");
            }

            // Primero, buscar si es un médico
            Medico medico = clinicaModel.buscarMedicoPorId(idUsuario);
            if (medico != null) {
                // Si es un médico, se le permite el acceso y se considera un rol "administrativo"
                return "Medico";
            }

            // Segundo, buscar si es un paciente. Si es un paciente, no se le permite acceder a esta interfaz.
            Paciente paciente = clinicaModel.buscarPacientePorId(idUsuario);
            if (paciente != null) {
                // Si el ID corresponde a un paciente, lanzamos una excepción específica
                throw new UsuarioNoEncontradoException("Los pacientes no tienen acceso a esta interfaz. Por favor, inicie sesión con un ID de médico/administrador.");
            }

            // Si no se encuentra ni como médico ni como paciente
            throw new UsuarioNoEncontradoException("Usuario con ID " + idUsuario + " no encontrado o no autorizado.");

        } catch (CampoVacioException e) {
            throw e; // Relanzar CampoVacioException
        } catch (UsuarioNoEncontradoException e) {
            throw e; // Relanzar la excepción de usuario no encontrado/no autorizado
        } catch (Exception e) {
            // Capturar otras excepciones inesperadas
            throw new UsuarioNoEncontradoException("Error inesperado al intentar iniciar sesión: " + e.getMessage());
        }
    }

    // --- Métodos para Registrar nuevos pacientes y médicos (Requisito funcional 2) ---
    public void registrarPaciente(String id, String nombre, String telefono, String email, String fechaNacimiento, String historialMedicoGeneral) throws CampoVacioException, Exception {
        if (id.trim().isEmpty() || nombre.trim().isEmpty() || telefono.trim().isEmpty() || email.trim().isEmpty() || fechaNacimiento.trim().isEmpty()) {
            throw new CampoVacioException("Todos los campos obligatorios del paciente deben ser llenados.");
        }
        Paciente nuevoPaciente = new Paciente(id, nombre, telefono, email, fechaNacimiento, historialMedicoGeneral);
        try {
            clinicaModel.registrarPaciente(nuevoPaciente);
        } catch (IllegalArgumentException e) {
            // Capturar la excepción del modelo y relanzar con un mensaje más específico
            throw new Exception("Error al registrar paciente: " + e.getMessage());
        }
    }

    public void registrarMedico(String id, String nombre, String telefono, String email, String especialidad, String licenciaMedica) throws CampoVacioException, Exception {
        if (id.trim().isEmpty() || nombre.trim().isEmpty() || telefono.trim().isEmpty() || email.trim().isEmpty() || especialidad.trim().isEmpty() || licenciaMedica.trim().isEmpty()) {
            throw new CampoVacioException("Todos los campos obligatorios del médico deben ser llenados.");
        }
        Medico nuevoMedico = new Medico(id, nombre, telefono, email, especialidad, licenciaMedica);
        try {
            clinicaModel.registrarMedico(nuevoMedico);
        } catch (IllegalArgumentException e) {
            throw new Exception("Error al registrar médico: " + e.getMessage());
        }
    }

    // --- Método para asignar una consulta (Requisito funcional 3) ---
    public Consulta asignarConsulta(String idPaciente, String idMedico, String sintomas, String diagnostico, String tratamiento) throws CampoVacioException, UsuarioNoEncontradoException, Exception {
        if (idPaciente.trim().isEmpty() || idMedico.trim().isEmpty() || sintomas.trim().isEmpty() || diagnostico.trim().isEmpty() || tratamiento.trim().isEmpty()) {
            throw new CampoVacioException("Todos los campos de la consulta deben ser llenados.");
        }
        try {
            return clinicaModel.asignarConsulta(idPaciente, idMedico, sintomas, diagnostico, tratamiento);
        } catch (IllegalArgumentException e) {
            // Podríamos distinguir si el error es por paciente/médico no encontrado o campos incompletos
            if (e.getMessage().contains("Paciente con ID") || e.getMessage().contains("Médico con ID")) {
                throw new UsuarioNoEncontradoException(e.getMessage());
            } else {
                throw new Exception("Error al asignar consulta: " + e.getMessage());
            }
        }
    }

    // --- Método para consultar el historial médico de un paciente (Requisito funcional 4) ---
    public List<Consulta> consultarHistorialPaciente(String idPaciente) throws UsuarioNoEncontradoException, CampoVacioException {
        if (idPaciente.trim().isEmpty()) {
            throw new CampoVacioException("El ID del paciente no puede estar vacío para consultar su historial.");
        }
        if (clinicaModel.buscarPacientePorId(idPaciente) == null) {
            throw new UsuarioNoEncontradoException("Paciente con ID " + idPaciente + " no encontrado.");
        }
        return new ArrayList<>(clinicaModel.consultarHistorialPaciente(idPaciente)); // Devuelve una copia
    }

    // --- Método para listar todas las consultas realizadas por un médico (Requisito funcional 5) ---
    public List<Consulta> listarConsultasPorMedico(String idMedico) throws UsuarioNoEncontradoException, CampoVacioException {
        if (idMedico.trim().isEmpty()) {
            throw new CampoVacioException("El ID del médico no puede estar vacío para listar sus consultas.");
        }
        if (clinicaModel.buscarMedicoPorId(idMedico) == null) {
            throw new UsuarioNoEncontradoException("Médico con ID " + idMedico + " no encontrado.");
        }
        return new ArrayList<>(clinicaModel.listarConsultasPorMedico(idMedico)); // Devuelve una copia
    }

    // Métodos para obtener listas de objetos para la UI (ej. para ComboBoxes o tablas)
    public List<Paciente> getListaPacientes() {
        return new ArrayList<>(clinicaModel.getPacientes()); // Devuelve una copia
    }

    public List<Medico> getListaMedicos() {
        return new ArrayList<>(clinicaModel.getMedicos()); // Devuelve una copia
    }

    public List<Consulta> getListaConsultas() {
        return new ArrayList<>(clinicaModel.getConsultas()); // Devuelve una copia
    }

    // Métodos para buscar Paciente/Medico por ID (útil para validaciones en la UI o para obtener detalles)
    public Paciente buscarPacientePorId(String id) {
        return clinicaModel.buscarPacientePorId(id);
    }

    public Medico buscarMedicoPorId(String id) {
        return clinicaModel.buscarMedicoPorId(id);
    }
}
