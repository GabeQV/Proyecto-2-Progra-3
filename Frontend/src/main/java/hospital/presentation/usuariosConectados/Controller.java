package hospital.presentation.usuariosConectados;

import hospital.logic.Service;
import hospital.logic.Usuario;
import hospital.presentation.usuariosConectados.UsuariosConectadosView;

import javax.swing.*;
import java.util.List;

public class Controller {
    private final UsuariosConectadosView view;
    private final Model model;

    public Controller(UsuariosConectadosView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        fetchConnectedUsers();
    }

    private void fetchConnectedUsers() {
        try {
            List<Usuario> users = Service.instance().getConnectedUsers();
            model.setList(users);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getUsuariosPanel(), "Error al obtener la lista de usuarios conectados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void userLoggedIn(Usuario user) {
        model.addUser(user);
    }

    public void userLoggedOut(Usuario user) {
        model.removeUser(user);
    }
}