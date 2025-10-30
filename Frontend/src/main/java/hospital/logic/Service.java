package hospital.logic;

import hospital.presentation.ThreadListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class Service {
    private static Service theInstance;
    public static Service instance() {
        if (theInstance == null) {
            theInstance = new Service();
        }
        return theInstance;
    }

    // Streams para el canal Síncrono (petición-respuesta)
    private Socket s;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    private Socket as;
    private ObjectOutputStream aos;
    private ObjectInputStream ais;

    private String sid; // Session ID
    private ThreadListener listener;

    private Service() {
        try {

            s = new Socket(Protocol.SERVER, Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
            os.writeInt(Protocol.SYNC);
            os.flush();
            sid = (String) is.readObject();


            as = new Socket(Protocol.SERVER, Protocol.PORT);
            aos = new ObjectOutputStream(as.getOutputStream());
            ais = new ObjectInputStream(as.getInputStream());
            aos.writeInt(Protocol.ASYNC);
            aos.writeObject(sid);
            aos.flush();

            startListening();

        } catch (Exception e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            // En una aplicación real, mostrarías un JOptionPane aquí.
            System.exit(-1);
        }
    }

    public void setListener(ThreadListener listener) {
        this.listener = listener;
    }

    private void startListening() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    int method = ais.readInt();
                    if (method == Protocol.DELIVER_MESSAGE) {
                        String message = (String) ais.readObject();
                        if (listener != null) {
                            // ¡Magia! Notificamos a la GUI que algo pasó.
                            listener.deliver_message(message);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Se perdió la conexión asíncrona con el servidor.");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void stop() {
        try {
            os.writeInt(Protocol.DISCONNECT);
            os.flush();
            s.shutdownOutput();
            s.close();
        } catch (Exception e) {
        }
    }

    // ----- MÉTODOS DE LÓGICA DE NEGOCIO -----

    // =============== AUTENTICACIÓN ===============
    public Usuario login(String id, String clave) throws Exception {
        os.writeInt(Protocol.LOGIN);
        os.writeObject(id);
        os.writeObject(clave);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (Usuario) is.readObject();
        } else {
            throw new Exception((String) is.readObject());
        }
    }

    public void cambiarClave(String id, String claveActual, String nuevaClave, String confirmarClave) throws Exception {
        os.writeInt(Protocol.CHANGE_PASSWORD);
        os.writeObject(id);
        os.writeObject(claveActual);
        os.writeObject(nuevaClave);
        os.writeObject(confirmarClave);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    // =============== MEDICAMENTOS ===============
    public void createMedicamento(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public void updateMedicamento(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public void deleteMedicamento(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public List<Medicamento> findAllMedicamentos() throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_FIND_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Medicamento>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Medicamento> findMedicamentosByNombre(String nombre) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_FIND_BY_NOMBRE);
        os.writeObject(nombre);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Medicamento>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Medicamento> findMedicamentosByCodigo(String codigo) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_FIND_BY_CODIGO);
        os.writeObject(codigo);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Medicamento>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    // =============== RECETAS ===============
    public void createReceta(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public void updateReceta(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public List<Receta> findAllRecetas() throws Exception {
        os.writeInt(Protocol.RECETA_FIND_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Receta>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Receta> findRecetasByPacienteId(String pacienteId) throws Exception {
        os.writeInt(Protocol.RECETA_FIND_BY_PACIENTE);
        os.writeObject(pacienteId);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Receta>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    public Receta readReceta(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (Receta) is.readObject();
        } else {
            throw new Exception("Receta no encontrada");
        }
    }

    // =============== MEDICOS ===============
    public void createMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public void updateMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public List<Medico> findAllMedicos() throws Exception {
        os.writeInt(Protocol.MEDICO_FIND_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Medico>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    public Medico readMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (Medico) is.readObject();
        } else {
            throw new Exception("Medico no encontrado");
        }
    }

    public void deleteMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    // =============== FARMACEUTAS ===============
    public void createFarmaceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public void updateFarmaceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public List<Farmaceuta> findAllFarmaceutas() throws Exception {
        os.writeInt(Protocol.FARMACEUTA_FIND_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Farmaceuta>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    public Farmaceuta readFarmaceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (Farmaceuta) is.readObject();
        } else {
            throw new Exception("Farmaceuta no encontrado");
        }
    }

    public void deleteFarmaceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    // =============== PACIENTES ===============
    public void createPaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public void updatePaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

    public List<Paciente> findAllPacientes() throws Exception {
        os.writeInt(Protocol.PACIENTE_FIND_ALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Paciente>) is.readObject();
        } else {
            return Collections.emptyList();
        }
    }

    public Paciente readPaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (Paciente) is.readObject();
        } else {
            throw new Exception("Paciente no encontrado");
        }
    }

    public void deletePaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception((String) is.readObject());
        }
    }

}