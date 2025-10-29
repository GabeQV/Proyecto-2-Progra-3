package hospital;

import hospital.logic.Server;

public class Backend_Main {

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}