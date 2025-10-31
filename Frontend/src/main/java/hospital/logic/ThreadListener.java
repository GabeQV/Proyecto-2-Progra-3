package hospital.logic;

public interface ThreadListener {
    void deliver_message(String message);
    void deliver_login(Usuario user);
    void deliver_logout(Usuario user);
}
