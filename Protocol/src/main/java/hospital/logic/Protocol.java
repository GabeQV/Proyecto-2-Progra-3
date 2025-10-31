package hospital.logic;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 1234;


    // Médicos
    public static final int MEDICO_CREATE = 101;
    public static final int MEDICO_READ = 102;
    public static final int MEDICO_UPDATE = 103;
    public static final int MEDICO_DELETE = 104;
    public static final int MEDICO_FIND_ALL = 105;

    // Farmaceutas
    public static final int FARMACEUTA_CREATE = 201;
    public static final int FARMACEUTA_READ = 202;
    public static final int FARMACEUTA_UPDATE = 203;
    public static final int FARMACEUTA_DELETE = 204;
    public static final int FARMACEUTA_FIND_ALL = 205;

    // Pacientes
    public static final int PACIENTE_CREATE = 301;
    public static final int PACIENTE_READ = 302;
    public static final int PACIENTE_UPDATE = 303;
    public static final int PACIENTE_DELETE = 304;
    public static final int PACIENTE_FIND_ALL = 305;

    // Medicamentos
    public static final int MEDICAMENTO_CREATE = 401;
    public static final int MEDICAMENTO_READ = 402;
    public static final int MEDICAMENTO_UPDATE = 403;
    public static final int MEDICAMENTO_DELETE = 404;
    public static final int MEDICAMENTO_FIND_ALL = 405;
    public static final int MEDICAMENTO_FIND_BY_NOMBRE = 406;
    public static final int MEDICAMENTO_FIND_BY_CODIGO = 407;

    // Recetas
    public static final int RECETA_CREATE = 501;
    public static final int RECETA_READ = 502;
    public static final int RECETA_UPDATE = 503;
    public static final int RECETA_FIND_BY_PACIENTE = 504;
    public static final int RECETA_FIND_ALL = 505;

    // Autenticación
    public static final int LOGIN = 601;
    public static final int LOGOUT = 602;
    public static final int CHANGE_PASSWORD = 603;


    // ----- CÓDIGOS DE ESTADO Y CONEXIÓN -----
    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_ERROR = 1;

    public static final int SYNC = 10;
    public static final int ASYNC = 11;
    public static final int DISCONNECT = 12;
    public static final int DELIVER_MESSAGE = 13;
    public static final int DELIVER_LOGIN = 14;
    public static final int DELIVER_LOGOUT = 15;

}
