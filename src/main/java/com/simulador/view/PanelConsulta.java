
package com.simulador.view;

import com.simulador.model.Consulta; // Para el objeto Consulta
import com.simulador.model.Paciente; // Para cargar los pacientes en el ComboBox
import com.simulador.model.Medico;   // Para cargar los médicos en el ComboBox
import com.simulador.viewmodel.ClinicaViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.simulador.excepciones.CampoVacioException;
import com.simulador.excepciones.UsuarioNoEncontradoException;

public class PanelConsulta extends JPanel {
    private ClinicaViewModel viewModel;

    private JComboBox<String> cmbPacientes;
    private JComboBox<String> cmbMedicos;
    private JTextArea txtAreaSintomas;
    private JTextArea txtAreaDiagnostico;
    private JTextArea txtAreaTratamiento;
    private JButton btnAsignarConsulta;

    public PanelConsulta(ClinicaViewModel viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Asignar Nueva Consulta"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Paciente
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1;
        cmbPacientes = new JComboBox<>();
        formPanel.add(cmbPacientes, gbc);

        // Medico
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Médico:"), gbc);
        gbc.gridx = 1;
        cmbMedicos = new JComboBox<>();
        formPanel.add(cmbMedicos, gbc);

        // Síntomas
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Síntomas:"), gbc);
        gbc.gridx = 1;
        txtAreaSintomas = new JTextArea(5, 20);
        JScrollPane scrollSintomas = new JScrollPane(txtAreaSintomas);
        formPanel.add(scrollSintomas, gbc);

        // Diagnóstico
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Diagnóstico:"), gbc);
        gbc.gridx = 1;
        txtAreaDiagnostico = new JTextArea(5, 20);
        JScrollPane scrollDiagnostico = new JScrollPane(txtAreaDiagnostico);
        formPanel.add(scrollDiagnostico, gbc);

        // Tratamiento
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Tratamiento:"), gbc);
        gbc.gridx = 1;
        txtAreaTratamiento = new JTextArea(5, 20);
        JScrollPane scrollTratamiento = new JScrollPane(txtAreaTratamiento);
        formPanel.add(scrollTratamiento, gbc);

        // Botón
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        btnAsignarConsulta = new JButton("Asignar Consulta");
        formPanel.add(btnAsignarConsulta, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Cargar datos en los ComboBoxes al inicializar el panel
        loadComboBoxData();

        btnAsignarConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarConsulta();
            }
        });
    }

    private void loadComboBoxData() {
        // Cargar Pacientes
        cmbPacientes.removeAllItems();
        cmbPacientes.addItem("Seleccione un paciente..."); // Opción por defecto
        List<Paciente> pacientes = viewModel.getListaPacientes();
        if (pacientes != null) {
            for (Paciente p : pacientes) {
                cmbPacientes.addItem(p.getId() + " - " + p.getNombre());
            }
        }

        // Cargar Médicos
        cmbMedicos.removeAllItems();
        cmbMedicos.addItem("Seleccione un médico..."); // Opción por defecto
        List<Medico> medicos = viewModel.getListaMedicos();
        if (medicos != null) {
            for (Medico m : medicos) {
                cmbMedicos.addItem(m.getId() + " - " + m.getNombre());
            }
        }
    }

    private void asignarConsulta() {
        String pacienteSeleccionado = (String) cmbPacientes.getSelectedItem();
        String medicoSeleccionado = (String) cmbMedicos.getSelectedItem();
        String sintomas = txtAreaSintomas.getText();
        String diagnostico = txtAreaDiagnostico.getText();
        String tratamiento = txtAreaTratamiento.getText();

        if (pacienteSeleccionado == null || pacienteSeleccionado.contains("Seleccione")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente.", "Error de Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (medicoSeleccionado == null || medicoSeleccionado.contains("Seleccione")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un médico.", "Error de Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extraer solo el ID de las cadenas seleccionadas
        String idPaciente = pacienteSeleccionado.split(" - ")[0];
        String idMedico = medicoSeleccionado.split(" - ")[0];

        try {
            Consulta nuevaConsulta = viewModel.asignarConsulta(idPaciente, idMedico, sintomas, diagnostico, tratamiento);
            JOptionPane.showMessageDialog(this, "Consulta asignada exitosamente. ID: " + nuevaConsulta.getIdConsulta(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            // Recargar datos en caso de que necesitemos actualizar algo (aunque para asignación no es crítico aquí)
            // loadComboBoxData(); // Si las listas cambian o la consulta influye
        } catch (CampoVacioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al asignar consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        cmbPacientes.setSelectedIndex(0); // Seleccionar la opción por defecto
        cmbMedicos.setSelectedIndex(0);   // Seleccionar la opción por defecto
        txtAreaSintomas.setText("");
        txtAreaDiagnostico.setText("");
        txtAreaTratamiento.setText("");
    }
}