package hospital.presentation.medicos;
import hospital.logic.Medico;
import hospital.logic.Service;

public class Controller {
    MedicosView view;
    Model model;

    public Controller(MedicosView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        try {
            refreshList();
        } catch (Exception e) {
            System.err.println("Error cargando médicos: " + e.getMessage());
        }
    }

    public void create(Medico e) throws Exception{
        Service.instance().createMedico(e);
        model.setCurrent(new Medico());
    }

    public void delete(Medico e) throws Exception{
        Service.instance().deleteMedico(e);
        model.setCurrent(new Medico());
    }

    public void update(Medico e) throws Exception {
        Service.instance().updateMedico(e);
        model.setCurrent(new Medico());
    }

    public void clear() {
        model.setCurrent(new Medico());
    }

    public void read(String id) throws Exception {
        Medico e = new Medico();
        e.setId(id);
        Medico result = Service.instance().readMedico(e);
        model.setCurrent(result);
    }

    public void refreshList() throws Exception {
        model.setList(Service.instance().findAllMedicos());
    }
}