package hospital.presentation.catalogo;

import hospital.logic.Medicamento;

import java.util.List;

public class Controller {
    CatologoView view;
    Model model;

    public Controller(CatologoView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        try {
            refreshList(); // Carga inicial de datos
        } catch (Exception e) {
            System.err.println("Error cargando medicamentos: " + e.getMessage());
        }
    }

    public void create(Medicamento e) throws Exception {
        Service.instance().createMedicamento(e);
        model.setCurrent(new Medicamento());
    }

    public void delete(String id) throws Exception {
        Medicamento med = new Medicamento();
        med.setId(id);
        Service.instance().deleteMedicamento(med);
        model.setCurrent(new Medicamento());
    }

    public void update(Medicamento e) throws Exception {
        Service.instance().updateMedicamento(e);
        model.setCurrent(new Medicamento());
    }

    public List<Medicamento> buscarPorNombre(String nombre) throws Exception {
        return Service.instance().findMedicamentosByNombre(nombre);
    }

    public List<Medicamento> buscarPorCodigo(String codigo) throws Exception {
        return Service.instance().findMedicamentosByCodigo(codigo);
    }

    public void clear() {
        model.setCurrent(new Medicamento());
    }

    public void refreshList() throws Exception {
        model.setList(Service.instance().findAllMedicamentos());
    }
}