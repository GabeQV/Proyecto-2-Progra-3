package hospital.data;

import hospital.logic.*;
import jakarta.xml.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
    @XmlElementWrapper(name = "medicos")
    @XmlElement(name = "medico")
    private List<Medico> medicos;

    @XmlElementWrapper(name = "pacientes")
    @XmlElement(name = "paciente")
    private List<Paciente> pacientes;

    @XmlElementWrapper(name = "farmaceutas")
    @XmlElement(name = "farmaceuta")
    private List<Farmaceuta> farmaceutas;

    @XmlElementWrapper(name = "medicamentos")
    @XmlElement(name = "medicamento")
    private List<Medicamento> medicamentos;

    @XmlElementWrapper(name = "recetas")
    @XmlElement(name = "receta")
    private List<Receta> recetas;

    public Data() {
        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        farmaceutas = new ArrayList<>();
        medicamentos = new ArrayList<>();
        recetas = new ArrayList<>();

    }

    public List<Medico> getMedicos() {return medicos;}
    public List<Paciente> getPacientes() {return pacientes;}
    public List<Farmaceuta> getFarmaceutas() { return farmaceutas; }
    public List<Medicamento> getMedicamentos() { return medicamentos; }
    public List<Receta> getRecetas() { return recetas; }

}
