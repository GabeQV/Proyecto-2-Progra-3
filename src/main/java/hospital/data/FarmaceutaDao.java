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
        try {
            cnx = db.getConnection();
            cnx.setAutoCommit(false);

            String sqlUsuario="insert into usuarios (idUsuario, claveUsuario, nombreUsuario, tipoUsuario) values(?,?,?,?)";
            PreparedStatement stmUsuario = cnx.prepareStatement(sqlUsuario);
            stmUsuario.setString(1, f.getId());
            stmUsuario.setString(2, f.getClave());
            stmUsuario.setString(3, f.getNombre());
            stmUsuario.setString(4, "FARMACEUTA");
            int countUsuario = stmUsuario.executeUpdate();
            if (countUsuario == 0) {
                throw new Exception("Farmaceuta ya existe en usuarios");
            }

            String sqlFarmaceuta="insert into farmaceutas (usuarios_idUsuario) values(?)";
            PreparedStatement stmFarmaceuta = cnx.prepareStatement(sqlFarmaceuta);
            stmFarmaceuta.setString(1, f.getId());
            int countFarmaceuta = stmFarmaceuta.executeUpdate();
            if (countFarmaceuta == 0) {
                throw new Exception("No se pudo crear el registro de farmaceuta");
            }

            cnx.commit();
        } catch (SQLException ex) {
            if (cnx != null) cnx.rollback();
            throw new Exception("Error al crear farmaceuta: " + ex.getMessage());
        } finally {
            if (cnx != null) {
                cnx.setAutoCommit(true);
                cnx.close();
            }
        }
    }

    public Farmaceuta read(String id) throws Exception{
        String sql="select u.idUsuario, u.claveUsuario, u.nombreUsuario " +
                "from usuarios u inner join farmaceutas f on u.idUsuario = f.usuarios_idUsuario "+
                "where u.idUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs);
        }
        else{
            throw new Exception ("Farmaceuta no Existe");
        }
    }

    public void update(Farmaceuta f) throws Exception{
        String sql="update usuarios set claveUsuario=?, nombreUsuario=? where idUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getClave());
        stm.setString(2, f.getNombre());
        stm.setString(3, f.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Farmaceuta no existe");
        }
    }

    public void delete(Farmaceuta f) throws Exception{
        String sql="delete from usuarios where idUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Farmaceuta no existe");
        }
    }

    public List<Farmaceuta> findByNombre(Farmaceuta filtro){
        List<Farmaceuta> resultado = new ArrayList<>();
        try {
            String sql="select u.idUsuario, u.nombreUsuario, u.claveUsuario from usuarios u " +
                    "inner join farmaceutas f on u.idUsuario = f.usuarios_idUsuario "+
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