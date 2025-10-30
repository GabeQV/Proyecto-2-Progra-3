package hospital.presentation.prescripcion;

import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Sesion;

public class Controller {
    PrescripcionView view;
    Model model;

    public Controller(PrescripcionView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void guardarRecetaActual(Receta r) throws Exception {
        if (r.getId() == null || r.getId().isEmpty()) {
            r.setId(generarIdReceta());
        }
        if (r.getFecha() == null) {
            r.setFecha(java.time.LocalDate.now());
        }
        if (r.getEstado() == null || r.getEstado().isEmpty()) {
            r.setEstado("Confeccionado");
        }
        r.setUsuario(Sesion.instance().getUsuarioActual());

        Service.instance().createReceta(r);

        this.clear();
    }

    public void descartarMedicamento() {
        Receta receta = model.getCurrentReceta();
        if (receta != null && receta.getMedicamento() != null) {
            receta.setMedicamento(null);
            receta.setCantidad("");
            receta.setDuracion("");
            receta.setIndicaciones("");
            model.setCurrentReceta(receta);
        }
    }

    public void clear() {
        model.setCurrentReceta(new Receta());
        model.setCurrentPaciente(new Paciente());
    }

    private String generarIdReceta() {
        java.util.Random rand = new java.util.Random();
        String nuevoId;
        boolean existe;
        do {
            int idNum = rand.nextInt(900) + 100; // 100 - 999
            nuevoId = String.format("R%03d", idNum);
            try {
                Receta temp = new Receta();
                temp.setId(nuevoId);
                Service.instance().readReceta(temp);
                existe = true;
            } catch (Exception e) {
                existe = false;
            }
        } while (existe);
        return nuevoId;
    }
}