package hospital.presentation.usuariosConectados;

import hospital.logic.Usuario;
import hospital.presentation.abstracts.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Model extends AbstractModel {
    public static final String LIST = "list";

    private List<Usuario> list;

    public Model() {
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST, null, list);
    }

    public List<Usuario> getList() {
        return list;
    }

    public void setList(List<Usuario> list) {
        this.list = list;
        firePropertyChange(LIST, null, this.list);
    }

    public void addUser(Usuario user) {
        if (list.stream().noneMatch(u -> u.getId().equals(user.getId()))) {
            list.add(user);
            firePropertyChange(LIST, null, this.list);
        }
    }

    public void removeUser(Usuario user) {
        List<Usuario> original = new ArrayList<>(list);
        list = list.stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .collect(Collectors.toList());
        if (original.size() != list.size()) {
            firePropertyChange(LIST, null, this.list);
        }
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}