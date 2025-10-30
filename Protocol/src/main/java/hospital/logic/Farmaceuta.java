package hospital.logic;

import java.io.Serializable;

public class Farmaceuta extends Usuario implements Serializable {

    public Farmaceuta() {super();}
    public Farmaceuta(String id, String clave, String nombre) {
        super(id, clave, nombre);
    }

    @Override
    public String getTipo() {
        return "FARMACEUTA";
    }

}