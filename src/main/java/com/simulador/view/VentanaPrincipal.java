
package com.simulador.view;

import com.simulador.viewmodel.ClinicaViewModel; // Importamos el ViewModel

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.simulador.excepciones.UsuarioNoEncontradoException; // Para el login
import com.simulador.excepciones.CampoVacioException; // Para el login

public class VentanaPrincipal extends JFrame {
    private ClinicaViewModel viewModel; // Instancia del ViewModel
    private JPanel contentPanel; // Panel para cambiar el contenido de la ventana
    private JMenuBar menuBar;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Consultas Médicas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        viewModel = new ClinicaViewModel(); // Inicializamos el ViewModel

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Pantalla de Login inicial
        showLoginPanel();

        // No mostraremos el menú hasta que el usuario inicie sesión
        menuBar = new JMenuBar();
        setJMenuBar(menuBar); // Se establecerá visible después del login exitoso
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JLabel idLabel = new JLabel("ID de Usuario:");
        JTextField idField = new JTextField();
        JButton loginButton = new JButton("Iniciar Sesión");
        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);

        loginPanel.add(idLabel);
        loginPanel.add(idField);
        loginPanel.add(new JLabel("")); // Espacio
        loginPanel.add(messageLabel);
        loginPanel.add(loginButton);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                try {
                    String tipoUsuario = viewModel.login(id);
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Bienvenido, " + tipoUsuario + "!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    setupMenu(tipoUsuario); // Configura el menú según el tipo de usuario
                    contentPanel.removeAll(); // Limpia el panel de contenido
                    contentPanel.revalidate();
                    contentPanel.repaint();
                    // Opcional: Mostrar un panel por defecto después del login (ej. un panel de bienvenida)
                    showDefaultPanel();
                } catch (CampoVacioException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (UsuarioNoEncontradoException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (Exception ex) {
                    messageLabel.setText("Error inesperado: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        contentPanel.removeAll();
        contentPanel.add(loginPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setupMenu(String tipoUsuario) {
        // Habilitar la barra de menú una vez que el usuario ha iniciado sesión
        setJMenuBar(menuBar);

        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem salirItem = new JMenuItem("Salir");
        salirItem.addActionListener(e -> System.exit(0));
        archivoMenu.add(salirItem);
        menuBar.add(archivoMenu);

        JMenu gestionMenu = new JMenu("Gestión");
        JMenuItem registrarItem = new JMenuItem("Registrar Persona");
        JMenuItem consultaItem = new JMenuItem("Asignar Consulta");
        JMenuItem historialItem = new JMenuItem("Historial Médico");

        registrarItem.addActionListener(e -> showPanel(new PanelRegistro(viewModel)));
        consultaItem.addActionListener(e -> showPanel(new PanelConsulta(viewModel)));
        historialItem.addActionListener(e -> showPanel(new PanelHistorial(viewModel)));

        gestionMenu.add(registrarItem);
        gestionMenu.add(consultaItem);
        gestionMenu.add(historialItem);
        menuBar.add(gestionMenu);

        // Dependiendo del tipo de usuario, podríamos ocultar/mostrar elementos del menú
        // Por ahora, todos ven todo, pero es un punto de extensión.
    }

    private void showDefaultPanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar
        JLabel welcomeLabel = new JLabel("<html><h1 style='color: #336699;'>Bienvenido al Sistema de Gestión de Consultas Médicas</h1></html>");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomePanel.add(welcomeLabel);
        contentPanel.removeAll();
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Método para cambiar el panel visible en la ventana principal
    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}