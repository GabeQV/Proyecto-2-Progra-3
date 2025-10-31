package hospital.presentation.usuariosConectados;

import hospital.presentation.usuariosConectados.Controller;
import hospital.presentation.usuariosConectados.Model;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UsuariosConectadosView implements PropertyChangeListener {
    private JPanel UsuariosPanel;
    private JPanel Usuarios;
    private JButton enviarButton;
    private JButton recibirButton;
    private JTable usuariosTable;

    Controller controller;
    Model model;




    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Model.LIST)) {
            // Cuando la lista en el modelo cambia, actualizamos la tabla
            int[] cols = {TableModel.ID};
            usuariosTable.setModel(new TableModel(cols, model.getList()));
            this.revalidate();
        }
    }

    public JPanel getUsuariosPanel() {return UsuariosPanel;}

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
}
