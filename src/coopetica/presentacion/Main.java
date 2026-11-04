package coopetica.presentacion;

import coopetica.logica.AsociadoServicio;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        try {
            AsociadoServicio servicio = new AsociadoServicio();
            VentanaPrincipal ventana = new VentanaPrincipal(servicio);
            ventana.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo abrir el archivo de datos: " + e.getMessage(),
                    "CoopeTica", JOptionPane.ERROR_MESSAGE);
        }
    }
}
