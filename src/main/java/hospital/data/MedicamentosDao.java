package hospital.data;

import hospital.logic.Medicamento;
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
        String sql="insert into Medicamentos (idMedicamento, nombreMedicamento, presentacionMedicamento)"+
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getNombre());
        stm.setString(3, e.getPresentacion());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento ya existe");
        }
    }

    public Medicamento read(String id) throws Exception{
        String sql="select * from Medicamentos m "+
                "where m.idMedicamento=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        Medicamento m;
        if (rs.next()) {
            m = from(rs,"m");
            return m;
        }
        else{
            throw new Exception ("Medicamento no Existe");
        }
    }

    public void update(Medicamento p) throws Exception{
        String sql="update medicamento set nombreMedicamento=?,presentacionMedicamento=?"+
                "where idMedicamento=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getNombre());
        stm.setString(2, p.getPresentacion());
        stm.setString(3, p.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento ya existe");
        }
        if (count==0){
            throw new Exception("Medicamento no existe");
        }
    }

    public void delete(Medicamento m) throws Exception{
        String sql="delete from Medicamentos where idMedicamento=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento no existe");
        }
    }

    public List<Medicamento> findByNombre(Medicamento filtro){
        List<Medicamento> resultado = new ArrayList<Medicamento>();
        try {
            String sql="select * from Medicamentos m "+
                    "where m.nombreMedicamento like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Medicamento m;
            while (rs.next()) {
                m= from(rs,"m");
                resultado.add(m);
            }
        } catch (SQLException ex) {  }
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