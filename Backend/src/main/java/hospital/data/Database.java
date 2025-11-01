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

    // --- CAMBIOS CLAVE AQUI ---
    // Ya no mantenemos una conexión única.

    public Database(){
    }

    public Connection getConnection(){
        try {
            Properties prop = new Properties();
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

}