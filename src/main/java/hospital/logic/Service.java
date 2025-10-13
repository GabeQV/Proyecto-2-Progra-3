package hospital.logic;

import hospital.data.*;
import hospital.data.MedicoDao;


import java.util.List;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private MedicoDao medicoDao;
    private FarmaceutaDao  farmaceutaDao;
    private PacienteDao pacienteDao;
    private RecetasDao recetasDao;
    private MedicamentosDao  medicamentosDao;

    private Service(){
        try{
            medicoDao =new MedicoDao();
            farmaceutaDao =new FarmaceutaDao();
            pacienteDao =new PacienteDao();
            recetasDao = new RecetasDao();
            medicamentosDao = new MedicamentosDao();
        }
        catch(Exception e){
            System.exit(-1);
        }
    }

    public void stop(){
        try {
            Database.instance().close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // =============== MEDICOS ===============
    public void createMedico(Medico e) throws Exception {
        medicoDao.create(e);
    }

    public Medico readMedico(Medico e) throws Exception {
        return  medicoDao.read(e.getId());
    }

    public void deleteMedico(Medico e) throws Exception {
        medicoDao.delete(e);
    }

    public void updateMedico(Medico e) throws Exception {
        medicoDao.update(e);
        stop();
    }

    public List<Medico> findAllMedicos() {
        Medico filtro = new Medico();
        filtro.setNombre("");
        return medicoDao.findByNombre(filtro);
    }


    // =============== FARMACEUTAS ===============
    public void createFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.create(e);
    }

    public Farmaceuta readFarmaceuta(Farmaceuta e) throws Exception {
        return  farmaceutaDao.read(e.getId());
    }

    public void deleteFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.delete(e);
    }

    public void updateFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.update(e);
        stop();
    }

    public List<Farmaceuta> findAllFarmaceutas() {
        Farmaceuta filtro = new Farmaceuta();
        filtro.setNombre("");
        return farmaceutaDao.findByNombre(filtro);
    }


    // =============== PACIENTES ===============
    public void createPaciente(Paciente e) throws Exception {
        pacienteDao.create(e);
    }

    public Paciente readPaciente(Paciente e) throws Exception {
        return pacienteDao.read(e.getId());
    }

    public void deletePaciente(Paciente e) throws Exception {
        pacienteDao.delete(e);
    }

    public void updatePaciente(Paciente e) throws Exception {
        pacienteDao.update(e);
        stop();
    }

    public List<Paciente> findAllPacientes() {
        Paciente filtro = new Paciente();
        filtro.setNombre("");
        return pacienteDao.findByNombre(filtro);
    }


    // =============== RECETAS ===============
    public void createReceta(Receta e) throws Exception {}

    //public Receta readReceta(Receta e) throws Exception {}

    public void deleteRecetas(Receta e) throws Exception {}

    //public List<Receta> findAllRecetas() {}

    //public List<Receta> findRecetasByPacienteId(String pacienteId) {}

    public void updateReceta(Receta r) throws Exception {

    }

    //public boolean findRecetaById(String id) {}

    // =============== MEDICAMENTOS ===============
    public void createMedicamento(Medicamento e) throws Exception {
        medicamentosDao.create(e);
    }

    public Medicamento readMedicamento(Medicamento e) throws Exception {
        return medicamentosDao.read(e.getId());
    }

    public void deleteMedicamento(Medicamento e) throws Exception {
        medicamentosDao.delete(e);
    }

    public void updateMedicamento(Medicamento e) throws Exception {
        medicamentosDao.update(e);
        stop();
    }

    public List<Medicamento> findMedicamentosByNombre(String nombre) {
        Medicamento filtro = new Medicamento();
        filtro.setNombre(nombre);
        return medicamentosDao.findByNombre(filtro);
    }

    //public List<Medicamento> findMedicamentosByCodigo(String codigo) {}

    public List<Medicamento> findAllMedicamentos() {
        Medicamento filtro = new Medicamento();
        filtro.setNombre("");
        return medicamentosDao.findByNombre(filtro);
    }


    // =============== AUTENTICACIÓN Y CONTRASEÑA ===============
    public Usuario login(String id, String clave) throws Exception {

        if ("ADM".equals(id) && "ADM".equals(clave)) {
            return new Admin();
        }

        Usuario user = null;

        user = data.getMedicos().stream()
                .filter(u -> u.getId().equals(id) && u.getClave().equals(clave))
                .findFirst()
                .orElse(null);

        if (user == null) {
            user = data.getFarmaceutas().stream()
                    .filter(u -> u.getId().equals(id) && u.getClave().equals(clave))
                    .findFirst()
                    .orElse(null);
        }

        if (user == null) throw new Exception("Usuario o clave incorrecta");

        return user;
    }

    public void cambiarClave(String id, String claveActual, String nuevaClave, String nuevaClaveConfirm) throws Exception {

        if ("ADM".equals(id)) {
            throw new Exception("No es posible cambiar la clave del administrador especial.");
        }

        Usuario user = null;

        user = data.getMedicos().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) user = data.getFarmaceutas().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) throw new Exception("Usuario no encontrado");

        if (!user.getClave().equals(claveActual)) throw new Exception("La clave actual no es correcta");

        if (!nuevaClave.equals(nuevaClaveConfirm)) throw new Exception("La nueva clave no coincide");

        user.setClave(nuevaClave);
        stop();
    }

}