package hospital.presentation.farmaceutas;

import hospital.logic.Farmaceuta;

public class Controller {
    FarmaceutasView view;
    Model model;

    public Controller(FarmaceutasView view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        try {
            refreshList();
        } catch (Exception e) {
            System.err.println("Error cargando farmaceutas: " + e.getMessage());
        }
    }

    public void create(Farmaceuta e) throws  Exception{
        Service.instance().createFarmaceuta(e);
        model.setCurrent(new Farmaceuta());
    }

    public void update(Farmaceuta e) throws Exception {
        Service.instance().updateFarmaceuta(e);
        model.setCurrent(new Farmaceuta());
    }

    public void delete(Farmaceuta e) throws Exception{
        Service.instance().deleteFarmaceuta(e);
        model.setCurrent(new Farmaceuta());
    }

    public void clear() {
        model.setCurrent(new Farmaceuta());
    }

    public void read(String id) throws Exception {
        Farmaceuta e = new Farmaceuta();
        e.setId(id);
        Farmaceuta result = Service.instance().readFarmaceuta(e);
        model.setCurrent(result);
    }

    public void refreshList() throws Exception {
        model.setList(Service.instance().findAllFarmaceutas());
    }
}