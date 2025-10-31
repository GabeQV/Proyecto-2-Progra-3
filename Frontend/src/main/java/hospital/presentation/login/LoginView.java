package hospital.presentation.login;

import hospital.logic.Service;
import hospital.logic.Sesion;
import hospital.logic.Usuario;
import hospital.presentation.login.cambio_clave.CambioClaveView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView  {
    private JPanel LoginPanel;
    private JTextField idField;
    private JTextField claveField;
    private JLabel idLabel;
    private JLabel claveLabel;
    private JButton loginButton;
    private JButton cancelarButton;
    private JButton cambiarClaveButton;


    public LoginView() {
        cambiarClaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = idField.getText();
                if (userId.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel, "Debe ingresar su ID primero", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                CambioClaveView dialog = new CambioClaveView((JFrame) SwingUtilities.getWindowAncestor(LoginPanel), userId);
                dialog.setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String id = idField.getText();
                final String clave = claveField.getText();


                loginButton.setEnabled(false);
                loginButton.setText("Conectando...");


                SwingWorker<Usuario, Void> worker = new SwingWorker<Usuario, Void>() {
                    @Override
                    protected Usuario doInBackground() throws Exception {

                        return Service.instance().login(id, clave);
                    }

                    @Override
                    protected void done() {
                        try {

                            Usuario user = get();
                            Sesion.instance().setUsuarioActual(user);
                            JOptionPane.showMessageDialog(LoginPanel, "Bienvenido " + user.getNombre(), "Login exitoso", JOptionPane.INFORMATION_MESSAGE);

                            JFrame window = (JFrame) SwingUtilities.getWindowAncestor(LoginPanel);
                            window.dispose();

                            Sesion.instance().abrirVentanaPrincipal();

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(LoginPanel, ex.getCause().getMessage(), "Error de login", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            loginButton.setEnabled(true);
                            loginButton.setText("Login");
                        }
                    }
                };

                worker.execute();
            }
        });
    }

    public JPanel getLoginPanel() {return LoginPanel;}


}