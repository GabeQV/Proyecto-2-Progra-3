package hospital.presentation.usuariosConectados;

import hospital.logic.Usuario;
import hospital.presentation.abstracts.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Usuario> implements javax.swing.table.TableModel{

    public static final int ID = 0;
    public static final int MENSAJES = 1;
    private final Model model;

    public TableModel(int[] cols, List<Usuario> rows, Model model) {
        super(cols, rows);
        this.model = model;
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[MENSAJES] = "Mensajes?";
    }
    public Class<?> getColumnClass(int columnIndex) {
        switch (cols[columnIndex]) {
            case ID: return String.class;
            case MENSAJES: return Boolean.class; // checkbox
            default: return Object.class;
        }
    }

    @Override
    protected Object getPropetyAt(Usuario e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId();
            case MENSAJES:
                return model.tieneMensajesDe(e.getId()); // true/false
            default:
                return "";
        }
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}

