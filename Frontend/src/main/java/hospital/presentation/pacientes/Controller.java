package hospital.presentation.pacientes;

import hospital.logic.Paciente;

public class Controller {
    PacientesView view;
    Model model;

    public Controller(PacientesView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        try {
            this.refreshList();
        } catch (Exception e) {
            System.err.println("Error cargando la lista inicial de pacientes: " + e.getMessage());
        }
    }

    public void create(Paciente e) throws Exception {
        Service.instance().createPaciente(e);
        model.setCurrent(new Paciente());
    }

    public void update(Paciente e) throws Exception {
        Service.instance().updatePaciente(e);
        model.setCurrent(new Paciente());
    }

    public void read(String id) throws Exception {
        Paciente e = new Paciente();
        e.setId(id);
        Paciente result = Service.instance().readPaciente(e);
        model.setCurrent(result);
    }

    public void delete(Paciente e) throws Exception {
        Service.instance().deletePaciente(e);
        model.setCurrent(new Paciente());
    }

    public void clear() {
        model.setCurrent(new Paciente());
    }

    public void refreshList() {
        try {
            model.setList(Service.instance().findAllPacientes());
        } catch (Exception e) {
            System.err.println("Error refrescando la lista de pacientes: " + e.getMessage());
        }
    }
}