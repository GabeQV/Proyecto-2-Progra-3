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

    /** Envía un mensaje al usuario destino a través del Service. */
    public void enviarMensajeA(String destinatarioId, String texto) throws Exception {
        if (destinatarioId == null || destinatarioId.isEmpty()) {
            throw new IllegalArgumentException("Debe indicar un destinatario.");
        }
        hospital.logic.Service.instance().enviarMensajeDirecto(destinatarioId, texto == null ? "" : texto);
    }

    /** Llamado cuando llega un mensaje del backend. Lo almacena y refresca la vista. */
    public void recibirMensajeDe(String remitenteId, String texto) {
        model.encolarMensaje(remitenteId, texto);
        model.refrescarLista(); // hará que la vista vuelva a construir el TableModel
    }

    /** Devuelve el próximo mensaje del remitente y refresca la vista para actualizar el indicador. */
    public String obtenerSiguienteMensajeDe(String remitenteId) {
        String msg = model.extraerMensaje(remitenteId);
        model.refrescarLista();
        return msg;
    }
}