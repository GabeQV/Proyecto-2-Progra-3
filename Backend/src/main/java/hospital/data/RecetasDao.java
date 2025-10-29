package hospital.data;

import hospital.logic.*;

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
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getId());
        stm.setString(2, r.getIndicaciones());
        stm.setString(3, r.getCantidad());
        stm.setString(4, r.getDuracion());
        stm.setString(5, r.getEstado());
        stm.setObject(6, r.getFecha());
        stm.setString(7, r.getMedicamento() != null ? r.getMedicamento().getId() : null);
        stm.setString(8, r.getPaciente() != null ? r.getPaciente().getId() : null);
        stm.setString(9, r.getUsuario() != null ? r.getUsuario().getId() : null);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo crear la receta");
        }
    }

    public Receta read(String id) throws Exception {
        String sql = "SELECT " +
                "r.idRecetas, r.indicaciones, r.cantidad, r.duracion, r.estado, r.fecha, " +
                "med.idMedicamento AS med_id, med.nombreMedicamento AS med_nombre, med.presentacionMedicamento AS med_presentacion, " +
                "pac.idPaciente AS pac_id, pac.nombrePaciente AS pac_nombre, " +
                "usr.idUsuario AS usr_id, usr.nombreUsuario AS usr_nombre " +
                "FROM recetas r " +
                "JOIN medicamentos med ON r.medicamentos_idMedicamento = med.idMedicamento " +
                "JOIN pacientes pac ON r.pacientes_idPaciente = pac.idPaciente " +
                "LEFT JOIN usuarios usr ON r.usuarios_idusuarios = usr.idUsuario " +
                "WHERE r.idRecetas = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs);
        } else {
            throw new Exception("Receta no existe");
        }
    }

    // ---------------- UPDATE ----------------
    public void update(Receta r) throws Exception {
        String sql = "UPDATE recetas SET indicaciones=?, cantidad=?, duracion=?, estado=?, fecha=?, " +
                "medicamentos_idMedicamento=?, pacientes_idPaciente=?, usuarios_idusuarios=? " +
                "WHERE idRecetas=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getIndicaciones());
        stm.setString(2, r.getCantidad());
        stm.setString(3, r.getDuracion());
        stm.setString(4, r.getEstado());
        stm.setObject(5, r.getFecha());
        stm.setString(6, r.getMedicamento() != null ? r.getMedicamento().getId() : null);
        stm.setString(7, r.getPaciente() != null ? r.getPaciente().getId() : null);
        stm.setString(8, r.getUsuario() != null ? r.getUsuario().getId() : null);
        stm.setString(9, r.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Receta no existe");
        }
    }

    public void delete(Receta r) throws Exception {
        String sql = "DELETE FROM recetas WHERE idRecetas = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Receta no existe");
        }
    }

    public List<Receta> buscarPorPaciente(String idPaciente) {
        List<Receta> resultado = new ArrayList<>();
        String sql = "SELECT " +
                "r.idRecetas, r.indicaciones, r.cantidad, r.duracion, r.estado, r.fecha, " +
                "med.idMedicamento AS med_id, med.nombreMedicamento AS med_nombre, med.presentacionMedicamento AS med_presentacion, " +
                "pac.idPaciente AS pac_id, pac.nombrePaciente AS pac_nombre, " +
                "usr.idUsuario AS usr_id, usr.nombreUsuario AS usr_nombre " +
                "FROM recetas r " +
                "JOIN medicamentos med ON r.medicamentos_idMedicamento = med.idMedicamento " +
                "JOIN pacientes pac ON r.pacientes_idPaciente = pac.idPaciente " +
                "LEFT JOIN usuarios usr ON r.usuarios_idusuarios = usr.idUsuario " +
                "WHERE r.pacientes_idPaciente = ?";
        try {
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, idPaciente);
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                Receta receta = from(rs);
                if (receta != null) {
                    resultado.add(receta);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return resultado;
    }

    public List<Receta> findAll() {
        List<Receta> resultado = new ArrayList<>();
        String sql = "SELECT " +
                "r.idRecetas, r.indicaciones, r.cantidad, r.duracion, r.estado, r.fecha, " +
                "med.idMedicamento AS med_id, med.nombreMedicamento AS med_nombre, med.presentacionMedicamento AS med_presentacion, " +
                "pac.idPaciente AS pac_id, pac.nombrePaciente AS pac_nombre, " +
                "usr.idUsuario AS usr_id, usr.nombreUsuario AS usr_nombre " +
                "FROM recetas r " +
                "JOIN medicamentos med ON r.medicamentos_idMedicamento = med.idMedicamento " +
                "JOIN pacientes pac ON r.pacientes_idPaciente = pac.idPaciente " +
                "LEFT JOIN usuarios usr ON r.usuarios_idusuarios = usr.idUsuario";
        try {
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                Receta receta = from(rs);
                if (receta != null) {
                    resultado.add(receta);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
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
                Medico u = new Medico();
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
