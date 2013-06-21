package de.fhb.trendsys.lsc.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.HyperlinkBuilder;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;
import de.fhb.trendsys.lsc.db.control.BusinessLogic;
import de.fhb.trendsys.lsc.model.NewAdvancedAndFancyAppModel;
import de.fhb.trendsys.lsc.model.NewsVO;

/**
 * Mit dieser Klasse wird die JavaFX-Applikation gestartet, Sie beinhaltet
 * GUI-Beschreibung und GUI-Logik
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
//TODO Refractoring to StockChartGUI
public class StockChart extends Application {

	private BusinessLogic logic;
	private NewAdvancedAndFancyAppModel model;

	private CategoryAxis xAxis;
	private NumberAxis yAxis;

	private LineChart<String, Number> lineChart;
	private TabPane tabPane;
	private Tab chartTab;
	private Tab webTab;
	private Group chartTabGroup;
	private Group webTabGroup;
	private Browser webContainer;
	private ChoiceBox<String> choiceBox;
//	private FlowPane listView;
	private ListView<String> listView;

	@Override
	public void start(Stage stage) throws Exception {
		this.init(stage);
		stage.show();
	}

	private void initTabs(Group root) {
		tabPane = new TabPane();
		tabPane.setPrefSize(950, 650);
		tabPane.setSide(Side.TOP);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		chartTab = new Tab();
		chartTab.setText("Chart");

		webTab = new Tab();
		webTab.setText("WebView");

		tabPane.getTabs().addAll(chartTab, webTab);
		chartTabGroup = new Group();
		webTabGroup = new Group();

		chartTab.setContent(chartTabGroup);
		webTab.setContent(webTabGroup);

		root.getChildren().add(tabPane);
	}

	private void init(Stage stage) {

		model = new NewAdvancedAndFancyAppModel();
		logic = new BusinessLogic(model);

		Group root = new Group();
		Scene scene = new Scene(root, 950, 650);
		stage.setScene(scene);
		stage.setResizable(false);

		initTabs(root);		
		
		webContainer = new Browser(600, 950);
		webTabGroup.getChildren().add(webContainer);

		LineChart<String, Number> chart = createChart();
		chart.setLayoutX(20);
		chart.setLayoutY(10);
		chart.setPrefSize(600, 600);
		chartTabGroup.getChildren().add(chart);

		ChoiceBox<String> choice = createChoiceBox();
		choice.setLayoutX(30);
		choice.setLayoutY(15);
		chartTabGroup.getChildren().add(choice);

		ListView<String> listView = createListView();
		listView.setLayoutX(620);
		listView.setLayoutY(45);
		listView.setPrefSize(300, 525);
		chartTabGroup.getChildren().add(listView);

		Group newsTicker = createNewsTicker(stage);
		newsTicker.toFront();
		root.getChildren().add(newsTicker);

		final Button button = new Button("Do not Push the RED-Button!");
		button.setStyle("-fx-base: red;");
		button.setLayoutX(500);
		button.setLayoutY(15);
		chartTabGroup.getChildren().add(button);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				button.setText("BÄMMMMMMMMM!!!!");
				logic.refresh(1);
			}

		});

		System.out.println("Refreshing GUI...");		
		logic.refresh();
		this.refresh();
		System.out.println("Refresh finished.");
	}
	
	
	protected void refresh(){
		
		//Choicebox refesh
		this.choiceBox.getItems().clear();
		
		for (String actual : this.model.getStockNames()) {
			this.choiceBox.getItems().add(actual);
		}
		
		//TODO chart refresh
		
		String selectedStock= choiceBox.getSelectionModel().getSelectedItem();
		if(selectedStock!=null){
			lineChart.setTitle(selectedStock);
			lineChart.setUserData(model.getDataSeries());
			lineChart.getData().clear();
			lineChart.getData().add(model.getDataSeries());
		}
		
		
		//TODO ticker
		
		
		//TODO listView refresh
		
		listView.getItems().clear();
		for(final NewsVO feed: this.model.getActualNewsFeeds()){
			
			Hyperlink actualLink = HyperlinkBuilder.create()
			.textFill(Color.WHITE)
			.text(feed.getTitle())
			.translateY(3)
			.tooltip(new Tooltip(feed.getUrl()))
			.build();
			
			
//			listView.getItems().
			
			
//			listView.getItems().add(actualLink);
		}

	}

	protected ListView<String> createListView() {

		//TODO Anbindung ans Model
		
		listView = new ListView<String>();
		listView.setItems(FXCollections.observableArrayList(

		"Row 1", "Row 2", "Long Row 3", "Row 4", "Row 5", "Row 6", "Row 7",
				"Row 8", "Row 9", "Row 10", "Row 11", "Row 12", "Row 13",
				"Row 14", "Row 15", "Row 16", "Row 17", "Row 18", "Row 19",
				"Row 20"

		));

		return listView;
	}

	protected Group createNewsTicker(Stage stage) {

		Group tickerArea = new Group();

		final Scene scene = stage.getScene();

		javafx.scene.shape.Rectangle tickerRect = RectangleBuilder.create()
				.arcHeight(20).arcWidth(15).x(0).y(0)
				.fill(new Color(0, 0, 0, .55))
				.width(stage.getScene().getWidth() - 6).height(30)
				.stroke(Color.rgb(255, 255, 255, .70)).build();

		javafx.scene.shape.Rectangle clipRegion = RectangleBuilder.create()
				.arcHeight(20).arcWidth(15).x(0).y(0)
				.fill(new Color(0, 0, 0, .55))
				.width(stage.getScene().getWidth() - 6).height(30)
				.stroke(Color.rgb(255, 255, 255, .70)).build();

		tickerArea.setClip(clipRegion);

		tickerArea.setTranslateX(6);
		tickerArea.translateYProperty().bind(
				scene.heightProperty().subtract(tickerRect.getHeight() + 6));
		tickerRect.widthProperty().bind(scene.widthProperty().subtract(16));
		clipRegion.widthProperty().bind(scene.widthProperty().subtract(16));
		tickerArea.getChildren().add(tickerRect);
		
		//experimental Hyperlink	
		List<Node> hyperlinks= new ArrayList<Node>();
		
		//erzeuge Hyperlinks mit Listener und fuege sie der Liste hinzu
<<<<<<< HEAD
		if (this.model.getSelectedChart() != null) {
			for(final NewsVO feed: this.model.getSelectedChart().getNewsFeeds()){
				
				Hyperlink actualLink = HyperlinkBuilder.create()
				.textFill(Color.WHITE)
				.text(feed.getTitle())
				.translateY(3)
				.tooltip(new Tooltip(feed.getUrl()))
				.build();
				
				
				actualLink.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent e) {
		            	System.out.println("This link is clicked: " + feed.getUrl());
		            	tabPane.getSelectionModel().select(webTab);
		            	webContainer.webEngine.load(feed.getUrl());
		            }
		        });
				
				hyperlinks.add(actualLink);
				hyperlinks.add(TextBuilder
						.create()
						.text("  +++++  ")
						.translateY(3)
						.fill(Color.WHITE).build());
				
			}
=======
		for(final NewsVO feed: this.model.getActualNewsFeeds()){
			
			Hyperlink actualLink = HyperlinkBuilder.create()
			.textFill(Color.WHITE)
			.text(feed.getTitle())
			.translateY(3)
			.tooltip(new Tooltip(feed.getUrl()))
			.build();
			
			
			actualLink.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent e) {
	            	System.out.println("This link is clicked: " + feed.getUrl());
	            	tabPane.getSelectionModel().select(webTab);
	            	webContainer.webEngine.load(feed.getUrl());
	            }
	        });
			
			hyperlinks.add(actualLink);
			hyperlinks.add(TextBuilder
					.create()
					.text("  +++++  ")
					.translateY(3)
					.fill(Color.WHITE).build());
			
			
			//TODO adding change of stock today as red or green percent
			
>>>>>>> 18a9c1a6be062e16cdde7f013406e990a49e8c6b
		}
		
		
		Group tickerStripe= new Group();
		tickerStripe.setLayoutX(0);
		tickerStripe.setLayoutY(0);
		FlowPane tickerFlow = new FlowPane();
		tickerFlow.setPrefWrapLength(2000);
		tickerStripe.getChildren().add(tickerFlow);
		tickerFlow.getChildren().addAll(hyperlinks);
        tickerArea.getChildren().add(tickerStripe);

		final TranslateTransition tickerAnimation = TranslateTransitionBuilder
				.create().node(tickerStripe)
				.duration(Duration.millis((scene.getWidth() / 300) * 15000))
				.fromX(scene.widthProperty().doubleValue()).fromY(10)
				.fromX(scene.widthProperty().doubleValue()).fromY(0)
				.toX(-scene.widthProperty().doubleValue())
				.interpolator(Interpolator.LINEAR).cycleCount(1).build();

		tickerAnimation.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				tickerAnimation.stop();
				tickerAnimation.setFromX(scene.getWidth());
				tickerAnimation.setDuration(new Duration(
						(scene.getWidth() / 300) * 15000));
				tickerAnimation.playFromStart();

			}
		});

		tickerAnimation.play();

		return tickerArea;
	}

	protected ChoiceBox<String> createChoiceBox() {

		choiceBox = new ChoiceBox<String>();

		for (String actual : this.model.getChartNames()) {
			choiceBox.getItems().add(actual);
		}

		choiceBox.getSelectionModel().selectFirst();
		choiceBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> arg0,
					final String arg1, final String name) {

				System.out.println("ChoiceBox: " + name);

				model.setSelectedChart(model.returnChartByName(name));
				// aktualisiere Chart
				lineChart.setTitle(name);
				lineChart.setUserData(model.getSelectedChart().getChart());
				lineChart.getData().clear();
				lineChart.getData().add(model.getSelectedChart().getChart());
			}

		});

		return choiceBox;
	}

	protected LineChart<String, Number> createChart() {

		xAxis = new CategoryAxis();
		xAxis.setLabel("Time");
		yAxis = new NumberAxis();
		yAxis = new NumberAxis(0, 100, 1);

		lineChart = new LineChart<String, Number>(xAxis, yAxis);

		// linechrt config
		lineChart.setId("Stockchart");
		// lineChart.setCreateSymbols(false);
		lineChart.setAnimated(false);
		lineChart.setLegendVisible(false);
		lineChart.setTitle("no stock loaded");
		xAxis.setLabel("Zeit");
		// xAxis.setForceZeroInRange(false);
		yAxis.setLabel("Preis");
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$",
				null));

		// Daten mit linechart verknuepfen - Databinding
//		lineChart.getData().add(model.getDataSeries());

		return lineChart;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
