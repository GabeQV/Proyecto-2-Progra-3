package hospital.presentation.prescripcion;

import hospital.logic.Medicamento;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;

public class AgregarMedicamento extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> comboBox1;
    private JTextField textField1;
    private JTable medicamentosTable;

    Model model;

    public AgregarMedicamento(JFrame parent, Model model) {
        super(parent, "Agregar Medicamento", true);
        this.model = model;

        setContentPane(contentPane);
        setSize(500, 300);
        setLocationRelativeTo(parent);

        try {
            int[] cols = {MedicamentosTableModel.ID, MedicamentosTableModel.NOMBRE, MedicamentosTableModel.PRESENTACION};
            medicamentosTable.setModel(new MedicamentosTableModel(cols, Service.instance().findAllMedicamentos()));
        } catch (Exception e) {
            System.err.println("Error al cargar medicamentos en el diálogo: " + e.getMessage());
            medicamentosTable.setModel(new MedicamentosTableModel(new int[]{}, Collections.emptyList()));
        }


        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        medicamentosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && medicamentosTable.getSelectedRow() != -1) {
                    onOK();
                }
            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarMedicamentos();
            }
        });
        comboBox1.addActionListener(e -> buscarMedicamentos());
    }

    private void buscarMedicamentos() {
        String criterio = textField1.getText();
        String tipoBusqueda = (String) comboBox1.getSelectedItem();
        List<Medicamento> result;

        try {
            if (criterio.isEmpty()) {
                result = Service.instance().findAllMedicamentos();
            } else {
                if ("Nombre".equals(tipoBusqueda)) {
                    result = Service.instance().findMedicamentosByNombre(criterio);
                } else {
                    result = Service.instance().findMedicamentosByCodigo(criterio);
                }
            }
            int[] cols = {MedicamentosTableModel.ID, MedicamentosTableModel.NOMBRE, MedicamentosTableModel.PRESENTACION};
            medicamentosTable.setModel(new MedicamentosTableModel(cols, result));
        } catch (Exception ex) {
            System.err.println("Error buscando medicamentos: " + ex.getMessage());
            medicamentosTable.setModel(new MedicamentosTableModel(new int[]{}, Collections.emptyList()));
        }
    }

    private void onOK() {
        int row = medicamentosTable.getSelectedRow();
        if (row != -1) {
            Medicamento seleccionado = ((MedicamentosTableModel) medicamentosTable.getModel()).getRowAt(row);
            model.getCurrentReceta().setMedicamento(seleccionado);
            model.setCurrentReceta(model.getCurrentReceta());
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}