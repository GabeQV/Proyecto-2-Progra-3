package hospital.presentation.historico_recetas;

import hospital.logic.Receta;

import java.util.List;

public class Controller {
    HistoricoRecetasView view;
    Model model;

    public Controller(HistoricoRecetasView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        try {
            refreshList();
        } catch (Exception e) {
            System.err.println("Error cargando histórico de recetas: " + e.getMessage());
        }
    }

    public void clear() throws Exception {
        refreshList();
        model.setCurrent(new Receta());
    }

    public boolean searchByPacienteId(String pacienteId) throws Exception {
        List<Receta> list = Service.instance().findRecetasByPacienteId(pacienteId);
        model.setList(list);
        if (!list.isEmpty()) {
            model.setCurrent(list.get(0));
            return true;
        } else {
            model.setCurrent(new Receta());
            return false;
        }
    }

    public boolean searchByRecetaId(String recetaId) throws Exception {
        Receta r = new Receta();
        r.setId(recetaId);
        Receta result = Service.instance().readReceta(r);
        model.setList(List.of(result));
        model.setCurrent(result);
        return true;
    }

    public void refreshList() throws Exception {
        model.setList(Service.instance().findAllRecetas());
    }
}