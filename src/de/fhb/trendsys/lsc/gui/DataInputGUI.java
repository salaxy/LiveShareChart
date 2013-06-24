package de.fhb.trendsys.lsc.gui;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import de.fhb.trendsys.lsc.db.control.BusinessLogic;
import de.fhb.trendsys.lsc.model.AppModel;
import de.fhb.trendsys.lsc.model.NewAdvancedAndFancyAppModel;

/**
 * Mit dieser Klasse wird die JavaFX-Applikation gestartet, Sie beinhaltet
 * GUI-Beschreibung und GUI-Logik
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class DataInputGUI extends Application {

//	private BusinessLogic logic;
//	private NewAdvancedAndFancyAppModel model;

	@Override
	public void start(Stage stage) throws Exception {
		this.init(stage);
		stage.show();
	}

	private void init(Stage stage) {

//		model = new NewAdvancedAndFancyAppModel(this);
//		logic = new BusinessLogic(model);

		Group root = new Group();

		Scene scene = new Scene(root, 200, 200);

		stage.setScene(scene);

		FlowPane flowPane = new FlowPane(4, 1);
		flowPane.setPrefWrapLength(80);
		
		
        final Label rssLabel = new Label("rss");
        final Label nameLabel = new Label("name");
        final Label kursLabel = new Label("kurs");
		 
        final TextField rss = new TextField("rss");
        final TextField name = new TextField("name");
        final TextField kurs = new TextField("kurs");
        
        final Button ok = new Button("Ok");

        final Button grabber = new Button("Grabber");
        
        flowPane.getChildren().addAll(nameLabel,name, rssLabel,rss,kursLabel,kurs,ok, grabber);
        root.getChildren().add(flowPane);
        
     
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ok.setText("abgeschickt");
                
                trageEinInDB(rss.textProperty(),name.textProperty(),kurs.textProperty());
                rss.promptTextProperty();
                kurs.promptTextProperty();
                name.promptTextProperty();
                
            }

        });
        
        
        grabber.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	grabber.setText("B‰‰‰‰‰mmm!!!");
                //TODO grabber
            }

        });
        

		System.out.println("Refreshing...");

		System.out.println("Refresh finished.");
	}

	protected void trageEinInDB(StringProperty textProperty,
			StringProperty textProperty2, StringProperty textProperty3) {
		// TODO hier Frank
		
//		logic...
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}


}