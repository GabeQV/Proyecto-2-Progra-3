package hospital.logic;

import hospital.data.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private MedicoDao medicoDao;
    private FarmaceutaDao farmaceutaDao;
    private PacienteDao pacienteDao;
    private RecetasDao recetasDao;
    private MedicamentosDao medicamentosDao;

    Service() {
        try {
            medicoDao = new MedicoDao();
            farmaceutaDao = new FarmaceutaDao();
            pacienteDao = new PacienteDao();
            recetasDao = new RecetasDao();
            medicamentosDao = new MedicamentosDao();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    public void stop() {
    }

    // =============== MEDICOS ===============
    public void createMedico(Medico e) throws Exception {
        medicoDao.create(e);
    }
    public Medico readMedico(Medico e) throws Exception {
        return medicoDao.read(e.getId());
    }
    public void deleteMedico(Medico e) throws Exception {
        medicoDao.delete(e);
    }
    public void updateMedico(Medico e) throws Exception {
        medicoDao.update(e);
        stop();
    }
    public List<Medico> findAllMedicos() {
        Medico filtro = new Medico();
        filtro.setNombre("");
        return medicoDao.findByNombre(filtro);
    }

    // =============== FARMACEUTAS ===============
    public void createFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.create(e);
    }
    public Farmaceuta readFarmaceuta(Farmaceuta e) throws Exception {
        return farmaceutaDao.read(e.getId());
    }
    public void deleteFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.delete(e);
    }
    public void updateFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.update(e);
        stop();
    }
    public List<Farmaceuta> findAllFarmaceutas() {
        Farmaceuta filtro = new Farmaceuta();
        filtro.setNombre("");
        return farmaceutaDao.findByNombre(filtro);
    }

    // =============== PACIENTES ===============
    public void createPaciente(Paciente e) throws Exception {
        pacienteDao.create(e);
    }
    public Paciente readPaciente(Paciente e) throws Exception {
        return pacienteDao.read(e.getId());
    }
    public void deletePaciente(Paciente e) throws Exception {
        pacienteDao.delete(e);
    }
    public void updatePaciente(Paciente e) throws Exception {
        pacienteDao.update(e);
        stop();
    }
    public List<Paciente> findAllPacientes() {
        Paciente filtro = new Paciente();
        filtro.setNombre("");
        return pacienteDao.findByNombre(filtro);
    }

    // =============== RECETAS ===============
    public void createReceta(Receta e) throws Exception {
        recetasDao.create(e);
    }
    public Receta readReceta(Receta e) throws Exception {
        return recetasDao.read(e.getId());
    }
    public void deleteRecetas(Receta e) throws Exception {
        recetasDao.delete(e);
    }
    public List<Receta> findAllRecetas() {
        return recetasDao.findAll();
    }
    public List<Receta> findRecetasByPacienteId(String pacienteId) {
        return recetasDao.buscarPorPaciente(pacienteId);
    }
    public void updateReceta(Receta r) throws Exception {
        recetasDao.update(r);
    }
    public boolean findRecetaById(String id) {
        try {
            recetasDao.read(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // =============== MEDICAMENTOS ===============
    public void createMedicamento(Medicamento e) throws Exception {
        medicamentosDao.create(e);
    }
    public Medicamento readMedicamento(Medicamento e) throws Exception {
        return medicamentosDao.read(e.getId());
    }
    public void deleteMedicamento(Medicamento e) throws Exception {
        medicamentosDao.delete(e);
    }
    public void updateMedicamento(Medicamento e) throws Exception {
        medicamentosDao.update(e);
        stop();
    }
    public List<Medicamento> findMedicamentosByNombre(String nombre) {
        Medicamento filtro = new Medicamento();
        filtro.setNombre(nombre);
        return medicamentosDao.findByNombre(filtro);
    }
    public List<Medicamento> findMedicamentosByCodigo(String codigo) {
        try {
            Medicamento m = medicamentosDao.read(codigo);
            return List.of(m);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
    public List<Medicamento> findAllMedicamentos() {
        Medicamento filtro = new Medicamento();
        filtro.setNombre("");
        return medicamentosDao.findByNombre(filtro);
    }

    // =============== AUTENTICACIÓN Y CONTRASEÑA ===============

    public Usuario login(String id, String clave) throws Exception {
        // Admin local
        if ("ADM".equals(id) && "ADM".equals(clave)) return new Admin();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.instance().getConnection();

            String sql = "SELECT nombreUsuario, tipoUsuario FROM usuarios WHERE idUsuario = ? AND claveUsuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, clave);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new Exception("Usuario o clave incorrecta");
            }

            String nombre = rs.getString("nombreUsuario");
            String tipo = rs.getString("tipoUsuario");

            switch (tipo) {
                case "MEDICO":
                    rs.close(); stmt.close();
                    sql = "SELECT especialidad FROM medicos WHERE usuarios_idUsuario = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, id);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        String esp = rs.getString("especialidad");
                        return new Medico(id, clave, nombre, esp != null ? esp : "");
                    }
                    break;
                case "FARMACEUTA":
                    return new Farmaceuta(id, clave, nombre);
            }

            throw new Exception("El usuario no tiene un rol asignado válido (medico o farmaceuta).");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error de base de datos: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    public void cambiarClave(String id, String claveActual, String nuevaClave, String nuevaClaveConfirm) throws Exception {
        if ("ADM".equals(id)) throw new Exception("No es posible cambiar la clave del administrador.");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.instance().getConnection();

            String sql = "SELECT claveUsuario FROM usuarios WHERE idUsuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (!rs.next()) throw new Exception("Usuario no encontrado");

            String claveBD = rs.getString("claveUsuario");
            if (!claveBD.equals(claveActual)) throw new Exception("La clave actual no es correcta");
            if (!nuevaClave.equals(nuevaClaveConfirm)) throw new Exception("La nueva clave no coincide");

            rs.close();
            stmt.close();

            sql = "UPDATE usuarios SET claveUsuario = ? WHERE idUsuario = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevaClave);
            stmt.setString(2, id);

            int filas = stmt.executeUpdate();
            if (filas == 0) throw new Exception("No se pudo actualizar la clave.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error de base de datos: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}