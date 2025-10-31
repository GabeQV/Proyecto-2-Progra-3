package hospital.logic;

import hospital.logic.ThreadListener;
import hospital.presentation.despacho.DespachoView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

public class Sesion implements ThreadListener {
    private static Sesion theInstance;
    private Usuario usuarioActual;
    private CountDownLatch latch;

    private hospital.presentation.catalogo.Controller catalogoController;
    private hospital.presentation.despacho.Controller despachoController;
    private hospital.presentation.historico_recetas.Controller historicoController;
    private hospital.presentation.medicos.Controller medicosController;
    private hospital.presentation.farmaceutas.Controller farmaceutasController;
    private hospital.presentation.pacientes.Controller pacientesController;
    private hospital.presentation.usuariosConectados.Controller usuariosConectadosController;

    public static Sesion instance() {
        if (theInstance == null) theInstance = new Sesion();
        return theInstance;
    }

    private Sesion() {}

    public void setLatch(CountDownLatch latch) { this.latch = latch; }

    public void setUsuarioActual(Usuario user) { this.usuarioActual = user; }

    public Usuario getUsuarioActual() { return usuarioActual; }

    @Override
    public void deliver_message(String message) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("NOTIFICACIÓN RECIBIDA: " + message);
            try {
                switch (message) {
                    case "UPDATE_MEDICAMENTOS":
                        if (catalogoController != null) catalogoController.refreshList();
                        break;
                    case "UPDATE_RECETAS":
                        if (despachoController != null) despachoController.refreshList();
                        if (historicoController != null) historicoController.refreshList();
                        break;
                    case "UPDATE_PACIENTES":
                        if (pacientesController != null) pacientesController.refreshList();
                        break;
                    case "UPDATE_MEDICOS":
                        if (medicosController != null) medicosController.refreshList();
                        break;
                    case "UPDATE_FARMACEUTAS":
                        if (farmaceutasController != null) farmaceutasController.refreshList();
                        break;
                }
            } catch (Exception e) {
                System.err.println("Error al refrescar la vista tras notificación: " + e.getMessage());
            }
        });
    }

    @Override
    public void deliver_login(Usuario user) {
        SwingUtilities.invokeLater(() -> {
            if (usuariosConectadosController != null) {
                usuariosConectadosController.userLoggedIn(user);
            }
        });
    }

    @Override
    public void deliver_logout(Usuario user) {
        SwingUtilities.invokeLater(() -> {
            if (usuariosConectadosController != null) {
                usuariosConectadosController.userLoggedOut(user);
            }
        });
    }

    // Construye el panel lateral de Usuarios (SIEMPRE visible)
    private JComponent buildUsuariosSidePanel() {
        hospital.presentation.usuariosConectados.UsuariosConectadosView usuariosView =
                new hospital.presentation.usuariosConectados.UsuariosConectadosView();
        hospital.presentation.usuariosConectados.Model usuariosModel =
                new hospital.presentation.usuariosConectados.Model();
        this.usuariosConectadosController =
                new hospital.presentation.usuariosConectados.Controller(usuariosView, usuariosModel);

        JComponent panel = usuariosView.getUsuariosPanel();
        panel.setPreferredSize(new Dimension(280, 0)); // ancho fijo, alto se estira
        return panel;
    }

    private void addAdminTabs(JTabbedPane tabbedPane) {
        // Medicos
        hospital.presentation.medicos.MedicosView medicosView = new hospital.presentation.medicos.MedicosView();
        hospital.presentation.medicos.Model medicosModel = new hospital.presentation.medicos.Model();
        this.medicosController = new hospital.presentation.medicos.Controller(medicosView, medicosModel);
        tabbedPane.addTab("Medicos", medicosView.getMedicosPanel());

        // Farmaceutas
        hospital.presentation.farmaceutas.FarmaceutasView farmaceutasView = new hospital.presentation.farmaceutas.FarmaceutasView();
        hospital.presentation.farmaceutas.Model farmaceutasModel = new hospital.presentation.farmaceutas.Model();
        this.farmaceutasController = new hospital.presentation.farmaceutas.Controller(farmaceutasView, farmaceutasModel);
        tabbedPane.addTab("Farmaceutas", farmaceutasView.getFarmaceutasPanel());

        // Pacientes
        hospital.presentation.pacientes.PacientesView pacientesView = new hospital.presentation.pacientes.PacientesView();
        hospital.presentation.pacientes.Model pacientesModel = new hospital.presentation.pacientes.Model();
        this.pacientesController = new hospital.presentation.pacientes.Controller(pacientesView, pacientesModel);
        tabbedPane.addTab("Pacientes", pacientesView.getPacientesPanel());

        // Medicamentos (Catálogo)
        hospital.presentation.catalogo.CatologoView catologoView = new hospital.presentation.catalogo.CatologoView();
        hospital.presentation.catalogo.Model catologoModel = new hospital.presentation.catalogo.Model();
        this.catalogoController = new hospital.presentation.catalogo.Controller(catologoView, catologoModel);
        tabbedPane.addTab("Medicamentos", catologoView.getCatalogoPanel());

        // Dashboard
        hospital.presentation.dashboard.DashboardView dashboardView = new hospital.presentation.dashboard.DashboardView();
        hospital.presentation.dashboard.Model dashboardModel = new hospital.presentation.dashboard.Model();
        hospital.presentation.dashboard.Controller dashboardController =
                new hospital.presentation.dashboard.Controller(dashboardView, dashboardModel);
        dashboardView.setController(dashboardController);
        dashboardView.setModel(dashboardModel);
        tabbedPane.addTab("Dashboard", dashboardView.getDashboardPanel());

        // Histórico
        hospital.presentation.historico_recetas.HistoricoRecetasView historicoRecetasView =
                new hospital.presentation.historico_recetas.HistoricoRecetasView();
        hospital.presentation.historico_recetas.Model historicoRecetaModel =
                new hospital.presentation.historico_recetas.Model();
        this.historicoController =
                new hospital.presentation.historico_recetas.Controller(historicoRecetasView, historicoRecetaModel);
        tabbedPane.addTab("Historico", historicoRecetasView.getHitoricoRecetasPanel());
    }

    public void addMedicoTabs(JTabbedPane tabbedPane) {
        // Prescripción
        hospital.presentation.prescripcion.PrescripcionView prescripcionView =
                new hospital.presentation.prescripcion.PrescripcionView();
        hospital.presentation.prescripcion.Model prescripcionModel =
                new hospital.presentation.prescripcion.Model();
        hospital.presentation.prescripcion.Controller prescripcionController =
                new hospital.presentation.prescripcion.Controller(prescripcionView, prescripcionModel);
        tabbedPane.addTab("Prescripcion", prescripcionView.getPrescripcionPanel());

        // Dashboard
        hospital.presentation.dashboard.DashboardView dashboardViewMed = new hospital.presentation.dashboard.DashboardView();
        hospital.presentation.dashboard.Model dashboardModelMed = new hospital.presentation.dashboard.Model();
        hospital.presentation.dashboard.Controller dashboardControllerMed =
                new hospital.presentation.dashboard.Controller(dashboardViewMed, dashboardModelMed);
        dashboardViewMed.setController(dashboardControllerMed);
        dashboardViewMed.setModel(dashboardModelMed);
        tabbedPane.addTab("Dashboard", dashboardViewMed.getDashboardPanel());

        // Histórico
        hospital.presentation.historico_recetas.HistoricoRecetasView historicoRecetasViewMed =
                new hospital.presentation.historico_recetas.HistoricoRecetasView();
        hospital.presentation.historico_recetas.Model historicoRecetaModelMed =
                new hospital.presentation.historico_recetas.Model();
        hospital.presentation.historico_recetas.Controller historicoRecetasControllerMed =
                new hospital.presentation.historico_recetas.Controller(historicoRecetasViewMed, historicoRecetaModelMed);
        tabbedPane.addTab("Historico", historicoRecetasViewMed.getHitoricoRecetasPanel());
    }

    public void addFarmaceutaTabs(JTabbedPane tabbedPane) {
        // Despacho
        DespachoView despachoView = new DespachoView();
        hospital.presentation.despacho.Model despachoModel = new hospital.presentation.despacho.Model();
        this.despachoController = new hospital.presentation.despacho.Controller(despachoView, despachoModel);
        tabbedPane.addTab("Despacho", despachoView.getDespachoView());

        // Dashboard
        hospital.presentation.dashboard.DashboardView dashboardViewFar = new hospital.presentation.dashboard.DashboardView();
        hospital.presentation.dashboard.Model dashboardModelFar = new hospital.presentation.dashboard.Model();
        hospital.presentation.dashboard.Controller dashboardControllerFar =
                new hospital.presentation.dashboard.Controller(dashboardViewFar, dashboardModelFar);
        dashboardViewFar.setController(dashboardControllerFar);
        dashboardViewFar.setModel(dashboardModelFar);
        tabbedPane.addTab("Dashboard", dashboardViewFar.getDashboardPanel());

        // Histórico
        hospital.presentation.historico_recetas.HistoricoRecetasView historicoRecetasViewFar =
                new hospital.presentation.historico_recetas.HistoricoRecetasView();
        hospital.presentation.historico_recetas.Model historicoRecetaModelFar =
                new hospital.presentation.historico_recetas.Model();
        hospital.presentation.historico_recetas.Controller historicoRecetasControllerFar =
                new hospital.presentation.historico_recetas.Controller(historicoRecetasViewFar, historicoRecetaModelFar);
        tabbedPane.addTab("Historico", historicoRecetasViewFar.getHitoricoRecetasPanel());
    }

    public void abrirVentanaPrincipal() {
        Usuario user = getUsuarioActual();
        if (user == null) return;

        Service.instance().setListener(this);

        JFrame mainWindow = new JFrame();
        mainWindow.setSize(1200, 600); // un poco más ancho para el panel lateral
        mainWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainWindow.setTitle("Hospital" + " " + user.getNombre() + " " + user.getId());

        // Tabs principales (izquierda)
        JTabbedPane tabbedPane = new JTabbedPane();
        switch (user.getTipo()) {
            case "ADMINISTRADOR": addAdminTabs(tabbedPane); break;
            case "MEDICO":        addMedicoTabs(tabbedPane); break;
            case "FARMACEUTA":    addFarmaceutaTabs(tabbedPane); break;
        }

        // Usuarios SIEMPRE visible (derecha)
        JComponent usuariosPanel = buildUsuariosSidePanel();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, usuariosPanel);
        split.setResizeWeight(1.0);          // da prioridad de tamaño a la parte izquierda
        split.setDividerLocation(900);       // posición inicial del divisor
        split.setContinuousLayout(true);
        split.setOneTouchExpandable(false);

        mainWindow.setContentPane(split);
        mainWindow.setLocationRelativeTo(null);

        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try { Service.instance().logout(); } catch (Exception ex) { }
                Service.instance().stop();
                if (latch != null) latch.countDown();
                System.exit(0);
            }
        });

        mainWindow.setVisible(true);
    }

    public void cerrarSesion() {
        try { Service.instance().logout(); } catch (Exception e) { }
        this.usuarioActual = null;
    }
}