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

    public static final String PROPERTIES_FILE_NAME="database.properties";
    Connection cnx;

    public Database(){
        cnx=this.getConnection();
    }

    public Connection getConnection(){
        try {
            Properties prop = new Properties();
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);

            if (stream == null) {
                throw new RuntimeException("No se pudo encontrar el archivo de propiedades en el classpath: " + PROPERTIES_FILE_NAME);
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
            return DriverManager.getConnection(URL_conexion);
        } catch (Exception e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    public PreparedStatement prepareStatement(String statement) throws SQLException {
        return cnx.prepareStatement(statement);
    }

    public int executeUpdate(PreparedStatement statement) {
        try {
            statement.executeUpdate();
            return statement.getUpdateCount();
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

    public void close() throws Exception{
        if (cnx!=null && !cnx.isClosed()){
            cnx.close();
        }
    }
}