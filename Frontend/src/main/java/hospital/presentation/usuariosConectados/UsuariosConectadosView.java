package hospital.presentation.usuariosConectados;

import hospital.presentation.usuariosConectados.Controller;
import hospital.presentation.usuariosConectados.Model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


    public String obtenerUsuarioSeleccionadoId() {
        int row = usuariosTable.getSelectedRow();
        if (row < 0) return null;
        return usuariosTable.getValueAt(row, 0).toString(); // col 0 = Id
    }

    public UsuariosConectadosView() {
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toId = obtenerUsuarioSeleccionadoId();
                if (toId == null) {
                    JOptionPane.showMessageDialog(Usuarios, "Seleccione un usuario.", "Enviar mensaje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String text = JOptionPane.showInputDialog(Usuarios, "Mensaje para " + toId + ":", "Enviar mensaje", JOptionPane.PLAIN_MESSAGE);
                if (text != null && !text.trim().isEmpty()) {
                    try {
                        controller.enviarMensajeA(toId, text.trim());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Usuarios, "No se pudo enviar el mensaje: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        recibirButton.addActionListener(e -> {
            String fromId = obtenerUsuarioSeleccionadoId();
            if (fromId == null) {
                JOptionPane.showMessageDialog(Usuarios, "Seleccione un remitente.", "Recibir mensaje", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String msg = controller.obtenerSiguienteMensajeDe(fromId);
            if (msg == null) {
                JOptionPane.showMessageDialog(Usuarios, "No hay mensajes de " + fromId + ".", "Recibir mensaje", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(Usuarios, msg, "Mensaje de " + fromId, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Model.LIST)) {
            int[] cols = {TableModel.ID, TableModel.MENSAJES};
            usuariosTable.setModel(new TableModel(cols, model.getList(),model));
            usuariosTable.revalidate();
            usuariosTable.repaint();
        }
    }

    public JPanel getUsuariosPanel() {
        return UsuariosPanel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
}
