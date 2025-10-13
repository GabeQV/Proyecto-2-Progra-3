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

    public List<Medico> findAll() {
        Medico filtro = new Medico();
        filtro.setNombre("");
        return medicoDao.findByNombre(filtro);
    }


    // =============== FARMACEUTAS ===============
    public void createFarmaceuta(Farmaceuta e) throws Exception {
        Farmaceuta result = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getFarmaceutas().add(e);
        } else {
            throw new Exception("Farmaceuta ya existe");
        }
    }

    public Farmaceuta readFarmaceuta(Farmaceuta e) throws Exception {
        Farmaceuta result = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Farmaceuta no existe");
        }
    }

    public void deleteFarmaceuta(Farmaceuta e) throws Exception {
        Farmaceuta result = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            data.getFarmaceutas().remove(result);
        } else {
            throw new Exception("Farmaceuta no existe");
        }
    }

    public void updateFarmaceuta(Farmaceuta e) throws Exception {
        Farmaceuta found = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (found == null) throw new Exception("Farmaceuta no existe");
        found.setNombre(e.getNombre());
        stop();
    }

    public List<Farmaceuta> findAllFarmaceutas() {
        return data.getFarmaceutas();
    }


    // =============== PACIENTES ===============
    public void createPaciente(Paciente e) throws Exception {
        Paciente result = data.getPacientes().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getPacientes().add(e);
        } else {
            throw new Exception("Paciente ya existe");
        }
    }

    public Paciente readPaciente(Paciente e) throws Exception {
        Paciente result = data.getPacientes().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Paciente no existe");
        }
    }

    public void deletePaciente(Paciente e) throws Exception {
        Paciente result = data.getPacientes().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            data.getPacientes().remove(result);
        } else {
            throw new Exception("Paciente no existe");
        }
    }

    public void updatePaciente(Paciente e) throws Exception {
        Paciente found = data.getPacientes().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (found == null) throw new Exception("Paciente no existe");
        found.setNombre(e.getNombre());
        found.setTelefono(e.getTelefono());
        found.setFechaNacimiento(e.getFechaNacimiento());
        stop();
    }

    public List<Paciente> findAllPacientes() {
        return data.getPacientes();
    }


    // =============== RECETAS ===============
    public void createReceta(Receta e) throws Exception {

        Receta result = data.getRecetas().stream()
                .filter(i -> i.getId() != null && i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getRecetas().add(e);
        } else {
            throw new Exception("Receta ya existe");
        }
    }

    public Receta readReceta(Receta e) throws Exception {
        Receta result = data.getRecetas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Receta no existe");
        }
    }

    public void deleteRecetas(Receta e) throws Exception {
        Receta result = data.getRecetas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            data.getRecetas().remove(result);
        } else {
            throw new Exception("Receta no existe");
        }
    }

    public List<Receta> findAllRecetas() {
        return data.getRecetas();
    }

    public List<Receta> findRecetasByPacienteId(String pacienteId) {
        return data.getRecetas().stream()
                .filter(r -> r.getPaciente() != null && pacienteId.equals(r.getPaciente().getId()))
                .toList();
    }

    public void updateReceta(Receta r) throws Exception {
        Receta found = data.getRecetas().stream()
                .filter(i -> i.getId().equals(r.getId()))
                .findFirst()
                .orElse(null);
        if (found == null) throw new Exception("Receta no existe");
        found.setEstado(r.getEstado());
        stop();
    }

    public boolean findRecetaById(String id) {
        return data.getRecetas().stream()
                .anyMatch(r -> r.getId() != null && r.getId().equals(id));
    }

    // =============== MEDICAMENTOS ===============
    public void createMedicamento(Medicamento e) throws Exception {
        Medicamento result = data.getMedicamentos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getMedicamentos().add(e);
        } else {
            throw new Exception("Medicamento ya existe");
        }
    }

    public Medicamento readMedicamento(Medicamento e) throws Exception {
        Medicamento result = data.getMedicamentos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Medicamento no existe");
        }
    }

    public void deleteMedicamento(Medicamento e) throws Exception {
        Medicamento result = data.getMedicamentos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            data.getMedicamentos().remove(result);
        } else {
            throw new Exception("Medicamento no existe");
        }
    }

    public void updateMedicamento(Medicamento e) throws Exception {
        Medicamento found = data.getMedicamentos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (found == null) throw new Exception("Medicamento no existe");
        found.setNombre(e.getNombre());
        found.setPresentacion(e.getPresentacion());
        stop();
    }

    public List<Medicamento> findMedicamentosByNombre(String nombre) {
        String filtro = nombre.trim().toLowerCase();
        return data.getMedicamentos().stream()
                .filter(m -> m.getNombre() != null && m.getNombre().toLowerCase().contains(filtro))
                .toList();
    }

    public List<Medicamento> findMedicamentosByCodigo(String codigo) {
        String filtro = codigo.trim().toLowerCase();
        return data.getMedicamentos().stream()
                .filter(m -> m.getId() != null && m.getId().toLowerCase().contains(filtro))
                .toList();
    }

    public List<Medicamento> findAllMedicamentos() {
        return data.getMedicamentos();
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