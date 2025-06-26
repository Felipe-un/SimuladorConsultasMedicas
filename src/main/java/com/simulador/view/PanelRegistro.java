
package com.simulador.view;

import com.simulador.viewmodel.ClinicaViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelRegistro extends JPanel {
    private ClinicaViewModel viewModel;

    // Componentes para el registro de Pacientes
    private JTextField txtIdPaciente, txtNombrePaciente, txtTelefonoPaciente, txtEmailPaciente, txtFechaNacimiento, txtHistorialMedico;
    private JButton btnRegistrarPaciente;

    // Componentes para el registro de Médicos
    private JTextField txtIdMedico, txtNombreMedico, txtTelefonoMedico, txtEmailMedico, txtEspecialidad, txtLicenciaMedica;
    private JButton btnRegistrarMedico;

    public PanelRegistro(ClinicaViewModel viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout(10, 10)); // Layout principal del panel

        // Crear pestañas para Pacientes y Médicos
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Panel de Registro de Pacientes ---
        JPanel panelPaciente = new JPanel(new GridBagLayout());
        panelPaciente.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Paciente"));
        setupPacientePanel(panelPaciente);
        tabbedPane.addTab("Paciente", panelPaciente);

        // --- Panel de Registro de Médicos ---
        JPanel panelMedico = new JPanel(new GridBagLayout());
        panelMedico.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Médico"));
        setupMedicoPanel(panelMedico);
        tabbedPane.addTab("Médico", panelMedico);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupPacientePanel(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes internos
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtIdPaciente = new JTextField(20);
        panel.add(txtIdPaciente, gbc);

        // Fila 2: Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombrePaciente = new JTextField(20);
        panel.add(txtNombrePaciente, gbc);

        // Fila 3: Teléfono
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefonoPaciente = new JTextField(20);
        panel.add(txtTelefonoPaciente, gbc);

        // Fila 4: Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmailPaciente = new JTextField(20);
        panel.add(txtEmailPaciente, gbc);

        // Fila 5: Fecha Nacimiento
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaNacimiento = new JTextField(20);
        panel.add(txtFechaNacimiento, gbc);

        // Fila 6: Historial Médico General
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Historial Médico General:"), gbc);
        gbc.gridx = 1;
        txtHistorialMedico = new JTextField(20);
        panel.add(txtHistorialMedico, gbc);

        // Fila 7: Botón Registrar
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Ocupa dos columnas
        btnRegistrarPaciente = new JButton("Registrar Paciente");
        panel.add(btnRegistrarPaciente, gbc);

        btnRegistrarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPaciente();
            }
        });
    }

    private void setupMedicoPanel(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtIdMedico = new JTextField(20);
        panel.add(txtIdMedico, gbc);

        // Fila 2: Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombreMedico = new JTextField(20);
        panel.add(txtNombreMedico, gbc);

        // Fila 3: Teléfono
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefonoMedico = new JTextField(20);
        panel.add(txtTelefonoMedico, gbc);

        // Fila 4: Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmailMedico = new JTextField(20);
        panel.add(txtEmailMedico, gbc);

        // Fila 5: Especialidad
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1;
        txtEspecialidad = new JTextField(20);
        panel.add(txtEspecialidad, gbc);

        // Fila 6: Licencia Médica
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Licencia Médica:"), gbc);
        gbc.gridx = 1;
        txtLicenciaMedica = new JTextField(20);
        panel.add(txtLicenciaMedica, gbc);

        // Fila 7: Botón Registrar
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        btnRegistrarMedico = new JButton("Registrar Médico");
        panel.add(btnRegistrarMedico, gbc);

        btnRegistrarMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarMedico();
            }
        });
    }

    private void registrarPaciente() {
        String id = txtIdPaciente.getText();
        String nombre = txtNombrePaciente.getText();
        String telefono = txtTelefonoPaciente.getText();
        String email = txtEmailPaciente.getText();
        String fechaNacimiento = txtFechaNacimiento.getText();
        String historialMedicoGeneral = txtHistorialMedico.getText();

        try {
            viewModel.registrarPaciente(id, nombre, telefono, email, fechaNacimiento, historialMedicoGeneral);
            JOptionPane.showMessageDialog(this, "Paciente " + nombre + " registrado exitosamente.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            clearPacienteFields();
        } catch (com.simulador.excepciones.CampoVacioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void registrarMedico() {
        String id = txtIdMedico.getText();
        String nombre = txtNombreMedico.getText();
        String telefono = txtTelefonoMedico.getText();
        String email = txtEmailMedico.getText();
        String especialidad = txtEspecialidad.getText();
        String licenciaMedica = txtLicenciaMedica.getText();

        try {
            viewModel.registrarMedico(id, nombre, telefono, email, especialidad, licenciaMedica);
            JOptionPane.showMessageDialog(this, "Médico " + nombre + " registrado exitosamente.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            clearMedicoFields();
        } catch (com.simulador.excepciones.CampoVacioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearPacienteFields() {
        txtIdPaciente.setText("");
        txtNombrePaciente.setText("");
        txtTelefonoPaciente.setText("");
        txtEmailPaciente.setText("");
        txtFechaNacimiento.setText("");
        txtHistorialMedico.setText("");
    }

    private void clearMedicoFields() {
        txtIdMedico.setText("");
        txtNombreMedico.setText("");
        txtTelefonoMedico.setText("");
        txtEmailMedico.setText("");
        txtEspecialidad.setText("");
        txtLicenciaMedica.setText("");
    }
}