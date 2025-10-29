package hospital.logic;

import hospital.presentation.despacho.DespachoView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Sesion {
    private static Sesion theInstance;
    private Usuario usuarioActual;

    public static Sesion instance() {
        if (theInstance == null) theInstance = new Sesion();
        return theInstance;
    }

    private Sesion() {}

    public void setUsuarioActual(Usuario user) {
        this.usuarioActual = user;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    private void addAdminTabs(JTabbedPane tabbedPane) {
        //MVC de Medicos
        hospital.presentation.medicos.MedicosView medicosView = new hospital.presentation.medicos.MedicosView();
        hospital.presentation.medicos.Model medicosModel = new hospital.presentation.medicos.Model();
        hospital.presentation.medicos.Controller medicosController = new hospital.presentation.medicos.Controller(medicosView, medicosModel);
        tabbedPane.addTab("Medicos", medicosView.getMedicosPanel());

        //MVC de Farmaceutas
        hospital.presentation.farmaceutas.FarmaceutasView farmaceutasView = new hospital.presentation.farmaceutas.FarmaceutasView();
        hospital.presentation.farmaceutas.Model farmaceutasModel = new hospital.presentation.farmaceutas.Model();
        hospital.presentation.farmaceutas.Controller farmaceutasController = new hospital.presentation.farmaceutas.Controller(farmaceutasView, farmaceutasModel);
        tabbedPane.addTab("Farmaceutas", farmaceutasView.getFarmaceutasPanel());

        //MVC de Pacientes
        hospital.presentation.pacientes.PacientesView pacientesView = new hospital.presentation.pacientes.PacientesView();
        hospital.presentation.pacientes.Model pacientesModel = new hospital.presentation.pacientes.Model();
        hospital.presentation.pacientes.Controller pacientesController = new hospital.presentation.pacientes.Controller(pacientesView, pacientesModel);
        tabbedPane.addTab("Pacientes", pacientesView.getPacientesPanel());

        //MVC de catalogo
        hospital.presentation.catalogo.CatologoView catologoView = new hospital.presentation.catalogo.CatologoView();
        hospital.presentation.catalogo.Model catologoModel = new hospital.presentation.catalogo.Model();
        hospital.presentation.catalogo.Controller catalogoController = new hospital.presentation.catalogo.Controller(catologoView, catologoModel);
        tabbedPane.addTab("Medicamentos", catologoView.getCatalogoPanel());

        //MVC de dashboard
        hospital.presentation.dashboard.DashboardView dashboardView = new hospital.presentation.dashboard.DashboardView();
        hospital.presentation.dashboard.Model dashboardModel = new hospital.presentation.dashboard.Model();
        hospital.presentation.dashboard.Controller dashboardController = new hospital.presentation.dashboard.Controller(dashboardView, dashboardModel);

        dashboardView.setController(dashboardController);
        dashboardView.setModel(dashboardModel);
        tabbedPane.addTab("Dashboard", dashboardView.getDashboardPanel());

        //MVC de historico
        hospital.presentation.historico_recetas.HistoricoRecetasView historicoRecetasView = new hospital.presentation.historico_recetas.HistoricoRecetasView();
        hospital.presentation.historico_recetas.Model historicoRecetaModel = new hospital.presentation.historico_recetas.Model();
        hospital.presentation.historico_recetas.Controller historicoRecetasController = new hospital.presentation.historico_recetas.Controller(historicoRecetasView, historicoRecetaModel);
        tabbedPane.addTab("Historico", historicoRecetasView.getHitoricoRecetasPanel());

    }

    public void addMedicoTabs(JTabbedPane tabbedPane) {
        //MVC de prescripcion
        hospital.presentation.prescripcion.PrescripcionView prescripcionView = new hospital.presentation.prescripcion.PrescripcionView();
        hospital.presentation.prescripcion.Model prescripcionModel = new hospital.presentation.prescripcion.Model();
        hospital.presentation.prescripcion.Controller prescripcionController =  new hospital.presentation.prescripcion.Controller(prescripcionView, prescripcionModel);
        tabbedPane.addTab("Prescripcion", prescripcionView.getPrescripcionPanel());

        //MVC de dashboard
        hospital.presentation.dashboard.DashboardView dashboardViewMed = new hospital.presentation.dashboard.DashboardView();
        hospital.presentation.dashboard.Model dashboardModelMed = new hospital.presentation.dashboard.Model();
        hospital.presentation.dashboard.Controller dashboardControllerMed = new hospital.presentation.dashboard.Controller(dashboardViewMed, dashboardModelMed);
        dashboardViewMed.setController(dashboardControllerMed);
        dashboardViewMed.setModel(dashboardModelMed);
        tabbedPane.addTab("Dashboard", dashboardViewMed.getDashboardPanel());

        //MVC de historico
        hospital.presentation.historico_recetas.HistoricoRecetasView historicoRecetasViewMed = new hospital.presentation.historico_recetas.HistoricoRecetasView();
        hospital.presentation.historico_recetas.Model historicoRecetaModelMed = new hospital.presentation.historico_recetas.Model();
        hospital.presentation.historico_recetas.Controller historicoRecetasControllerMed = new hospital.presentation.historico_recetas.Controller(historicoRecetasViewMed, historicoRecetaModelMed);
        tabbedPane.addTab("Historico", historicoRecetasViewMed.getHitoricoRecetasPanel());
    }

    public void addFarmaceutaTabs(JTabbedPane tabbedPane) {
        //MVC de despacho
        DespachoView despachoView = new DespachoView();
        hospital.presentation.despacho.Model despachoModel = new hospital.presentation.despacho.Model();
        hospital.presentation.despacho.Controller controller = new hospital.presentation.despacho.Controller(despachoView, despachoModel);
        tabbedPane.addTab("Despacho", despachoView.getDespachoView());

        //MVC de dashboard
        hospital.presentation.dashboard.DashboardView dashboardViewFar = new hospital.presentation.dashboard.DashboardView();
        hospital.presentation.dashboard.Model dashboardModelFar = new hospital.presentation.dashboard.Model();
        hospital.presentation.dashboard.Controller dashboardControllerFar = new hospital.presentation.dashboard.Controller(dashboardViewFar, dashboardModelFar);
        dashboardViewFar.setController(dashboardControllerFar);
        dashboardViewFar.setModel(dashboardModelFar);
        tabbedPane.addTab("Dashboard", dashboardViewFar.getDashboardPanel());

        //MVC de historico
        hospital.presentation.historico_recetas.HistoricoRecetasView historicoRecetasViewMed = new hospital.presentation.historico_recetas.HistoricoRecetasView();
        hospital.presentation.historico_recetas.Model historicoRecetaModelMed = new hospital.presentation.historico_recetas.Model();
        hospital.presentation.historico_recetas.Controller historicoRecetasControllerMed = new hospital.presentation.historico_recetas.Controller(historicoRecetasViewMed, historicoRecetaModelMed);
        tabbedPane.addTab("Historico", historicoRecetasViewMed.getHitoricoRecetasPanel());
    }

    public void abrirVentanaPrincipal() {
        Usuario user = getUsuarioActual();
        if (user == null) return;

        JFrame mainWindow = new JFrame();
        mainWindow.setSize(1000,500);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setTitle("Hospital" + " " + user.getNombre() + " " + user.getId());

        JTabbedPane tabbedPane = new JTabbedPane();

        switch(user.getTipo()) {
            case "ADMINISTRADOR":
                addAdminTabs(tabbedPane);
                break;

            case "MEDICO":
                addMedicoTabs(tabbedPane);
                break;

            case "FARMACEUTA":
                addFarmaceutaTabs(tabbedPane);
                break;
            default:
                break;
        }

        mainWindow.setContentPane(tabbedPane);
        mainWindow.setLocationRelativeTo(null);

        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Service.instance().stop();
            }
        });
        mainWindow.setVisible(true);

    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}

