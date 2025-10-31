package hospital.data;

import hospital.logic.Paciente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    Database db;

    public PacienteDao(){db= Database.instance();}

    public void create(Paciente p) throws Exception{
        String sql="insert into Pacientes (idPaciente, nombrePaciente, fechaNacimiento, telefono) values(?,?,?,?)";

        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, p.getId());
            stm.setString(2, p.getNombre());
            stm.setDate(3, Date.valueOf(p.getFechaNacimiento()));
            stm.setString(4, p.getTelefono());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Paciente ya existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al crear paciente: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public Paciente read(String id) throws Exception{
        String sql="select * from Pacientes p where p.idPaciente=?";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                return from(rs,"p");
            } else {
                throw new Exception ("Paciente no Existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al leer paciente: " + ex.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void update(Paciente p) throws Exception{
        String sql="update Pacientes set nombrePaciente=?, fechaNacimiento=?, telefono=? where idPaciente=?";

        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, p.getNombre());
            stm.setDate(2, Date.valueOf(p.getFechaNacimiento()));
            stm.setString(3, p.getTelefono());
            stm.setString(4, p.getId());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Paciente no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar paciente: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void delete(Paciente o) throws Exception{
        String sql="delete from Pacientes where idPaciente=?";
        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, o.getId());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Paciente no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar paciente: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public List<Paciente> findByNombre(Paciente filtro){
        List<Paciente> resultado = new ArrayList<>();
        String sql="select * from Pacientes p where p.nombrePaciente like ?";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            rs = stm.executeQuery();
            while (rs.next()) {
                resultado.add(from(rs,"p"));
            }
        } catch (SQLException ex) {
            // Log error
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
        return resultado;
    }

    private Paciente from(ResultSet rs, String alias){
        try {
            Paciente p= new Paciente();
            p.setId(rs.getString(alias + ".idPaciente"));
            p.setNombre(rs.getString(alias + ".nombrePaciente"));
            Date fechaNacimiento = rs.getDate(alias + ".fechaNacimiento");
            if (fechaNacimiento != null) {
                p.setFechaNacimiento(fechaNacimiento.toLocalDate());
            }
            p.setTelefono(rs.getString(alias + ".telefono"));
            return p;
        } catch (SQLException ex) {
            return null;
        }
    }
}