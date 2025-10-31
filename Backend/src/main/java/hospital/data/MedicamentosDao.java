package hospital.data;

import hospital.logic.Medicamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentosDao {
    Database db;

    public MedicamentosDao(){
        db= Database.instance();
    }

    public void create(Medicamento e) throws Exception{
        String sql="insert into Medicamentos (idMedicamento, nombreMedicamento, presentacionMedicamento) values(?,?,?)";

        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, e.getId());
            stm.setString(2, e.getNombre());
            stm.setString(3, e.getPresentacion());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Medicamento ya existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al crear medicamento: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public Medicamento read(String id) throws Exception{
        String sql="select * from Medicamentos m where m.idMedicamento=?";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                return from(rs,"m");
            } else {
                throw new Exception ("Medicamento no Existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al leer medicamento: " + ex.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void update(Medicamento p) throws Exception{
        String sql="update Medicamentos set nombreMedicamento=?, presentacionMedicamento=? where idMedicamento=?";

        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, p.getNombre());
            stm.setString(2, p.getPresentacion());
            stm.setString(3, p.getId());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Medicamento no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar medicamento: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void delete(Medicamento m) throws Exception{
        String sql="delete from Medicamentos where idMedicamento=?";
        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, m.getId());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Medicamento no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar medicamento: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public List<Medicamento> findByNombre(Medicamento filtro){
        List<Medicamento> resultado = new ArrayList<>();
        String sql="select * from Medicamentos m where m.nombreMedicamento like ?";

        Connection cnx = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            rs = stm.executeQuery();
            while (rs.next()) {
                resultado.add(from(rs,"m"));
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

    private Medicamento from(ResultSet rs, String alias){
        try {
            Medicamento p= new Medicamento();
            p.setId(rs.getString(alias + ".idMedicamento"));
            p.setNombre(rs.getString(alias + ".nombreMedicamento"));
            p.setPresentacion(rs.getString(alias + ".presentacionMedicamento"));
            return p;
        } catch (SQLException ex) {
            return null;
        }
    }
}