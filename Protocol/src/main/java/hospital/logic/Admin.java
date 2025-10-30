package hospital.logic;

import java.io.Serializable;

public class Admin extends Usuario implements Serializable {
    public Admin(){
        super("ADM","ADM","Administrador");
    }

    @Override
    public String getTipo() {
        return "ADMINISTRADOR";
    }
}