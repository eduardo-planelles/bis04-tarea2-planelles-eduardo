package coopetica.presentacion;

import coopetica.logica.AsociadoServicio;
import coopetica.modelo.Asociado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VentanaPrincipal extends JFrame {

    private static final String TITULO_VENTANA = "CoopeTica - Asociados";

    private AsociadoServicio servicio;

    private JTextField campoCedula = new JTextField(15);
    private JTextField campoNombre = new JTextField(15);
    private JTextField campoSaldo = new JTextField(15);
    private JTextField campoBuscar = new JTextField(15);

    private JButton botonRegistrar = new JButton("Registrar");
    private JButton botonListar = new JButton("Listar");
    private JButton botonResumen = new JButton("Resumen");
    private JButton botonBuscar = new JButton("Buscar");

    private JTextArea areaResultados = new JTextArea(12, 40);
    private JLabel etiquetaEstado = new JLabel(" ");

    public VentanaPrincipal(AsociadoServicio servicio) {
        this.servicio = servicio;

        setTitle(TITULO_VENTANA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Nuevo asociado"));
        formulario.add(new JLabel("Cedula:"));
        formulario.add(campoCedula);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Saldo de aportes:"));
        formulario.add(campoSaldo);

        JPanel botones = new JPanel();
        botones.add(botonRegistrar);
        botones.add(botonListar);
        botones.add(botonResumen);

        JPanel busqueda = new JPanel();
        busqueda.setBorder(BorderFactory.createTitledBorder("Buscar asociado"));
        busqueda.add(new JLabel("Cedula:"));
        busqueda.add(campoBuscar);
        busqueda.add(botonBuscar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.NORTH);
        norte.add(botones, BorderLayout.CENTER);
        norte.add(busqueda, BorderLayout.SOUTH);

        areaResultados.setEditable(false);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);
        add(etiquetaEstado, BorderLayout.SOUTH);

        botonRegistrar.addActionListener(e -> registrar());
        botonListar.addActionListener(e -> listar());
        botonResumen.addActionListener(e -> mostrarResumen());
        botonBuscar.addActionListener(e -> buscar());

        pack();
        setLocationRelativeTo(null);

        actualizarLista();
    }

    private void registrar() {
        try {
            String resultado = servicio.registrar(
                    campoCedula.getText(),
                    campoNombre.getText(),
                    campoSaldo.getText());

            if (resultado.equals(AsociadoServicio.REGISTRO_EXITOSO)) {
                limpiarCampos();
                actualizarLista();
                mostrarEstado("Asociado registrado correctamente.", false);
                JOptionPane.showMessageDialog(
                        this,
                        "Asociado registrado correctamente.",
                        TITULO_VENTANA,
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                mostrarEstado(resultado, true);
                JOptionPane.showMessageDialog(
                        this,
                        resultado,
                        TITULO_VENTANA,
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException e) {
            mostrarError("Error al guardar el asociado: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("El archivo de asociados tiene una linea con datos incorrectos.");
        }
    }

    private void listar() {
        mostrarEstado("Lista actualizada.", false);
        actualizarLista();
    }

    private void mostrarResumen() {
        try {
            int cantidad = servicio.cantidadAsociados();
            double total = servicio.totalAportes();

            String mensaje = "Cantidad de asociados: " + cantidad
                    + "\nTotal de aportes: " + String.format("%.2f", total);
            mostrarEstado("Cantidad de asociados: " + cantidad
                    + " - Total de aportes: " + String.format("%.2f", total), false);
            JOptionPane.showMessageDialog(
                    this,
                    mensaje,
                    TITULO_VENTANA,
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            mostrarError("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("El archivo de asociados tiene una linea con datos incorrectos.");
        }
    }

    private void buscar() {
        try {
            Asociado a = servicio.buscarPorCedula(campoBuscar.getText());

            if (a == null) {
                areaResultados.setText("--- Busqueda ---\nNo existe un asociado con esa cedula.\n");
                mostrarEstado("No existe un asociado con esa cedula.", true);
                JOptionPane.showMessageDialog(
                        this,
                        "No existe un asociado con esa cedula.",
                        TITULO_VENTANA,
                        JOptionPane.WARNING_MESSAGE);
            } else {
                areaResultados.setText("--- Busqueda ---\n"
                        + "Cedula: " + a.getCedula() + "\n"
                        + "Nombre: " + a.getNombre() + "\n"
                        + "Saldo de aportes: " + String.format("%.2f", a.getSaldoAportes()) + "\n");
                mostrarEstado("Asociado encontrado.", false);
            }
        } catch (IOException e) {
            mostrarError("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("El archivo de asociados tiene una linea con datos incorrectos.");
        }
    }

    private void actualizarLista() {
        try {
            ArrayList<Asociado> lista = servicio.listar();
            areaResultados.setText("--- Lista ---\n");
            for (Asociado a : lista) {
                areaResultados.append(a + "\n");
            }
            if (lista.isEmpty()) {
                areaResultados.append("No hay asociados registrados.\n");
            }
        } catch (IOException e) {
            mostrarError("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("El archivo de asociados tiene una linea con datos incorrectos.");
        }
    }

    private void limpiarCampos() {
        campoCedula.setText("");
        campoNombre.setText("");
        campoSaldo.setText("");
        campoCedula.requestFocus();
    }

    private void mostrarEstado(String mensaje, boolean esError) {
        etiquetaEstado.setText(mensaje);
        if (esError) {
            etiquetaEstado.setForeground(Color.RED);
        } else {
            etiquetaEstado.setForeground(new Color(0, 110, 0));
        }
    }

    private void mostrarError(String mensaje) {
        mostrarEstado(mensaje, true);
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                TITULO_VENTANA,
                JOptionPane.ERROR_MESSAGE);
    }
}
