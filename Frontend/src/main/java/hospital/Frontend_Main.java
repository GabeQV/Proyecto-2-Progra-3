package hospital;

import hospital.logic.Service;
import hospital.presentation.login.LoginView;
import javax.swing.*;
import java.awt.Color;

public class Frontend_Main {
    public static final Color BACKGROUND_ERROR = new Color(255, 200, 200);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception ex) {};

        Service.instance();

        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Login - Sistema Hospital");
            LoginView loginView = new LoginView();
            window.setContentPane(loginView.getLoginPanel());
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}