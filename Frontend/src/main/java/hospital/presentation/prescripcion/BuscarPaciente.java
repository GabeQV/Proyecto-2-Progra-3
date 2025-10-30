package hospital.presentation.prescripcion;

import hospital.logic.Paciente;
import hospital.presentation.pacientes.TableModel;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BuscarPaciente extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JComboBox<String> comboBox1;
    private JTable pacientesTable;
    Model model;

    public BuscarPaciente(JFrame parent, Model model) {
        super(parent, "Buscar Paciente", true);
        this.model = model;

        setContentPane(contentPane);
        setSize(500, 300);
        setLocationRelativeTo(parent);

        try {
            int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.FECHA_NACIMIENTO, TableModel.TELEFONO};
            pacientesTable.setModel(new TableModel(cols, Service.instance().findAllPacientes()));
        } catch (Exception e) {
            System.err.println("Error al cargar pacientes en el diálogo: " + e.getMessage());
            pacientesTable.setModel(new TableModel(new int[]{}, Collections.emptyList()));
        }


        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

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
                    onOK();
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

        try {
            if (criterio.isEmpty()) {
                result = Service.instance().findAllPacientes();
            } else {
                if ("Nombre".equals(tipoBusqueda)) {
                    result = Service.instance().findAllPacientes().stream()
                            .filter(p -> p.getNombre().toLowerCase().contains(criterio.toLowerCase()))
                            .collect(Collectors.toList());
                } else {
                    Paciente filtro = new Paciente();
                    filtro.setId(criterio);
                    try {
                        result = Collections.singletonList(Service.instance().readPaciente(filtro));
                    } catch (Exception e) {
                        result = Collections.emptyList();
                    }
                }
            }
            int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.FECHA_NACIMIENTO, TableModel.TELEFONO};
            pacientesTable.setModel(new TableModel(cols, result));
        } catch (Exception e) {
            System.err.println("Error buscando pacientes: " + e.getMessage());
            pacientesTable.setModel(new TableModel(new int[]{}, Collections.emptyList()));
        }
    }

    private void onOK() {
        int row = pacientesTable.getSelectedRow();
        if (row != -1) {
            Paciente seleccionado = ((TableModel) pacientesTable.getModel()).getRowAt(row);
            model.setCurrentPaciente(seleccionado);
            model.getCurrentReceta().setPaciente(seleccionado);
            model.setCurrentReceta(model.getCurrentReceta());
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}