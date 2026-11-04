package coopetica.logica;

import coopetica.datos.AsociadoArchivoDAO;
import coopetica.modelo.Asociado;
import java.io.IOException;
import java.util.ArrayList;

public class AsociadoServicio {

    public static final String REGISTRO_EXITOSO = "OK";

    private AsociadoArchivoDAO dao;

    public AsociadoServicio() throws IOException {
        this.dao = new AsociadoArchivoDAO();
    }

    public ArrayList<Asociado> listar() throws IOException {
        return dao.listar();
    }

    public String registrar(String cedula, String nombre, String saldoTexto)
            throws IOException {

        cedula = cedula.trim();
        nombre = nombre.trim();
        saldoTexto = saldoTexto.trim();

        if (cedula.isEmpty() || nombre.isEmpty() || saldoTexto.isEmpty()) {
            return "La cedula, el nombre y el saldo son obligatorios.";
        }

        double saldo;
        try {
            saldo = Double.parseDouble(saldoTexto);
        } catch (NumberFormatException e) {
            return "El saldo debe ser un numero.";
        }
        if (Double.isNaN(saldo) || Double.isInfinite(saldo)) {
            return "El saldo debe ser un numero.";
        }

        return registrar(new Asociado(cedula, nombre, saldo));
    }

    public String registrar(Asociado a)
            throws IOException {

        if (a.getCedula().isEmpty() || a.getNombre().isEmpty()) {
            return "La cedula y el nombre son obligatorios.";
        }

        if (a.getCedula().contains(",") || a.getNombre().contains(",")) {
            return "La cedula y el nombre no pueden contener comas.";
        }

        if (a.getSaldoAportes() < 0) {
            return "El saldo no puede ser negativo.";
        }

        for (Asociado asociado : dao.listar()) {
            if (asociado.getCedula().equals(a.getCedula())) {
                return "Ya existe un asociado con esa cedula.";
            }
        }

        dao.agregar(a);
        return REGISTRO_EXITOSO;
    }

    public int cantidadAsociados() throws IOException {
        return dao.listar().size();
    }

    public double totalAportes() throws IOException {
        double total = 0;
        for (Asociado asociado : dao.listar()) {
            total += asociado.getSaldoAportes();
        }
        return total;
    }

    public Asociado buscarPorCedula(String cedula) throws IOException {
        cedula = cedula.trim();
        for (Asociado asociado : dao.listar()) {
            if (asociado.getCedula().equals(cedula)) {
                return asociado;
            }
        }
        return null;
    }
}
