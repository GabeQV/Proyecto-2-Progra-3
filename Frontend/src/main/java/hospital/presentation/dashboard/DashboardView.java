package hospital.presentation.dashboard;

import com.github.lgooddatepicker.components.DatePicker;
import hospital.logic.Medicamento;
import hospital.logic.Receta;
import hospital.logic.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardView extends JPanel implements PropertyChangeListener  {

    private JPanel DashboardPanel;
    private JComboBox<String> medicamentosComboBox;
    private JButton filtrarButton1;
    private JButton addButton;
    private JTable medicamentosTable;
    private JPanel panel2;
    private DatePicker hastaDatePicker;
    private DatePicker desdeDatePicker;
    private JPanel lineChartPanel;
    private JPanel pieChartPanel;
    LineChart lineChart;
    PieChart pieChart;

    Controller controller;
    Model model;

    private List<String> medicamentosSeleccionados = new ArrayList<>();

    public JPanel getDashboardPanel() {return DashboardPanel;}

    public DashboardView() {
        lineChart = new LineChart();
        pieChart = new PieChart();

        lineChartPanel.setLayout(new BorderLayout());
        lineChartPanel.add(lineChart, BorderLayout.CENTER);

        pieChartPanel.setLayout(new BorderLayout());
        pieChartPanel.add(pieChart, BorderLayout.CENTER);

        try {
            List<Medicamento> medicamentos = Service.instance().findAllMedicamentos();
            String[] medicamentosStr = new String[medicamentos.size()];
            for (int i = 0; i < medicamentos.size(); i++) {
                medicamentosStr[i] = medicamentos.get(i).getNombre();
            }
            medicamentosComboBox.setModel(new DefaultComboBoxModel<>(medicamentosStr));
        } catch (Exception e) {
            System.err.println("Error al cargar medicamentos para el dashboard: " + e.getMessage());
            medicamentosComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Error al cargar"}));
        }


        addButton.addActionListener(e -> {
            String seleccionado = (String) medicamentosComboBox.getSelectedItem();
            if (seleccionado != null && !medicamentosSeleccionados.contains(seleccionado)) {
                medicamentosSeleccionados.add(seleccionado);
            }
            actualizarVista();
        });

        filtrarButton1.addActionListener(e -> actualizarVista());
    }

    private void actualizarVista() {
        LocalDate desde = desdeDatePicker.getDate();
        LocalDate hasta = hastaDatePicker.getDate();
        controller.findRecetasMultiple(desde, hasta, medicamentosSeleccionados);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.MEDICAMENTO, TableModel.MES, TableModel.CANTIDAD};
                medicamentosTable.setModel(new TableModel(cols, model.getList()));
                lineChart.setData(model.getList());
                pieChart.setData(model.getList());
                break;
            case Model.CURRENT:
                break;
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
}