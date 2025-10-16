package hospital.data;

import hospital.logic.Paciente;

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
        String sql="insert into Pacientes (idPaciente, nombrePaciente, fechaNacimiento, telefono)"+
                "values(?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getNombre());
        stm.setDate(3, Date.valueOf(p.getFechaNacimiento()));
        stm.setString(4, p.getTelefono());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Paciente ya existe");
        }
    }

    public Paciente read(String id) throws Exception{
        String sql="select * from Pacientes p "+
                "where p.idPaciente=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        Paciente p;
        if (rs.next()) {
            p = from(rs,"p");
            return p;
        }
        else{
            throw new Exception ("Paciente no Existe");
        }
    }

    public void update(Paciente p) throws Exception{
        String sql="update paciente set nombrePaciente=?, fechaNacimiento=?, telefono=?"+
                "where idPaciente=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getNombre());
        stm.setDate(2, Date.valueOf(p.getFechaNacimiento()));
        stm.setString(3, p.getTelefono());
        stm.setString(4, p.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Paciente ya existe");
        }
        if (count==0){
            throw new Exception("Paciente no existe");
        }
    }

    public void delete(Paciente o) throws Exception{
        String sql="delete from Pacientes where idPaciente=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Paciente no existe");
        }
    }

    public List<Paciente> findByNombre(Paciente filtro){
        List<Paciente> resultado = new ArrayList<Paciente>();
        try {
            String sql="select * from Pacientes p "+
                    "where p.nombrePaciente like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Paciente p;
            while (rs.next()) {
                p = from(rs,"p");
                resultado.add(p);
            }
        } catch (SQLException ex) {  }
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