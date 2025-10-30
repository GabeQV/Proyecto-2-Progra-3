package hospital.presentation.despacho;

import hospital.logic.Paciente;
import hospital.logic.Receta;
import hospital.logic.Service;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DespachoView implements PropertyChangeListener {
    private JTextField idField;
    private JTable recetasTable;
    private JButton procesarButton;
    private JButton listoButton;
    private JButton entregarButton;
    private JButton button4;
    private JButton buscarButton;
    private JPanel despachoView;
    private JLabel nombrePacienteLabel;


    Controller controller;
    Model model;

    public DespachoView() {
        buscarButton.addActionListener(e -> {
            String pacienteId = idField.getText();
            if (pacienteId == null || pacienteId.isEmpty()) {
                JOptionPane.showMessageDialog(despachoView, "Ingrese ID de paciente para buscar", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                Paciente p = new Paciente();
                p.setId(pacienteId);
                Paciente pacienteEncontrado = Service.instance().readPaciente(p);
                nombrePacienteLabel.setText(pacienteEncontrado.getNombre());

                controller.searchByPacienteId(pacienteId);

                if (model.getList().isEmpty()) {
                    JOptionPane.showMessageDialog(despachoView, "El paciente no tiene recetas pendientes", "Información", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                nombrePacienteLabel.setText("PACIENTE");
                JOptionPane.showMessageDialog(despachoView, "Error: " + ex.getMessage(), "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            }
        });

        procesarButton.addActionListener(e -> {
            try {
                controller.procesar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(despachoView, "Error al procesar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        listoButton.addActionListener(e -> {
            try {
                controller.listo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(despachoView, "Error al marcar como lista: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        entregarButton.addActionListener(e -> {
            try {
                controller.entregar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(despachoView, "Error al entregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        button4.addActionListener(e -> {
            try {
                controller.revertir();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(despachoView, "Error al revertir estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        recetasTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (recetasTable.getSelectedRow() != -1) {
                    int row = recetasTable.getSelectedRow();
                    Receta seleccionado = ((TableModel) recetasTable.getModel()).getRowAt(row);
                    controller.model.setCurrent(seleccionado);
                }
            }
        });
    }

    public JPanel getDespachoView() {
        return despachoView;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {
                        TableModel.MEDICAMENTO,
                        TableModel.PRESENTACION,
                        TableModel.CANTIDAD,
                        TableModel.INDICACIONES,
                        TableModel.DURACION,
                        TableModel.ESTADO
                };
                recetasTable.setModel(new TableModel(cols, model.getList()));
                break;
            case Model.CURRENT:
                updateButtons(model.getCurrent());
                break;
        }
        despachoView.revalidate();
    }

    private void updateButtons(Receta current) {
        String estado = current != null ? current.getEstado() : null;
        if (estado == null || estado.isEmpty() || model.getList().isEmpty()) {
            procesarButton.setEnabled(false);
            listoButton.setEnabled(false);
            entregarButton.setEnabled(false);
            button4.setEnabled(false);
            return;
        }

        procesarButton.setEnabled("Confeccionado".equalsIgnoreCase(estado));
        listoButton.setEnabled("En proceso".equalsIgnoreCase(estado));
        entregarButton.setEnabled("Lista".equalsIgnoreCase(estado));
        button4.setEnabled(estado.equalsIgnoreCase("En proceso") || estado.equalsIgnoreCase("Lista") || estado.equalsIgnoreCase("Entregada"));
    }
}