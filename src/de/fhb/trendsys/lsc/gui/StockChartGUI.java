package de.fhb.trendsys.lsc.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.HyperlinkBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;

import de.fhb.trendsys.lsc.db.control.BusinessLogic;
import de.fhb.trendsys.lsc.model.ChartVO;
import de.fhb.trendsys.lsc.model.AppModel;
import de.fhb.trendsys.lsc.model.NewsVO;

/**
 * Mit dieser Klasse wird die JavaFX-Applikation gestartet, Sie beinhaltet
 * GUI-Beschreibung und GUI-Logik
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class StockChartGUI extends Application {

	private BusinessLogic logic;
	private AppModel model;

	private CategoryAxis xAxis;
	private NumberAxis yAxis;

	private LineChart<String, Number> lineChart;
	private TabPane tabPane;
	private Tab chartTab;
	private Tab webTab;
	private Group chartTabGroup;
	private Group webTabGroup;
	private NewsContentPane webContainer;
	private ChoiceBox<String> choiceBox;
	private FlowPane tickerFlow;
	private TilePane stockNewsPane;
	private Button updateButton;
	

	@Override
	public void start(Stage stage) throws Exception {
		model = new AppModel(this);
		logic = new BusinessLogic(model);
		this.initComponents(stage);
		stage.show();
	}

	private void initTabs(Group root) {
		tabPane = new TabPane();
		tabPane.setPrefSize(950, 650);
		tabPane.setSide(Side.TOP);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		chartTab = new Tab();
		chartTab.setText("Kurse");

		webTab = new Tab();
		webTab.setText("Geöffneter Feed");

		tabPane.getTabs().addAll(chartTab, webTab);
		chartTabGroup = new Group();
		webTabGroup = new Group();

		chartTab.setContent(chartTabGroup);
		webTab.setContent(webTabGroup);

		root.getChildren().add(tabPane);
	}

	/**
	 * initialiseren der GUI-Komponeten
	 * @param stage
	 */
	private void initComponents(Stage stage) {
		
		Group root = new Group();
		Scene scene = new Scene(root, 950, 650);
		stage.setScene(scene);
		stage.setResizable(false);

		initTabs(root);		
		
		webContainer = new NewsContentPane(600, 950);
		webTabGroup.getChildren().add(webContainer);

		LineChart<String, Number> chart = createChart();
		chart.setLayoutX(20);
		chart.setLayoutY(10);
		chart.setPrefSize(600, 600);
		chartTabGroup.getChildren().add(chart);

		choiceBox = createChoiceBox();
		choiceBox.setLayoutX(30);
		choiceBox.setLayoutY(15);
		chartTabGroup.getChildren().add(choiceBox);

		stockNewsPane = createStockNewsPane();
		stockNewsPane.setLayoutX(620);
		stockNewsPane.setLayoutY(45);
		stockNewsPane.setPrefSize(300, 525);
		chartTabGroup.getChildren().add(stockNewsPane);

		Group newsTicker = createNewsTicker(stage);
		newsTicker.toFront();
		root.getChildren().add(newsTicker);

		updateButton= createUpdateButton();
		chartTabGroup.getChildren().add(updateButton);
		
		logic.refresh();
		model.updateChartNames();
		model.updateTicker();
	}

	protected Button createUpdateButton() {
		final Button button;
		button = new Button("Manual Update");
		button.setStyle("-fx-base: red;");
		button.setLayoutX(500);
		button.setLayoutY(15);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//model.initTestData();
				logic.refresh();
				model.updateChartNames();
				refreshTicker();				
			}

		});
		return button;
	}
	
	/**
	 * refresh des Tickerbandes
	 */
	public void refreshTicker(){
		List<Node> hyperlinks= new ArrayList<Node>();
		addNewsToTicker(hyperlinks);
		addStockPercentagesToTickerNodes(hyperlinks);
		tickerFlow.getChildren().clear();
		tickerFlow.getChildren().addAll(hyperlinks);
	}

	/**
	 * erstellt das TilePane im Chart-Tab
	 * @return
	 */
	protected TilePane createStockNewsPane() {
		stockNewsPane = new TilePane();
		stockNewsPane.setStyle("-fx-background-color: F9FCB6;");//DAE6F3
		stockNewsPane.setPadding(new Insets(10, 10, 10, 10));
		stockNewsPane.setPrefColumns(1);
		stockNewsPane.setTileAlignment(Pos.CENTER_LEFT);
		refreshStockNewsPane("dummy");
		
		return stockNewsPane;
	}

	private void refreshStockNewsPane(String name) {
		ChartVO currentChart = this.model.returnChartByName(name);
		
		if(currentChart!=null){
			stockNewsPane.getChildren().clear();
			
			for(final NewsVO feed: currentChart.getNewsFeeds()){
				
				Hyperlink actualLink = HyperlinkBuilder.create()
				.textFill(Color.BLACK)
				.font(Font.font("Verdana", FontWeight.BOLD, 10))
				.text(this.model.millisToHHMM(feed.getTime().getTime()) + " Uhr  " + feed.getTitle())
				.tooltip(new Tooltip(feed.getUrl()))
				.build();
				
				actualLink.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent e) {
		            	tabPane.getSelectionModel().select(webTab);
		            	webContainer.webEngine.load(feed.getUrl());
		            }
		        });
	
				stockNewsPane.getChildren().add(actualLink);	
			}
		}
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
		
		//Texte hinzufuegen
		List<Node> hyperlinks= new ArrayList<Node>();
		addNewsToTicker(hyperlinks);
		addStockPercentagesToTickerNodes(hyperlinks);
		
		Group tickerStripe= new Group();
		tickerStripe.setLayoutX(0);
		tickerStripe.setLayoutY(0);
		tickerFlow = new FlowPane();
		tickerFlow.setPrefWrapLength(2000);
		tickerStripe.getChildren().add(tickerFlow);
		tickerFlow.getChildren().addAll(hyperlinks);
        tickerArea.getChildren().add(tickerStripe);

        //Animation 
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

	private void addNewsToTicker(List<Node> hyperlinks) {
		//erzeuge Hyperlinks mit Listener und fuege sie der Liste hinzu
		for(final NewsVO feed: this.model.getTickerNews()){
			
			Hyperlink actualLink = HyperlinkBuilder.create()
			.textFill(Color.WHITE)
			.font(Font.font("Verdana", FontWeight.BOLD, 10))
			.text(feed.getTitle())
			.translateY(3)
			.tooltip(new Tooltip(feed.getUrl()))
			.build();
			
			actualLink.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent e) {
	            	tabPane.getSelectionModel().select(webTab);
	            	webContainer.webEngine.load(feed.getUrl());
	            }
	        });
			
			hyperlinks.add(TextBuilder
					.create()
					.text(" + ")
					.translateY(3)
					.fill(Color.WHITE).build());
			
			hyperlinks.add(actualLink);
			
			hyperlinks.add(TextBuilder
					.create()
					.text(" + ")
					.translateY(3)
					.fill(Color.WHITE).build());
		}
	}

	/**
	 * erstellt Text-Nodes für Prozent-Anzeigen der Aktien im Ticker
	 * @param hyperlinks
	 */
	private void addStockPercentagesToTickerNodes(List<Node> hyperlinks) {
		for(ChartVO currentChart: this.model.getChartList()){
			
			String seriesName=currentChart.getName();
			
			double stockDayDiff = currentChart.getChangeInPercents();
			hyperlinks.add(TextBuilder
					.create()
					.text(" * ")
					.translateY(3)
					.fill(Color.WHITE).build());
			
			hyperlinks.add(TextBuilder
					.create()
					.text(seriesName + " ")
					.font(Font.font("Verdana", FontWeight.BOLD, 12))
					.translateY(3)
					.fill(Color.WHITE).build());
			
			if(stockDayDiff >= 0.01d){
				hyperlinks.add(TextBuilder
						.create()
						.text(String.format("+%.2f", stockDayDiff) + "%")
						.font(Font.font("Verdana", FontWeight.BOLD, 12))
						.translateY(3)
						.fill(Color.LIGHTGREEN).build());
			}
			else if (stockDayDiff < 0d){
				hyperlinks.add(TextBuilder
						.create()
						.text(String.format("%.2f", stockDayDiff) + "%")
						.font(Font.font("Verdana", FontWeight.BOLD, 12))
						.translateY(3)
						.fill(Color.RED).build());	
			}
			else {
				hyperlinks.add(TextBuilder
						.create()
						.text(String.format("+/-%.2f", stockDayDiff) + "%")
						.font(Font.font("Verdana", FontWeight.BOLD, 12))
						.translateY(3)
						.fill(Color.YELLOW).build());
			}
			
			hyperlinks.add(TextBuilder
					.create()
					.text(" * ")
					.translateY(3)
					.fill(Color.WHITE).build());
		}
	}

	protected ChoiceBox<String> createChoiceBox() {
		ObservableList<String> chartNamesList = this.model.getChartNamesList();
		choiceBox = new ChoiceBox<String>(chartNamesList);
		choiceBox.getSelectionModel().selectFirst();
		choiceBox.valueProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				logic.refreshByName(newValue);
				refreshChart(newValue);
				refreshStockNewsPane(newValue);
			}
		});

		return choiceBox;
	}
	/**
	 * passt das Chart nach Auswahl der choicebox an
	 * @param name
	 */
	private void refreshChart(final String name) {
		Series<String, Number> series = model.getCurrentChartByName(name);
		if(series != null){
			lineChart.getData().clear();
			lineChart.getData().setAll(series);					
			lineChart.setTitle(series.getName());	
		}
	}

	protected LineChart<String, Number> createChart() {

		xAxis = new CategoryAxis();
		xAxis.setLabel("Time");
		yAxis = new NumberAxis();
		yAxis = new NumberAxis(0, 100, 1);
		lineChart=new LineChart<String,Number>(xAxis, yAxis);
		
		refreshChart("dummy");

		// linechart config
		lineChart.setId("Stockchart");
		// lineChart.setCreateSymbols(false);
		lineChart.setAnimated(false);
		lineChart.setLegendVisible(false);
		lineChart.setTitle("no stock loaded");
		xAxis.setLabel("Zeit");
		// xAxis.setForceZeroInRange(false);
		yAxis.setLabel("Preis");
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$", null));
	
		return lineChart;
	}
	
	@Override
	public void stop() throws Exception {
		logic.shutdown();
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
