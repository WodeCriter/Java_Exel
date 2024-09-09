package exel.userinterface.resources.app.popups.newsheet;


import exel.eventsys.EventBus;
import exel.eventsys.events.CreateNewSheetEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateNewSheetScreenController {



    private EventBus eventBus;

    @FXML
    private TextField textboxSheetName;

    @FXML
    private TextField textboxRowNum;

    @FXML
    private TextField textboxColNum;

    @FXML
    private Button buttonCreateSheet;

    @FXML
    void createNewSheetListener(ActionEvent event) {
        eventBus.publish(new CreateNewSheetEvent(textboxSheetName.getText()
                ,5
                ,5
                ,Integer.parseInt(textboxColNum.getText())
                ,Integer.parseInt(textboxRowNum.getText())));
    }


    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}

