package hospital.data;

import hospital.logic.Farmaceuta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaDao {
    Database db;

    public FarmaceutaDao(){
        db= Database.instance();
    }

    public void create(Farmaceuta f) throws Exception{
        Connection cnx = null;
        PreparedStatement stmUsuario = null;
        PreparedStatement stmFarmaceuta = null;
        try {
            cnx = db.getConnection();
            cnx.setAutoCommit(false);

            String sqlUsuario="insert into usuarios (idUsuario, claveUsuario, nombreUsuario, tipoUsuario) values(?,?,?,?)";
            stmUsuario = cnx.prepareStatement(sqlUsuario);
            stmUsuario.setString(1, f.getId());
            stmUsuario.setString(2, f.getClave());
            stmUsuario.setString(3, f.getNombre());
            stmUsuario.setString(4, "FARMACEUTA");
            int countUsuario = stmUsuario.executeUpdate();
            if (countUsuario == 0) {
                throw new Exception("Farmaceuta ya existe en usuarios");
            }

            String sqlFarmaceuta="insert into farmaceutas (usuarios_idUsuario) values(?)";
            stmFarmaceuta = cnx.prepareStatement(sqlFarmaceuta);
            stmFarmaceuta.setString(1, f.getId());
            int countFarmaceuta = stmFarmaceuta.executeUpdate();
            if (countFarmaceuta == 0) {
                throw new Exception("No se pudo crear el registro de farmaceuta");
            }

            cnx.commit();
        } catch (SQLException ex) {
            if (cnx != null) try { cnx.rollback(); } catch (SQLException ignored) {}
            throw new Exception("Error al crear farmaceuta: " + ex.getMessage());
        } finally {
            if (stmUsuario != null) try { stmUsuario.close(); } catch (SQLException ignored) {}
            if (stmFarmaceuta != null) try { stmFarmaceuta.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public Farmaceuta read(String id) throws Exception{
        String sql="select u.idUsuario, u.claveUsuario, u.nombreUsuario " +
                "from usuarios u inner join farmaceutas f on u.idUsuario = f.usuarios_idUsuario "+
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
                throw new Exception ("Farmaceuta no Existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al leer farmaceuta: " + ex.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void update(Farmaceuta f) throws Exception{
        String sql="update usuarios set claveUsuario=?, nombreUsuario=? where idUsuario=?";
        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, f.getClave());
            stm.setString(2, f.getNombre());
            stm.setString(3, f.getId());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Farmaceuta no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar farmaceuta: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public void delete(Farmaceuta f) throws Exception{
        String sql="delete from usuarios where idUsuario=?";
        Connection cnx = null;
        PreparedStatement stm = null;
        try {
            cnx = db.getConnection();
            stm = cnx.prepareStatement(sql);
            stm.setString(1, f.getId());
            int count = stm.executeUpdate();
            if (count==0){
                throw new Exception("Farmaceuta no existe");
            }
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar farmaceuta: " + ex.getMessage());
        } finally {
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
    }

    public List<Farmaceuta> findByNombre(Farmaceuta filtro){
        List<Farmaceuta> resultado = new ArrayList<>();
        String sql="select u.idUsuario, u.nombreUsuario, u.claveUsuario from usuarios u " +
                "inner join farmaceutas f on u.idUsuario = f.usuarios_idUsuario "+
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
            // Log error
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stm != null) try { stm.close(); } catch (SQLException ignored) {}
            if (cnx != null) try { cnx.close(); } catch (SQLException ignored) {}
        }
        return resultado;
    }

    private Farmaceuta from(ResultSet rs){
        try {
            Farmaceuta f= new Farmaceuta();
            f.setId(rs.getString("idUsuario"));
            f.setNombre(rs.getString("nombreUsuario"));
            f.setClave(rs.getString("claveUsuario"));
            return f;
        } catch (SQLException ex) {
            return null;
        }
    }
}