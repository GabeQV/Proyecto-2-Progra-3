package hospital.presentation.despacho;

import hospital.logic.Receta;

import java.util.List;

public class Controller {
    DespachoView view;
    Model model;
    private String pacienteIdActual = null;

    public Controller(DespachoView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void searchByPacienteId(String pacienteId) throws Exception {
        pacienteIdActual = pacienteId;
        List<Receta> list = Service.instance().findRecetasByPacienteId(pacienteId);
        model.setList(list);
        if (list.isEmpty()) {
            model.setCurrent(new Receta());
        }
    }

    public void procesar() throws Exception {
        Receta r = model.getCurrent();
        r.setEstado("En proceso");
        Service.instance().updateReceta(r);
    }

    public void listo() throws Exception {
        Receta r = model.getCurrent();
        r.setEstado("Lista");
        Service.instance().updateReceta(r);
    }

    public void entregar() throws Exception {
        Receta r = model.getCurrent();
        r.setEstado("Entregada");
        Service.instance().updateReceta(r);
    }

    public void revertir() throws Exception {
        Receta r = model.getCurrent();
        String estado = r.getEstado();

        if ("En proceso".equalsIgnoreCase(estado)) r.setEstado("Confeccionado");
        else if ("Lista".equalsIgnoreCase(estado)) r.setEstado("En proceso");
        else if ("Entregada".equalsIgnoreCase(estado)) r.setEstado("Lista");
        else throw new Exception("No hay estado anterior para revertir");

        Service.instance().updateReceta(r);
    }


    public void refreshList() throws Exception {
        if (pacienteIdActual != null && !pacienteIdActual.isEmpty()) {
            model.setList(Service.instance().findRecetasByPacienteId(pacienteIdActual));
        }
    }
}