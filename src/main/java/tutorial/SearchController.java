package tutorial;


import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.*;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zul.ext.*;

import java.util.*;

public class SearchController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    @Wire
    private Textbox keywordBox;
    @Wire
    private Listbox carListbox;
    @Wire
    private Label modelLabel;
    @Wire
    private Label makeLabel;
    @Wire
    private Label priceLabel;
    @Wire
    private Label colourLabel;
    @Wire
    private Label descriptionLabel;
    @Wire
    private Image previewImage;
    @Wire
    private Hlayout detailedCar;
    @Wire
    private Window addCarWindow;
    @Wire
    private Textbox modelTextBox;
    @Wire
    private Textbox makeTextBox;
    @Wire
    private Intbox priceIntBox;
    @Wire
    private Textbox colourTextBox;
    @Wire
    private Button deleteCar;


    private ListModelList<Car> dataModel = new ListModelList<>();
    private CarService carService = new CarServiceImpl();
    private Car selected;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dataModel.addAll(carService.findAll());
        carListbox.setModel(dataModel);
    }

    @Listen("onClick = #searchButton; onOK = window")
    public void search() {
        String keyword = keywordBox.getValue();
        dataModel.clear();
        dataModel.addAll(carService.search(keyword));
        if(carService.search(keyword).isEmpty()){
            detailedCar.setVisible(false);
            deleteCar.setVisible(false);
        }
        else{
            detailedCar.setVisible(true);
            deleteCar.setVisible(true);
        }
    }

    @Listen("onSelect = #carListbox")
    public void showDetail() {
        Set<Car> selection = dataModel.getSelection();
        Car selected = selection.iterator().next();
        this.selected = selected;
        previewImage.setSrc(selected.getPreview());
        modelLabel.setValue(selected.getModel());
        makeLabel.setValue(selected.getMake());
        priceLabel.setValue(selected.getPrice().toString());
        colourLabel.setValue(selected.getColour());
        descriptionLabel.setValue(selected.getDescription());
        deleteCar.setVisible(true);
    }

    @Listen("onCLick = #addCarButton")
    public void addCar() {
        Executions.sendRedirect("addCar.zul");
        Car car = new Car();
        car.setModel(modelTextBox.getValue());
        car.setMake(modelTextBox.getValue());
        car.setPrice(Integer.parseInt(modelTextBox.getValue()));
        car.setColour(modelTextBox.getValue());
        carService.saveCar(car);
    }

    @Listen("onClick = #deleteCar")
    public void deleteCar(){
        if (this.selected != null){
            Messagebox.show("Are you sure?",
                    "", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener(){
                        public void onEvent(Event e){
                            if(Messagebox.ON_OK.equals(e.getName())){
                                detailedCar.setVisible(false);
                                carService.deleteCar(selected);
                                search();
                            }else if(Messagebox.ON_CANCEL.equals(e.getName())){
                                //Cancel is clicked
                            }
                        }
                    }
            );
        }
    }
}
