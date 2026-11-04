package coopetica.presentacion;

import coopetica.logica.AsociadoServicio;
import coopetica.modelo.Asociado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
    private static final String AUTOR = "Eduardo Planelles Ramirez";

    private AsociadoServicio servicio;

    private JTextField campoCedula = new JTextField(15);
    private JTextField campoNombre = new JTextField(15);
    private JTextField campoSaldo = new JTextField(15);
    private JTextField campoBuscar = new JTextField(15);

    private JButton botonRegistrar = new JButton("Registrar");
    private JButton botonListar = new JButton("Listar");
    private JButton botonResumen = new JButton("Resumen");
    private JButton botonBuscar = new JButton("Buscar");

    private JTextArea areaListado = new JTextArea(12, 40);
    private JLabel etiquetaEstado = new JLabel(" ");
    private JLabel etiquetaAutor = new JLabel("Elaborado por: " + AUTOR);

    public VentanaPrincipal(AsociadoServicio servicio) {
        this.servicio = servicio;

        setTitle(TITULO_VENTANA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
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

        JPanel registro = new JPanel(new BorderLayout(5, 5));
        registro.setBorder(BorderFactory.createTitledBorder("Asociados"));
        registro.add(formulario, BorderLayout.CENTER);
        registro.add(botones, BorderLayout.SOUTH);

        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar asociado"));
        panelBusqueda.add(new JLabel("Cedula:"));
        panelBusqueda.add(campoBuscar);
        panelBusqueda.add(botonBuscar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(registro, BorderLayout.CENTER);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        etiquetaAutor.setHorizontalAlignment(JLabel.CENTER);
        etiquetaAutor.setFont(etiquetaAutor.getFont().deriveFont(Font.BOLD, 14f));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(etiquetaAutor, BorderLayout.NORTH);
        encabezado.add(panelSuperior, BorderLayout.CENTER);

        areaListado.setEditable(false);

        add(encabezado, BorderLayout.NORTH);
        add(new JScrollPane(areaListado), BorderLayout.CENTER);
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
                areaListado.setText("--- Busqueda ---\nNo existe un asociado con esa cedula.\n");
                mostrarEstado("No existe un asociado con esa cedula.", true);
                JOptionPane.showMessageDialog(
                        this,
                        "No existe un asociado con esa cedula.",
                        TITULO_VENTANA,
                        JOptionPane.WARNING_MESSAGE);
            } else {
                areaListado.setText("--- Busqueda ---\n"
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
            areaListado.setText("--- Lista ---\n");
            for (Asociado a : lista) {
                areaListado.append(a + "\n");
            }
            if (lista.isEmpty()) {
                areaListado.append("No hay asociados registrados.\n");
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
