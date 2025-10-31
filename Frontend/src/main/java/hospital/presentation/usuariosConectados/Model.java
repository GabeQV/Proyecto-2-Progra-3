package hospital.presentation.usuariosConectados;

import hospital.logic.Usuario;
import hospital.presentation.abstracts.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    public static final String LIST = "list";

    private List<Usuario> list;

    public Model() {
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
    }

    public List<Usuario> getList() {
        return list;
    }

    public void setList(List<Usuario> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}