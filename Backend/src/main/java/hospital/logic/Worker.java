package hospital.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Worker {
    Server srv;
    // SYNC
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;
    // ASYNC
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;

    Service service;
    String sid;
    private Usuario user;

    public Worker(Server srv, String sid, Service service) {
        this.srv = srv;
        this.sid = sid;
        this.service = service;
        this.user = null;
    }

    public void setSync(Socket s, ObjectOutputStream os, ObjectInputStream is) {
        this.s = s;
        this.os = os;
        this.is = is;
    }

    public void setAsync(Socket as, ObjectOutputStream aos, ObjectInputStream ais) {
        this.as = as;
        this.aos = aos;
        this.ais = ais;
    }

    public boolean isReady() {
        return s != null && as != null;
    }

    public Usuario getUser() {
        return user;
    }

    boolean continuar;

    public void start() {
        System.out.println("Worker " + sid + " atendiendo peticiones...");
        Thread t = new Thread(this::listen);
        continuar = true;
        t.start();
    }

    public void stop() {
        continuar = false;
        try {
            if (s != null && !s.isClosed()) s.close();
            if (as != null && !as.isClosed()) as.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar sockets para worker " + sid + ": " + e.getMessage());
        }
        System.out.println("Worker " + sid + " detenido.");
    }

    public void listen() {
        int method;
        while (continuar) {
            try {
                method = is.readInt();
                System.out.println("Operacion: " + method + " para " + sid);
                switch (method) {
                    // ... (TODOS LOS CASE VAN AQUÍ, SIN CAMBIOS)
                    // ------------ LOGIN ------------
                    case Protocol.LOGIN:
                        try {
                            String id = (String) is.readObject();
                            String clave = (String) is.readObject();
                            this.user = service.login(id, clave);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(user);
                            srv.deliver_login(this.user);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.LOGOUT:
                        try {
                            if (this.user != null) {
                                srv.deliver_logout(this.user);
                                this.user = null;
                            }
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.writeObject(ex.getMessage());
                        }
                        break;
                    case Protocol.GET_CONNECTED_USERS:
                        try {
                            List<Usuario> users = srv.getConnectedUsers();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(users);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
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
                        // La desconexión es manejada por la excepción de IO
                        break;
                }
                os.flush();
            } catch (IOException e) {
                // Si hay una excepción de IO (p. ej., el cliente se desconecta),
                // el worker debe detenerse y limpiarse.
                if (user != null) {
                    srv.deliver_logout(this.user);
                }
                stop();
                srv.remove(this);
                System.err.println("Worker " + sid + ": Conexión perdida. Worker detenido.");
            } catch (Exception e) {
                System.err.println("Worker " + sid + " error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public synchronized void deliver_message(String message) {
        if (aos != null) {
            try {
                aos.writeInt(Protocol.DELIVER_MESSAGE);
                aos.writeObject(message);
                aos.flush();
            } catch (Exception e) {
                // La conexión podría estar cerrada, ignorar.
            }
        }
    }

    public synchronized void deliver_login(Usuario user) {
        if (aos != null) {
            try {
                aos.writeInt(Protocol.DELIVER_LOGIN);
                aos.writeObject(user);
                aos.flush();
            } catch (IOException e) {
                // La conexión podría estar cerrada, ignorar.
            }
        }
    }

    public synchronized void deliver_logout(Usuario user) {
        if (aos != null) {
            try {
                aos.writeInt(Protocol.DELIVER_LOGOUT);
                aos.writeObject(user);
                aos.flush();
            } catch (IOException e) {
                // La conexión podría estar cerrada, ignorar.
            }
        }
    }
}