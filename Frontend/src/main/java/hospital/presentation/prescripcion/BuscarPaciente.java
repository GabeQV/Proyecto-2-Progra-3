package hospital.presentation.prescripcion;

import hospital.logic.Paciente;
import hospital.logic.Service;
import hospital.presentation.pacientes.TableModel;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

public class BuscarPaciente extends JDialog implements PropertyChangeListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JTable pacientesTable;
    Controller controllerPrescripcion;
    Model model;

    public BuscarPaciente(JFrame parent, Model model) {
        super(parent, "Buscar Paciente", true);
        setContentPane(contentPane);
        setSize(500, 300);
        setLocationRelativeTo(parent);
        parent.setVisible(true);
        parent.setLocationRelativeTo(null);


        this.model = model;

        int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.FECHA_NACIMIENTO, TableModel.TELEFONO};
        pacientesTable.setModel(new TableModel(cols, Service.instance().findAllPacientes()));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        pacientesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && pacientesTable.getSelectedRow() != -1) {
                    Paciente seleccionado = ((TableModel) pacientesTable.getModel()).getRowAt(pacientesTable.getSelectedRow());
                    model.setCurrentPaciente(seleccionado);
                    model.getCurrentReceta().setPaciente(seleccionado);
                    model.setCurrentReceta(model.getCurrentReceta());
                    dispose();
                }
            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarPacientes();
            }
        });
        comboBox1.addActionListener(e -> buscarPacientes());
    }

    private void buscarPacientes() {
        String criterio = textField1.getText();
        String tipoBusqueda = (String) comboBox1.getSelectedItem();
        List<Paciente> result;

        if (criterio.isEmpty()) {
            result = Service.instance().findAllPacientes();
        } else {
            Paciente filtro = new Paciente();
            if ("Nombre".equals(tipoBusqueda)) {
                filtro.setNombre(criterio);
                result = Service.instance().findAllPacientes().stream()
                        .filter(p -> p.getNombre().toLowerCase().contains(criterio.toLowerCase()))
                        .toList();
            } else { // "Codigo"
                try {
                    filtro.setId(criterio);
                    result = Collections.singletonList(Service.instance().readPaciente(filtro));
                } catch (Exception e) {
                    result = Collections.emptyList();
                }
            }
        }
        int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.FECHA_NACIMIENTO, TableModel.TELEFONO};
        pacientesTable.setModel(new TableModel(cols, result));
    }

    private void onOK() {
        int row = pacientesTable.getSelectedRow();
        if (row != -1) {
            Paciente seleccionado = ((TableModel) pacientesTable.getModel()).getRowAt(row);
            model.setCurrentPaciente(seleccionado);
            model.setCurrentReceta(model.getCurrentReceta());
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.FECHA_NACIMIENTO, TableModel.TELEFONO};
                pacientesTable.setModel(new TableModel(cols, Service.instance().findAllPacientes()));
                break;
            case Model.CURRENT_PACIENTE:
               model.getCurrentReceta().setPaciente(model.getCurrentPaciente());

        }
    }
}
