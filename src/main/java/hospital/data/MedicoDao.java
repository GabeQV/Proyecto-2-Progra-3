package hospital.data;

import hospital.logic.Medico;
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
        String sql="insert into Medico (usuarios_idUsuario, usuarios_claveUsuario, usuarios_nombreUsuario, especialidad)"+
                "values(?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getId());
        stm.setString(2, m.getClave());
        stm.setString(3, m.getNombre());
        stm.setString(4, m.getEspecialidad());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico ya existe");
        }
    }

    public Medico read(String id) throws Exception{
        String sql="select * from Medico m "+
                "where m.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        Medico m;
        if (rs.next()) {
            m = from(rs,"m");
            return m;
        }
        else{
            throw new Exception ("Medico no Existe");
        }
    }

    public void update(Medico m) throws Exception{
        String sql="update medico set usuarios_claveUsuario=?,usuarios_nombreUsuario=?,especialidad=?"+
                "where usuarios_idUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getClave());
        stm.setString(2, m.getNombre());
        stm.setString(3, m.getEspecialidad());
        stm.setString(4, m.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico ya existe");
        }
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public void delete(Medico f) throws Exception{
        String sql="delete from Medico where usuarios_idUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado = new ArrayList<Medico>();
        try {
            String sql="select * from Medico m "+
                    "where m.usuarios_nombreUsuario like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Medico m;
            while (rs.next()) {
                m= from(rs,"m");
                resultado.add(m);
            }
        } catch (SQLException ex) {  }
        return resultado;
    }

    private Medico from(ResultSet rs, String alias){
        try {
            Medico m= new Medico();
            m.setId(rs.getString(alias + ".usuarios_idUsuario"));
            m.setNombre(rs.getString(alias + ".usuarios_nombreUsuario"));
            m.setEspecialidad(rs.getString(alias + ".especialidad"));
            return m;
        } catch (SQLException ex) {
            return null;
        }
    }
}