package hospital.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Worker {
    Server srv;
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;
    Service service;

    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;

    private Usuario user;

    public Worker(Server srv, Socket s, ObjectOutputStream os, ObjectInputStream is, String sid, Service service) {
        this.srv = srv;
        this.s = s;
        this.os = os;
        this.is = is;
        this.service = service;
        this.sid = sid;
        this.user = null;
    }

    public Usuario getUser() {
        return user;
    }

    public void setAs(Socket as, ObjectOutputStream aos, ObjectInputStream ais) {
        this.as = as;
        this.aos = aos;
        this.ais = ais;
    }

    boolean continuar;

    public void start() {
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable() {
                public void run() {
                    listen();
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {
        }
    }

    public void stop() {
        continuar = false;
        System.out.println("Conexion cerrada...");
    }

    public void listen() {
        int method;
        while (continuar) {
            try {
                method = is.readInt();
                System.out.println("Operacion: " + method + " para " + sid);
                switch (method) {
                    // ------------ LOGIN ------------
                    case Protocol.LOGIN:
                        try {
                            String id = (String) is.readObject();
                            String clave = (String) is.readObject();
                            this.user = service.login(id, clave);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(user);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.CHANGE_PASSWORD:
                        try {
                            String id = (String) is.readObject();
                            String claveActual = (String) is.readObject();
                            String nuevaClave = (String) is.readObject();
                            String confirmarClave = (String) is.readObject();
                            service.cambiarClave(id, claveActual, nuevaClave, confirmarClave);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;

                    // ------------ MEDICAMENTOS ------------
                    case Protocol.MEDICAMENTO_READ:
                        try {
                            Medicamento e = service.readMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_CREATE:
                        try {
                            service.createMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_MEDICAMENTOS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.MEDICAMENTO_UPDATE:
                        try {
                            service.updateMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_MEDICAMENTOS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.MEDICAMENTO_DELETE:
                        try {
                            service.deleteMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_MEDICAMENTOS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.MEDICAMENTO_FIND_ALL:
                        try {
                            List<Medicamento> list = service.findAllMedicamentos();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_FIND_BY_NOMBRE:
                        try {
                            String nombre = (String) is.readObject();
                            List<Medicamento> list = service.findMedicamentosByNombre(nombre);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_FIND_BY_CODIGO:
                        try {
                            String codigo = (String) is.readObject();
                            List<Medicamento> list = service.findMedicamentosByCodigo(codigo);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ------------ RECETAS ------------
                    case Protocol.RECETA_READ:
                        try {
                            Receta e = service.readReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_CREATE:
                        try {
                            service.createReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_RECETAS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.RECETA_UPDATE:
                        try {
                            service.updateReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_RECETAS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.RECETA_FIND_ALL:
                        try {
                            List<Receta> list = service.findAllRecetas();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_FIND_BY_PACIENTE:
                        try {
                            String pacienteId = (String) is.readObject();
                            List<Receta> list = service.findRecetasByPacienteId(pacienteId);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    // ------------ MEDICOS ------------
                    case Protocol.MEDICO_READ:
                        try {
                            Medico e = service.readMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_CREATE:
                        try {
                            service.createMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_MEDICOS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.MEDICO_UPDATE:
                        try {
                            service.updateMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_MEDICOS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.MEDICO_DELETE:
                        try {
                            service.deleteMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_MEDICOS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.MEDICO_FIND_ALL:
                        try {
                            List<Medico> list = service.findAllMedicos();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    //------------ PACIENTES ------------
                    case Protocol.PACIENTE_READ:
                        try {
                            Paciente e = service.readPaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.PACIENTE_CREATE:
                        try {
                            service.createPaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_PACIENTES");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.PACIENTE_UPDATE:
                        try {
                            service.updatePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_PACIENTES");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.PACIENTE_DELETE:
                        try {
                            service.deletePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_PACIENTES");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.PACIENTE_FIND_ALL:
                        try {
                            List<Paciente> list = service.findAllPacientes();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    //------------ FARMACEUTAS ------------
                    case Protocol.FARMACEUTA_READ:
                        try {
                            Farmaceuta e = service.readFarmaceuta((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.FARMACEUTA_CREATE:
                        try {
                            service.createFarmaceuta((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_FARMACEUTAS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.FARMACEUTA_UPDATE:
                        try {
                            service.updateFarmaceuta((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_FARMACEUTAS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.FARMACEUTA_DELETE:
                        try {
                            service.deleteFarmaceuta((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_message(this, "UPDATE_FARMACEUTAS");
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.FARMACEUTA_FIND_ALL:
                        try {
                            List<Farmaceuta> list = service.findAllFarmaceutas();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(list);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.DISCONNECT:
                        stop();
                        srv.remove(this);
                        break;
                }
                os.flush();
            } catch (IOException e) {
                System.err.println("Error de IO en worker, cerrando conexión: " + e.getMessage());
                stop();
                srv.remove(this);
            }
        }
    }

    public synchronized void deliver_message(String message) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_MESSAGE);
                aos.writeObject(message);
                aos.flush();
            } catch (Exception e) {
            }
        }
    }

}