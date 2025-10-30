package hospital.logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Receta implements Serializable {

    private String id;
    private String indicaciones;
    private String cantidad;
    private String duracion;
    private String estado;
    private Usuario usuario;
    private Paciente paciente;
    private Medicamento medicamento;
    LocalDate fecha;

    public Receta() {
    }

    public Receta(String id, String indicaciones, String cantidad, String duracion, String estado, Medicamento medicamento, LocalDate fecha) {
        this.id = id;
        this.indicaciones = indicaciones;
        this.cantidad = cantidad;
        this.duracion = duracion;
        this.medicamento = medicamento;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Receta(String id, String indicaciones, String cantidad, String duracion,String estado, Medicamento medicamento) {
        this(id, indicaciones, cantidad, duracion, estado, medicamento, null);
    }

    public String getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getIndicaciones() {
        return this.indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDuracion() {
        return this.duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Medicamento getMedicamento() {
        return this.medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {return this.estado;}
    public void setEstado(String estado) {this.estado = estado;}

    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receta receta = (Receta) o;
        return Objects.equals(id, receta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}