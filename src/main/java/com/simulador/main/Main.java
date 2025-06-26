// Main.java
package com.simulador.main; // <<-- ¡Asegúrate de que esta línea esté presente y coincida con el nombre de tu paquete!

import com.simulador.view.VentanaPrincipal;
import javax.swing.SwingUtilities;
//import com.simulador.persistencia.PersistenciaFirebase; // Importa tu clase de persistencia
//import com.simulador.model.Medico; // Importa las clases de tu modelo
//import com.simulador.model.Paciente;

public class Main {
    public static void main(String[] args) {

        // Ejecutar la interfaz de usuario en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            }
        });
    }
}