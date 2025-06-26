package com.simulador.view;

import com.simulador.model.Consulta;
import com.simulador.model.Paciente;
import com.simulador.model.Medico;
import com.simulador.viewmodel.ClinicaViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.simulador.excepciones.CampoVacioException;
import com.simulador.excepciones.UsuarioNoEncontradoException;

public class PanelHistorial extends JPanel {
    private ClinicaViewModel viewModel;

    // Componentes para Historial de Paciente
    private JTextField txtIdPacienteHistorial;
    private JButton btnBuscarHistorialPaciente;
    private JTable tablaHistorialPaciente;
    private DefaultTableModel modeloTablaHistorialPaciente;

    // Componentes para Consultas por Médico
    private JTextField txtIdMedicoConsultas;
    private JButton btnBuscarConsultasMedico;
    private JTable tablaConsultasMedico;
    private DefaultTableModel modeloTablaConsultasMedico;

    public PanelHistorial(ClinicaViewModel viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Historial y Consultas"));

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Panel de Historial de Paciente ---
        JPanel panelHistorialPaciente = new JPanel(new BorderLayout(10, 10));
        panelHistorialPaciente.setBorder(BorderFactory.createTitledBorder("Historial Médico por Paciente"));
        setupHistorialPacientePanel(panelHistorialPaciente);
        tabbedPane.addTab("Historial Paciente", panelHistorialPaciente);

        // --- Panel de Consultas por Médico ---
        JPanel panelConsultasMedico = new JPanel(new BorderLayout(10, 10));
        panelConsultasMedico.setBorder(BorderFactory.createTitledBorder("Consultas Realizadas por Médico"));
        setupConsultasMedicoPanel(panelConsultasMedico);
        tabbedPane.addTab("Consultas Médico", panelConsultasMedico);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupHistorialPacientePanel(JPanel panel) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("ID Paciente:"));
        txtIdPacienteHistorial = new JTextField(15);
        inputPanel.add(txtIdPacienteHistorial);
        btnBuscarHistorialPaciente = new JButton("Buscar Historial");
        inputPanel.add(btnBuscarHistorialPaciente);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID Consulta", "ID Paciente", "ID Médico", "Fecha", "Síntomas", "Diagnóstico", "Tratamiento"};
        modeloTablaHistorialPaciente = new DefaultTableModel(columnNames, 0);
        tablaHistorialPaciente = new JTable(modeloTablaHistorialPaciente);
        JScrollPane scrollPane = new JScrollPane(tablaHistorialPaciente);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnBuscarHistorialPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarHistorialPaciente();
            }
        });
    }

    private void setupConsultasMedicoPanel(JPanel panel) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("ID Médico:"));
        txtIdMedicoConsultas = new JTextField(15);
        inputPanel.add(txtIdMedicoConsultas);
        btnBuscarConsultasMedico = new JButton("Buscar Consultas");
        inputPanel.add(btnBuscarConsultasMedico);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID Consulta", "ID Paciente", "ID Médico", "Fecha", "Síntomas", "Diagnóstico", "Tratamiento"};
        modeloTablaConsultasMedico = new DefaultTableModel(columnNames, 0);
        tablaConsultasMedico = new JTable(modeloTablaConsultasMedico);
        JScrollPane scrollPane = new JScrollPane(tablaConsultasMedico);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnBuscarConsultasMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarConsultasMedico();
            }
        });
    }

    private void buscarHistorialPaciente() {
        String idPaciente = txtIdPacienteHistorial.getText();
        try {
            List<Consulta> historial = viewModel.consultarHistorialPaciente(idPaciente);
            modeloTablaHistorialPaciente.setRowCount(0); // Limpiar tabla
            if (historial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron consultas para el paciente con ID: " + idPaciente, "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Consulta c : historial) {
                    modeloTablaHistorialPaciente.addRow(new Object[]{
                        c.getIdConsulta(),
                        c.getIdPaciente(),
                        c.getIdMedico(),
                        c.getFechaConsulta(),
                        c.getSintomas(),
                        c.getDiagnostico(),
                        c.getTratamiento()
                    });
                }
                JOptionPane.showMessageDialog(this, "Historial de paciente cargado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (CampoVacioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buscarConsultasMedico() {
        String idMedico = txtIdMedicoConsultas.getText();
        try {
            List<Consulta> consultas = viewModel.listarConsultasPorMedico(idMedico);
            modeloTablaConsultasMedico.setRowCount(0); // Limpiar tabla
            if (consultas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron consultas para el médico con ID: " + idMedico, "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Consulta c : consultas) {
                    modeloTablaConsultasMedico.addRow(new Object[]{
                        c.getIdConsulta(),
                        c.getIdPaciente(),
                        c.getIdMedico(),
                        c.getFechaConsulta(),
                        c.getSintomas(),
                        c.getDiagnostico(),
                        c.getTratamiento()
                    });
                }
                JOptionPane.showMessageDialog(this, "Consultas del médico cargadas exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (CampoVacioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar consultas del médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}