package hospital.presentation.dashboard;

import hospital.logic.Medicamento;
import hospital.logic.Receta;
import hospital.logic.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    Model model;
    DashboardView view;

    public Controller(DashboardView view, Model model) {
        this.model = model;
        this.view = view;
    }

    public void findRecetasMultiple(LocalDate from, LocalDate to, List<String> medicamentosSeleccionados) {
        try {
            List<Receta> todasLasRecetas = Service.instance().findAllRecetas();

            List<Receta> filtradas = todasLasRecetas.stream()
                    .filter(r -> r.getFecha() != null
                            && (from == null || !r.getFecha().isBefore(from))
                            && (to == null || !r.getFecha().isAfter(to))
                            && (medicamentosSeleccionados == null || medicamentosSeleccionados.isEmpty()
                            || (r.getMedicamento() != null && medicamentosSeleccionados.contains(r.getMedicamento().getNombre())))
                    )
                    .collect(Collectors.toList());

            model.setList(filtradas);

        } catch (Exception e) {
            System.err.println("Error al buscar recetas para el dashboard: " + e.getMessage());
            model.setList(Collections.emptyList());
        }
    }

    public List<String> eliminarMedicamento(String nombre, List<String> medicamentosSeleccionados) {
        if (medicamentosSeleccionados == null || medicamentosSeleccionados.isEmpty()) {
            return medicamentosSeleccionados == null ? List.of() : List.copyOf(medicamentosSeleccionados);
        }

        if (nombre == null || nombre.isEmpty()) {
            return List.copyOf(medicamentosSeleccionados);
        }

        List<String> nueva = medicamentosSeleccionados.stream()
                .filter(n -> !n.equals(nombre))
                .collect(Collectors.toList());

        return Collections.unmodifiableList(nueva);
    }
}