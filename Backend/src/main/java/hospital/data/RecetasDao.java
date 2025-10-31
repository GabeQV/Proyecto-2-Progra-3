package hospital.data;

import hospital.logic.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecetasDao {
    Database db;

    public RecetasDao() {
        db = Database.instance();
    }

    public void create(Receta r) throws Exception {
        String sql = "INSERT INTO recetas (idRecetas, indicaciones, cantidad, duracion, estado, fecha, " +
                "medicamentos_idMedicamento, pacientes_idPaciente, usuarios_idusuarios) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, r.getId());
            stm.setString(2, r.getIndicaciones());
            stm.setString(3, r.getCantidad());
            stm.setString(4, r.getDuracion());
            stm.setString(5, r.getEstado());
            stm.setObject(6, r.getFecha());
            stm.setString(7, r.getMedicamento() != null ? r.getMedicamento().getId() : null);
            stm.setString(8, r.getPaciente() != null ? r.getPaciente().getId() : null);
            stm.setString(9, r.getUsuario() != null ? r.getUsuario().getId() : null);

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("No se pudo crear la receta");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al crear receta: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public Receta read(String id) throws Exception {
        String sql = "SELECT " +
                "r.idRecetas, r.indicaciones, r.cantidad, r.duracion, r.estado, r.fecha, " +
                "med.idMedicamento AS med_id, med.nombreMedicamento AS med_nombre, med.presentacionMedicamento AS med_presentacion, " +
                "pac.idPaciente AS pac_id, pac.nombrePaciente AS pac_nombre, " +
                "usr.idUsuario AS usr_id, usr.nombreUsuario AS usr_nombre, usr.tipoUsuario as usr_tipo " +
                "FROM recetas r " +
                "JOIN medicamentos med ON r.medicamentos_idMedicamento = med.idMedicamento " +
                "JOIN pacientes pac ON r.pacientes_idPaciente = pac.idPaciente " +
                "LEFT JOIN usuarios usr ON r.usuarios_idusuarios = usr.idUsuario " +
                "WHERE r.idRecetas = ?";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                return from(rs);
            } else {
                throw new Exception("Receta no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al leer receta: " + ex.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void update(Receta r) throws Exception {
        String sql = "UPDATE recetas SET indicaciones=?, cantidad=?, duracion=?, estado=?, fecha=?, " +
                "medicamentos_idMedicamento=?, pacientes_idPaciente=?, usuarios_idusuarios=? " +
                "WHERE idRecetas=?";

        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, r.getIndicaciones());
            stm.setString(2, r.getCantidad());
            stm.setString(3, r.getDuracion());
            stm.setString(4, r.getEstado());
            stm.setObject(5, r.getFecha());
            stm.setString(6, r.getMedicamento() != null ? r.getMedicamento().getId() : null);
            stm.setString(7, r.getPaciente() != null ? r.getPaciente().getId() : null);
            stm.setString(8, r.getUsuario() != null ? r.getUsuario().getId() : null);
            stm.setString(9, r.getId());

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Receta no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar receta: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void delete(Receta r) throws Exception {
        String sql = "DELETE FROM recetas WHERE idRecetas = ?";
        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, r.getId());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Receta no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar receta: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public List<Receta> buscarPorPaciente(String idPaciente) {
        List<Receta> resultado = new ArrayList<>();
        String sql = "SELECT " +
                "r.idRecetas, r.indicaciones, r.cantidad, r.duracion, r.estado, r.fecha, " +
                "med.idMedicamento AS med_id, med.nombreMedicamento AS med_nombre, med.presentacionMedicamento AS med_presentacion, " +
                "pac.idPaciente AS pac_id, pac.nombrePaciente AS pac_nombre, " +
                "usr.idUsuario AS usr_id, usr.nombreUsuario AS usr_nombre, usr.tipoUsuario as usr_tipo " +
                "FROM recetas r " +
                "JOIN medicamentos med ON r.medicamentos_idMedicamento = med.idMedicamento " +
                "JOIN pacientes pac ON r.pacientes_idPaciente = pac.idPaciente " +
                "LEFT JOIN usuarios usr ON r.usuarios_idusuarios = usr.idUsuario " +
                "WHERE r.pacientes_idPaciente = ?";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, idPaciente);
            rs = stm.executeQuery();
            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
        return resultado;
    }

    public List<Receta> findAll() {
        List<Receta> resultado = new ArrayList<>();
        String sql = "SELECT " +
                "r.idRecetas, r.indicaciones, r.cantidad, r.duracion, r.estado, r.fecha, " +
                "med.idMedicamento AS med_id, med.nombreMedicamento AS med_nombre, med.presentacionMedicamento AS med_presentacion, " +
                "pac.idPaciente AS pac_id, pac.nombrePaciente AS pac_nombre, " +
                "usr.idUsuario AS usr_id, usr.nombreUsuario AS usr_nombre, usr.tipoUsuario as usr_tipo " +
                "FROM recetas r " +
                "JOIN medicamentos med ON r.medicamentos_idMedicamento = med.idMedicamento " +
                "JOIN pacientes pac ON r.pacientes_idPaciente = pac.idPaciente " +
                "LEFT JOIN usuarios usr ON r.usuarios_idusuarios = usr.idUsuario";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
        return resultado;
    }

    private Receta from(ResultSet rs) {
        try {
            Receta r = new Receta();
            r.setId(rs.getString("idRecetas"));
            r.setIndicaciones(rs.getString("indicaciones"));
            r.setCantidad(rs.getString("cantidad"));
            r.setDuracion(rs.getString("duracion"));
            r.setEstado(rs.getString("estado"));
            r.setFecha(rs.getObject("fecha", LocalDate.class));

            Medicamento m = new Medicamento();
            m.setId(rs.getString("med_id"));
            m.setNombre(rs.getString("med_nombre"));
            m.setPresentacion(rs.getString("med_presentacion"));
            r.setMedicamento(m);

            Paciente p = new Paciente();
            p.setId(rs.getString("pac_id"));
            p.setNombre(rs.getString("pac_nombre"));
            r.setPaciente(p);

            String userId = rs.getString("usr_id");
            if (userId != null) {
                String userType = rs.getString("usr_tipo");
                Usuario u;
                if ("MEDICO".equals(userType)) {
                    u = new Medico();
                } else if ("FARMACEUTA".equals(userType)) {
                    u = new Farmaceuta();
                } else {
                    u = new Admin(); // o un tipo por defecto
                }
                u.setId(userId);
                u.setNombre(rs.getString("usr_nombre"));
                r.setUsuario(u);
            } else {
                r.setUsuario(null);
            }

            return r;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}