package hospital.presentation.login;

import hospital.logic.Service;
import hospital.logic.Sesion;
import hospital.logic.Usuario;

public class Controller {
    LoginView view;
    Model model;

    public Controller(LoginView view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void login(String id, String clave) throws Exception {
            Usuario user = Service.instance().login(id, clave);
            Sesion.instance().setUsuarioActual(user);
    }

}
