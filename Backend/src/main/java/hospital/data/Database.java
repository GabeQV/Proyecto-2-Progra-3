package hospital.data;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static Database theInstance;
    public static Database instance(){
        if (theInstance==null){
            theInstance=new Database();
        }
        return theInstance;
    }

    // --- CAMBIOS CLAVE AQUÍ ---
    // Ya no mantenemos una conexión única.
    // private Connection cnx; // ELIMINAR ESTA LÍNEA

    public Database(){
        // El constructor ahora está vacío.
    }

    public Connection getConnection(){
        try {
            Properties prop = new Properties();
            // La ruta del archivo de propiedades es relativa al classpath
            InputStream stream = getClass().getClassLoader().getResourceAsStream("database.properties");

            if (stream == null) {
                throw new RuntimeException("No se pudo encontrar el archivo de propiedades en el classpath: database.properties");
            }

            prop.load(stream);
            String driver = prop.getProperty("database_driver");
            String server = prop.getProperty("database_server");
            String port = prop.getProperty("database_port");
            String user = prop.getProperty("database_user");
            String password = prop.getProperty("database_password");
            String database = prop.getProperty("database_name");

            String URL_conexion="jdbc:mysql://"+ server+":"+port+"/"+
                    database+"?user="+user+"&password="+password+"&serverTimezone=UTC";

            Class.forName(driver);
            // Cada llamada devuelve una NUEVA conexión.
            return DriverManager.getConnection(URL_conexion);
        } catch (Exception e) {
            System.err.println("Error al obtener una conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
            // No salir de la aplicación, simplemente lanzar una excepción.
            throw new RuntimeException("No se pudo conectar a la base de datos.", e);
        }
    }

    // Los siguientes métodos ya no son necesarios porque los DAOs manejarán sus propias conexiones.
    // Puedes eliminarlos o dejarlos si los usas en otro lugar, pero la práctica correcta
    // es que cada DAO gestione su ciclo de vida de PreparedStatement.
    /*
    public PreparedStatement prepareStatement(String statement) throws SQLException {
        // Este método se vuelve problemático sin una conexión única.
        // Se recomienda manejar los PreparedStatements en los DAOs.
        throw new UnsupportedOperationException("Obtenga una conexión y prepare el statement en el DAO.");
    }

    public int executeUpdate(PreparedStatement statement) {
        try {
            return statement.getUpdateCount();
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet executeQuery(PreparedStatement statement){
        try {
            return statement.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    */
}