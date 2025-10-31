package hospital.data;

import hospital.logic.Medico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {
    Database db;

    public MedicoDao(){
        db = Database.instance();
    }

    public void create(Medico m) throws Exception{
        Connection cnx = null;
        PreparedStatement stmUsuario = null;
        PreparedStatement stmMedico = null;
        try {
            cnx = db.getConnection();
            cnx.setAutoCommit(false);

            String sqlUsuario="insert into usuarios (idUsuario, claveUsuario, nombreUsuario, tipoUsuario) values(?,?,?,?)";
            stmUsuario = cnx.prepareStatement(sqlUsuario);
            stmUsuario.setString(1, m.getId());
            stmUsuario.setString(2, m.getClave());
            stmUsuario.setString(3, m.getNombre());
            stmUsuario.setString(4, "MEDICO");
            int countUsuario = stmUsuario.executeUpdate();
            if (countUsuario == 0) {
                throw new Exception("Medico ya existe en la tabla de usuarios");
            }

            String sqlMedico="insert into medicos (usuarios_idUsuario, especialidad) values(?,?)";
            stmMedico = cnx.prepareStatement(sqlMedico);
            stmMedico.setString(1, m.getId());
            stmMedico.setString(2, m.getEspecialidad());
            int countMedico = stmMedico.executeUpdate();
            if (countMedico == 0) {
                throw new Exception("No se pudo crear el registro específico de médico");
            }

            cnx.commit();
        } catch (SQLException ex) {
            if (cnx != null) try { cnx.rollback(); } catch (SQLException ignored) {}
            throw new Exception("Error al crear médico: " + ex.getMessage());
        } finally {
            if (stmUsuario != null) try { stmUsuario.close(); } catch (SQLException ignored) {}
            if (stmMedico != null) try { stmMedico.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public Medico read(String id) throws Exception{
        String sql="select u.idUsuario, u.claveUsuario, u.nombreUsuario, m.especialidad " +
                "from usuarios u inner join medicos m on u.idUsuario = m.usuarios_idUsuario " +
                "where u.idUsuario=?";

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
                throw new Exception ("Medico no Existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al leer médico: " + ex.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void update(Medico m) throws Exception{
        String sqlUsuario="update usuarios set claveUsuario=?, nombreUsuario=? where idUsuario=?";
        String sqlMedico="update medicos set especialidad=? where usuarios_idUsuario=?";

        Connection cnx = null;
        PreparedStatement stmUsuario = null;
        PreparedStatement stmMedico = null;
        try {
            cnx = db.getConnection();
            cnx.setAutoCommit(false);

            stmUsuario = cnx.prepareStatement(sqlUsuario);
            stmUsuario.setString(1, m.getClave());
            stmUsuario.setString(2, m.getNombre());
            stmUsuario.setString(3, m.getId());
            int countUsuario = stmUsuario.executeUpdate();
            if (countUsuario == 0) {
                throw new Exception("Medico no existe en usuarios");
            }

            stmMedico = cnx.prepareStatement(sqlMedico);
            stmMedico.setString(1, m.getEspecialidad());
            stmMedico.setString(2, m.getId());
            stmMedico.executeUpdate();

            cnx.commit();
        } catch (SQLException ex) {
            if (cnx != null) try { cnx.rollback(); } catch (SQLException ignored) {}
            throw new Exception("Error al actualizar médico: " + ex.getMessage());
        } finally {
            if (stmUsuario != null) try { stmUsuario.close(); } catch (SQLException ignored) {}
            if (stmMedico != null) try { stmMedico.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void delete(Medico m) throws Exception{
        String sql="delete from usuarios where idUsuario=?";
        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, m.getId());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Medico no existe");
            }
        } catch (SQLException ex) {
            // Si la eliminación falla por una restricción de clave foránea (p. ej., tiene recetas asociadas),
            // se lanzará una excepción más específica.
            throw new Exception("Error al eliminar médico: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado = new ArrayList<>();
        String sql="select u.idUsuario, u.nombreUsuario, u.claveUsuario, m.especialidad " +
                "from usuarios u inner join medicos m on u.idUsuario = m.usuarios_idUsuario " +
                "where u.nombreUsuario like ?";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            rs = stm.executeQuery();
            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {
            // En un caso real, aquí se debería registrar el error (log).
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
        return resultado;
    }

    private Medico from(ResultSet rs){
        try {
            Medico m= new Medico();
            m.setId(rs.getString("idUsuario"));
            m.setNombre(rs.getString("nombreUsuario"));
            m.setClave(rs.getString("claveUsuario"));
            m.setEspecialidad(rs.getString("especialidad"));
            return m;
        } catch (SQLException ex) {
            return null;
        }
    }
}