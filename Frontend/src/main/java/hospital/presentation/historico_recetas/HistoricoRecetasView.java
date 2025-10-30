package hospital.presentation.historico_recetas;

import hospital.logic.Receta;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HistoricoRecetasView implements PropertyChangeListener {
    private JTextField idPacienteTextField;
    private JTextField idRecetaTextField;
    private JButton buscarIdPacienteButton;
    private JButton buscarIdRecetaButton;
    private JTable recetasTable;
    private JPanel historicoRecetasPanel;
    private JPanel Busqueda;
    private JLabel nombrePacienteLabel;

    Controller controller;
    Model model;

    public JPanel getHitoricoRecetasPanel() { return historicoRecetasPanel; }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public HistoricoRecetasView() {
        buscarIdPacienteButton.addActionListener(e -> {
            String pacienteId = idPacienteTextField.getText();
            if (pacienteId == null || pacienteId.isEmpty()) {
                JOptionPane.showMessageDialog(historicoRecetasPanel, "Ingrese ID de paciente para buscar", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try {
                boolean encontrado = controller.searchByPacienteId(pacienteId);
                if (!encontrado) {
                    JOptionPane.showMessageDialog(historicoRecetasPanel, "No se encontraron recetas para ese paciente", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(historicoRecetasPanel, "Error al buscar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buscarIdRecetaButton.addActionListener(e -> {
            String recetaId = idRecetaTextField.getText();
            if (recetaId == null || recetaId.isEmpty()) {
                try {
                    controller.clear();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(historicoRecetasPanel, "Error al limpiar la búsqueda: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            try {
                boolean encontrado = controller.searchByRecetaId(recetaId);
                if (!encontrado) {
                    JOptionPane.showMessageDialog(historicoRecetasPanel, "No se encontró una receta con ese ID", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {

                JOptionPane.showMessageDialog(historicoRecetasPanel, "Error al buscar receta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        recetasTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (recetasTable.getSelectedRow() != -1) {
                    int row = recetasTable.getSelectedRow();
                    Receta seleccionado = ((TableModel) recetasTable.getModel()).getRowAt(row);
                    model.setCurrent(seleccionado);
                    if (seleccionado != null && seleccionado.getPaciente() != null) {
                        nombrePacienteLabel.setText(seleccionado.getPaciente().getNombre());
                    } else {
                        nombrePacienteLabel.setText("PACIENTE");
                    }
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {
                        TableModel.ID,
                        TableModel.MEDICAMENTO,
                        TableModel.PRESENTACION,
                        TableModel.CANTIDAD,
                        TableModel.INDICACIONES,
                        TableModel.DURACION,
                        TableModel.ESTADO
                };
                recetasTable.setModel(new TableModel(cols, model.getList()));

                if (!model.getList().isEmpty()) {
                    Receta primeraReceta = model.getList().get(0);
                    if (primeraReceta != null && primeraReceta.getPaciente() != null) {
                        nombrePacienteLabel.setText(primeraReceta.getPaciente().getNombre());
                    }
                } else {
                    nombrePacienteLabel.setText("PACIENTE");
                }
                break;
            case Model.CURRENT:
                Receta r = model.getCurrent();
                if (r != null && r.getPaciente() != null) {
                    nombrePacienteLabel.setText(r.getPaciente().getNombre());
                }
                break;
        }
        historicoRecetasPanel.revalidate();
    }
}