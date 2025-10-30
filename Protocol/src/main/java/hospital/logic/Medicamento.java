package hospital.logic;


public class Medicamento {

    private String id;

    private String nombre;

    private String presentacion;

    public Medicamento() {
    }

    public Medicamento(String id, String nombre, String presentacion) {
        this.id = id;
        this.nombre = nombre;
        this.presentacion = presentacion;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return this.presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
