package hospital.presentation.historico_recetas;


import hospital.logic.Receta;
import hospital.logic.Service;

import java.util.List;

public class Controller {
    HistoricoRecetasView view;
    Model model;

    public Controller(HistoricoRecetasView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllRecetas());
    }

    public void create(Receta e) throws Exception {
        Service.instance().createReceta(e);
        model.setCurrent(new Receta());
        model.setList(Service.instance().findAllRecetas());
    }

    public void read(String id) throws Exception {
        Receta r = new Receta();
        r.setId(id);
        try {
            model.setCurrent(Service.instance().readReceta(r));
        } catch (Exception ex) {
            Receta empty = new Receta();
            empty.setId(id);
            model.setCurrent(empty);
            throw ex;
        }
    }

    public void clear() {
        model.setList(Service.instance().findAllRecetas());
        model.setCurrent(new Receta());
    }

    public boolean searchByPacienteId(String pacienteId) {
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

    public boolean searchByRecetaId(String recetaId) {
        List<Receta> list = new java.util.ArrayList<>();
        try {
            Receta r = new Receta();
            r.setId(recetaId);
            Receta result = Service.instance().readReceta(r);
            if (result != null) {
                list.add(result);
                model.setCurrent(result);
            }
        } catch (Exception ex) {
        }
        model.setList(list);
        return !list.isEmpty();
    }
}
