package hospital;

import hospital.logic.Service;
import hospital.logic.Sesion;
import hospital.presentation.login.LoginView;
import javax.swing.*;
import java.awt.Color;
import java.util.concurrent.CountDownLatch;

public class Frontend_Main {
    public static final Color BACKGROUND_ERROR = new Color(255, 200, 200);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception ex) {};

        final CountDownLatch latch = new CountDownLatch(1);
        Sesion.instance().setLatch(latch);

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

        try {
            latch.await();
            System.out.println("Aplicación terminando...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}