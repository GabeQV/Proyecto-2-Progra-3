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

    // Streams para el canal Asíncrono (notificaciones del servidor)
    private Socket as;
    private ObjectOutputStream aos;
    private ObjectInputStream ais;

    private String sid; // Session ID
    private ThreadListener listener; // Referencia a la GUI para notificarla

    private Service() {
        try {
            // 1. Conectar canal síncrono
            s = new Socket(Protocol.SERVER, Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
            os.writeInt(Protocol.SYNC);
            os.flush();
            sid = (String) is.readObject(); // Recibir Session ID del servidor

            // 2. Conectar canal asíncrono
            as = new Socket(Protocol.SERVER, Protocol.PORT);
            aos = new ObjectOutputStream(as.getOutputStream());
            ais = new ObjectInputStream(as.getInputStream());
            aos.writeInt(Protocol.ASYNC);
            aos.writeObject(sid); // Enviar Session ID para que el servidor nos identifique
            aos.flush();

            // 3. Iniciar hilo que escucha notificaciones del servidor
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
        t.setDaemon(true); // El hilo no impedirá que la aplicación se cierre
        t.start();
    }

    public void stop() {
        try {
            os.writeInt(Protocol.DISCONNECT);
            os.flush();
            s.shutdownOutput();
            s.close();
        } catch (Exception e) {
            // Ignorar errores al desconectar
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

    // ... Implementa aquí los métodos para PACIENTES, MEDICOS, FARMACEUTAS de forma similar ...
    // Por simplicidad, los he omitido, pero la estructura es idéntica.
    // Ejemplo:
    public List<Paciente> findAllPacientes() throws Exception {
        // Debes implementar la lógica similar a findAllMedicamentos
        return Collections.emptyList(); // Placeholder
    }

    public List<Medico> findAllMedicos() throws Exception {
        return Collections.emptyList(); // Placeholder
    }

    public List<Farmaceuta> findAllFarmaceutas() throws Exception {
        return Collections.emptyList(); // Placeholder
    }

    public Paciente readPaciente(Paciente p) throws Exception {
        return null; // Placeholder
    }

    public void createPaciente(Paciente p) throws Exception {
        // Placeholder
    }

    public void updatePaciente(Paciente p) throws Exception {
        // Placeholder
    }

    public void deletePaciente(Paciente p) throws Exception {
        // Placeholder
    }
}