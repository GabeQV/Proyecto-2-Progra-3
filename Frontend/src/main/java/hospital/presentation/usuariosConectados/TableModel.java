package hospital.presentation.usuariosConectados;

import hospital.logic.Usuario;
import hospital.presentation.abstracts.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Usuario> implements javax.swing.table.TableModel{

    public static final int ID = 0;
    public static final int MENSAJES = 1;

    public TableModel(int[] cols, List<Usuario> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[MENSAJES] = "Mensajes?";
    }

    @Override
    protected Object getPropetyAt(Usuario e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId();
                case MENSAJES:
                    return "";
            default:
                return "";
        }
    }

}

