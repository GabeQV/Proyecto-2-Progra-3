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
        db= Database.instance();
    }

    public void create(Medico m) throws Exception{
        Connection cnx = null;
        try {
            cnx = db.getConnection();
            cnx.setAutoCommit(false);

            String sqlUsuario="insert into usuarios (idUsuario, claveUsuario, nombreUsuario, tipoUsuario) values(?,?,?,?)";
            PreparedStatement stmUsuario = cnx.prepareStatement(sqlUsuario);
            stmUsuario.setString(1, m.getId());
            stmUsuario.setString(2, m.getClave());
            stmUsuario.setString(3, m.getNombre());
            stmUsuario.setString(4, "MEDICO"); // Usamos el tipoUsuario
            int countUsuario = stmUsuario.executeUpdate();
            if (countUsuario == 0) {
                throw new Exception("Medico ya existe en la tabla de usuarios");
            }


            String sqlMedico="insert into medicos (usuarios_idUsuario, especialidad) values(?,?)";
            PreparedStatement stmMedico = cnx.prepareStatement(sqlMedico);
            stmMedico.setString(1, m.getId());
            stmMedico.setString(2, m.getEspecialidad());
            int countMedico = stmMedico.executeUpdate();
            if (countMedico == 0) {
                throw new Exception("No se pudo crear el registro específico de médico");
            }

            cnx.commit();
        } catch (SQLException ex) {
            if (cnx != null) cnx.rollback();
            throw new Exception("Error al crear médico: " + ex.getMessage());
        } finally {
            if (cnx != null) {
                cnx.setAutoCommit(true);
            }
        }
    }

    public Medico read(String id) throws Exception{
        String sql="select u.idUsuario, u.claveUsuario, u.nombreUsuario, m.especialidad " +
                "from usuarios u inner join medicos m on u.idUsuario = m.usuarios_idUsuario " +
                "where u.idUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs);
        }
        else{
            throw new Exception ("Medico no Existe");
        }
    }

    public void update(Medico m) throws Exception{

        String sqlUsuario="update usuarios set claveUsuario=?, nombreUsuario=? where idUsuario=?";
        PreparedStatement stmUsuario = db.prepareStatement(sqlUsuario);
        stmUsuario.setString(1, m.getClave());
        stmUsuario.setString(2, m.getNombre());
        stmUsuario.setString(3, m.getId());
        int countUsuario = db.executeUpdate(stmUsuario);
        if (countUsuario == 0) {
            throw new Exception("Medico no existe en usuarios");
        }


        String sqlMedico="update medicos set especialidad=? where usuarios_idUsuario=?";
        PreparedStatement stmMedico = db.prepareStatement(sqlMedico);
        stmMedico.setString(1, m.getEspecialidad());
        stmMedico.setString(2, m.getId());
        db.executeUpdate(stmMedico);
    }

    public void delete(Medico m) throws Exception{

        String sql="delete from usuarios where idUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado = new ArrayList<>();
        try {
            String sql="select u.idUsuario, u.nombreUsuario, u.claveUsuario, m.especialidad " +
                    "from usuarios u inner join medicos m on u.idUsuario = m.usuarios_idUsuario " +
                    "where u.nombreUsuario like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {  }
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