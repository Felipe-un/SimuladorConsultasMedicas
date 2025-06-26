
package com.simulador.persistencia;

// Firebase
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Paquete model
import com.simulador.model.Consulta;
import com.simulador.model.IPersistencia;
import com.simulador.model.Medico;
import com.simulador.model.Paciente;

// Paquete excepciones
import com.simulador.excepciones.UsuarioNoEncontradoException; // Se usarán más adelante
import com.simulador.excepciones.CampoVacioException; // Se usarán más adelante

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch; // Para esperar operaciones asíncronas

public class PersistenciaFirebase implements IPersistencia {

    private DatabaseReference database;
    private static final String DATABASE_URL = "https://simuladorgestionconsultas-default-rtdb.firebaseio.com/"; // ¡Reemplaza con la URL de tu base de datos Firebase!

    public PersistenciaFirebase() {
        try {
            // Cargar el archivo de credenciales de Firebase
            FileInputStream serviceAccount =
                    new FileInputStream("C:\\Users\\pipe_\\OneDrive\\Documentos\\NetBeansProjects\\SimuladorGestionConsultasMedicas\\SimuladorGestionConsultasMedicas\\src\\simuladorgestionconsultas-firebase-adminsdk-fbsvc-59db2dce2f.json"); // Asegúrate de que esta ruta sea correcta

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();

            // Inicializar Firebase si aún no ha sido inicializado
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            database = FirebaseDatabase.getInstance().getReference();
            System.out.println("Conexión a Firebase establecida exitosamente.");

        } catch (IOException e) {
            System.err.println("ERROR: No se pudo cargar el archivo de credenciales de Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos para pacientes
    @Override
    public void guardarPaciente(Paciente paciente) throws Exception {
        if (paciente.getId() == null || paciente.getId().isEmpty()) {
            throw new CampoVacioException("El ID del paciente no puede estar vacío para guardar en Firebase.");
        }
        DatabaseReference pacientesRef = database.child("pacientes").child(paciente.getId());
        CountDownLatch latch = new CountDownLatch(1);
        pacientesRef.setValue(paciente, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.err.println("Error al guardar paciente: " + databaseError.getMessage());
            } else {
                System.out.println("Paciente " + paciente.getNombre() + " guardado exitosamente en Firebase.");
            }
            latch.countDown();
        });
        latch.await(); // Espera a que la operación asíncrona termine
    }

    @Override
    public Paciente cargarPaciente(String id) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Paciente[] pacienteHolder = {null};
        database.child("pacientes").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pacienteHolder[0] = dataSnapshot.getValue(Paciente.class);
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al cargar paciente: " + databaseError.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        if (pacienteHolder[0] == null) {
            throw new UsuarioNoEncontradoException("Paciente con ID " + id + " no encontrado en Firebase.");
        }
        return pacienteHolder[0];
    }

    @Override
    public List<Paciente> cargarTodosLosPacientes() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        List<Paciente> pacientes = new ArrayList<>();
        database.child("pacientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Paciente paciente = snapshot.getValue(Paciente.class);
                    if (paciente != null) {
                        pacientes.add(paciente);
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al cargar todos los pacientes: " + databaseError.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        return pacientes;
    }

    @Override
    public void eliminarPaciente(String id) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        database.child("pacientes").child(id).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.err.println("Error al eliminar paciente: " + databaseError.getMessage());
            } else {
                System.out.println("Paciente con ID " + id + " eliminado exitosamente de Firebase.");
            }
            latch.countDown();
        });
        latch.await();
    }

    // Métodos para médicos (similar a pacientes)
    @Override
    public void guardarMedico(Medico medico) throws Exception {
        if (medico.getId() == null || medico.getId().isEmpty()) {
            throw new CampoVacioException("El ID del médico no puede estar vacío para guardar en Firebase.");
        }
        DatabaseReference medicosRef = database.child("medicos").child(medico.getId());
        CountDownLatch latch = new CountDownLatch(1);
        medicosRef.setValue(medico, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.err.println("Error al guardar médico: " + databaseError.getMessage());
            } else {
                System.out.println("Médico " + medico.getNombre() + " guardado exitosamente en Firebase.");
            }
            latch.countDown();
        });
        latch.await();
    }

    @Override
    public Medico cargarMedico(String id) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Medico[] medicoHolder = {null};
        database.child("medicos").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicoHolder[0] = dataSnapshot.getValue(Medico.class);
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al cargar médico: " + databaseError.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        if (medicoHolder[0] == null) {
            throw new UsuarioNoEncontradoException("Médico con ID " + id + " no encontrado en Firebase.");
        }
        return medicoHolder[0];
    }

    @Override
    public List<Medico> cargarTodosLosMedicos() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        List<Medico> medicos = new ArrayList<>();
        database.child("medicos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medico medico = snapshot.getValue(Medico.class);
                    if (medico != null) {
                        medicos.add(medico);
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al cargar todos los médicos: " + databaseError.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        return medicos;
    }

    @Override
    public void eliminarMedico(String id) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        database.child("medicos").child(id).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.err.println("Error al eliminar médico: " + databaseError.getMessage());
            } else {
                System.out.println("Médico con ID " + id + " eliminado exitosamente de Firebase.");
            }
            latch.countDown();
        });
        latch.await();
    }

    // Métodos para consultas (similar a pacientes y médicos)
    @Override
    public void guardarConsulta(Consulta consulta) throws Exception {
        if (consulta.getIdConsulta() == null || consulta.getIdConsulta().isEmpty()) {
            throw new CampoVacioException("El ID de la consulta no puede estar vacío para guardar en Firebase.");
        }
        DatabaseReference consultasRef = database.child("consultas").child(consulta.getIdConsulta());
        CountDownLatch latch = new CountDownLatch(1);
        consultasRef.setValue(consulta, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.err.println("Error al guardar consulta: " + databaseError.getMessage());
            } else {
                System.out.println("Consulta " + consulta.getIdConsulta() + " guardada exitosamente en Firebase.");
            }
            latch.countDown();
        });
        latch.await();
    }

    @Override
    public Consulta cargarConsulta(String id) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Consulta[] consultaHolder = {null};
        database.child("consultas").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consultaHolder[0] = dataSnapshot.getValue(Consulta.class);
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al cargar consulta: " + databaseError.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        if (consultaHolder[0] == null) {
            throw new UsuarioNoEncontradoException("Consulta con ID " + id + " no encontrada en Firebase.");
        }
        return consultaHolder[0];
    }

    @Override
    public List<Consulta> cargarTodasLasConsultas() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        List<Consulta> consultas = new ArrayList<>();
        database.child("consultas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Consulta consulta = snapshot.getValue(Consulta.class);
                    if (consulta != null) {
                        consultas.add(consulta);
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al cargar todas las consultas: " + databaseError.getMessage());
                latch.countDown();
            }
        });
        latch.await();
        return consultas;
    }

    @Override
    public void eliminarConsulta(String id) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        database.child("consultas").child(id).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.err.println("Error al eliminar consulta: " + databaseError.getMessage());
            } else {
                System.out.println("Consulta con ID " + id + " eliminada exitosamente de Firebase.");
            }
            latch.countDown();
        });
        latch.await();
    }
}
