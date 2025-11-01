package hospital.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID; // <--- IMPORTANTE: Añadir esta importación
import java.util.stream.Collectors;

public class Server {
    ServerSocket ss;
    List<Worker> workers;

    public Server() {
        try {
            ss = new ServerSocket(Protocol.PORT);
            workers = Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void run() {

        Service service = Service.instance();

        boolean continuar = true;
        while (continuar) {
            try {
                Socket s = ss.accept();
                System.out.println("Conexion Establecida...");
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                int type = is.readInt();
                String sid;
                switch (type) {
                    case Protocol.SYNC:
                        // --- LA SOLUCIÓN DEFINITIVA ---
                        // Generar un Session ID único y robusto.
                        sid = UUID.randomUUID().toString();
                        System.out.println("SYNC: " + sid);

                        Worker w = find(sid);
                        if (w == null) {
                            w = new Worker(this, sid, service);
                            workers.add(w);
                        }
                        w.setSync(s, os, is);

                        if (w.isReady()) w.start();

                        os.writeObject(sid);
                        os.flush();
                        break;

                    case Protocol.ASYNC:
                        sid = (String) is.readObject();
                        System.out.println("ASYNC: " + sid);

                        Worker w_async = find(sid);
                        if (w_async == null) {
                            w_async = new Worker(this, sid, service);
                            workers.add(w_async);
                        }
                        w_async.setAsync(s, os, is);

                        if (w_async.isReady()) w_async.start();
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }

    private Worker find(String sid) {
        for (Worker w : workers) {
            if (w.sid.equals(sid)) {
                return w;
            }
        }
        return null;
    }

    public void remove(Worker w) {
        workers.remove(w);
        System.out.println("Quedan: " + workers.size());
    }

    public void deliver_message(Worker from, String message) {
        for (Worker w : workers) {
            w.deliver_message(message);
        }
    }

    public void deliver_login(Usuario user) {
        for (Worker w : workers) {
            w.deliver_login(user);
        }
    }

    public void deliver_logout(Usuario user) {
        for (Worker w : workers) {
            w.deliver_logout(user);
        }
    }

    public List<Usuario> getConnectedUsers() {
        return workers.stream()
                .filter(w -> w.getUser() != null)
                .map(Worker::getUser)
                .collect(Collectors.toList());
    }

    public boolean deliver_dm(Usuario from, String toUserId, String text) {
        if (toUserId == null) return false;
        for (Worker w : workers) {
            Usuario u = w.getUser();
            if (u != null && toUserId.equals(u.getId())) {
                String payload = "DM|" + (from != null ? from.getId() : "") + "|" + (text == null ? "" : text);
                w.deliver_message(payload);
                return true;
            }
        }
        return false;
    }
};