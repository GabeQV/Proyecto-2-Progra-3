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
    public void create(Medico e) throws Exception{
        String sql="insert into Medico (id, clave, nombre, especialidad)"+
                "values(?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getClave());
        stm.setString(3, e.getNombre());
        stm.setString(4, e.getEspecialidad());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico ya existe");
        }
    }

    public Medico read(String id) throws Exception{
        String sql="select * from Medico m "+
                "inner join Especialidad e on m.especialidad=e.especialidad "+
                "where p.id=?";
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

    public void update(Medico p) throws Exception{
        String sql="update medico set clave=?,nombre=?,especialidad=?"+
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getClave());
        stm.setString(2, p.getNombre());
        stm.setString(3, p.getEspecialidad());
        stm.setString(4, p.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico ya existe");
        }
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public void delete(Medico o) throws Exception{
        String sql="delete from Medico where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado = new ArrayList<Medico>();
        try {
            String sql="select * from Medico p "+
                    "where p.nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Medico p;
            while (rs.next()) {
                p= from(rs,"p");
                resultado.add(p);
            }
        } catch (SQLException ex) {  }
        return resultado;
    }

    private Medico from(ResultSet rs, String alias){
        try {
            Medico p= new Medico();
            p.setId(rs.getString(alias + ".id"));
            p.setNombre(rs.getString(alias + ".nombre"));
            p.setEspecialidad(rs.getString(alias + ".especialidad"));
            return p;
        } catch (SQLException ex) {
            return null;
        }
    }
}