package de.fhb.trendsys.lsc.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.chart.XYChart;
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
import de.fhb.trendsys.lsc.model.NewAdvancedAndFancyAppModel;
import de.fhb.trendsys.lsc.model.NewsVO;

/**
 * Mit dieser Klasse wird die JavaFX-Applikation gestartet, Sie beinhaltet
 * GUI-Beschreibung und GUI-Logik
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class StockChartGUI extends Application {

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
	private NewsContentPane webContainer;
	private ChoiceBox<String> choiceBox;
	
    final ObservableList<String> choiceBoxData = FXCollections.<String>emptyObservableList();
	private TilePane stockNewsPane;
	private Button bigRedButton;
	
	private Series<String, Number> testSeries;
	
	private ArrayList<NewsVO> testFeeds;
	
	
	public void initTestData(){
		
		testSeries = new XYChart.Series<String, Number>();
		testSeries.setName("TestAktie-Numbers");
		
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(1*6000)), 22));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(2*6000)), 13));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(3*6000)), 16));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(4*6000)), 23));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(5*6000)), 35));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(6*6000)), 33));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(7*6000)), 40));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(8*6000)), 44));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(9*6000)), 35));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(10*6000)), 30));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(11*6000)), 28));
		testSeries.getData().add(new XYChart.Data(this.millisToHHMM(System.currentTimeMillis()+(12*6000)), 30));
		
		
		testFeeds= new ArrayList<NewsVO>();
		testFeeds.add(new NewsVO("Flying away, amaizing machines!","description","http://www.tagesschau.de/inland/eurohawk152.html", new Date(System.currentTimeMillis())));
		testFeeds.add(new NewsVO("Water, water annnd water again!","description","http://www.tagesschau.de/inland/hochwasser1142.html", new Date(System.currentTimeMillis())));
		testFeeds.add(new NewsVO("Dictator at work!","description","http://www.tagesschau.de/ausland/eu-erdogan100.html", new Date(System.currentTimeMillis())));
		testFeeds.add(new NewsVO("Check thisss out!","description","http://www.tagesschau.de/inland/geheimdienste110.html", new Date(System.currentTimeMillis())));
		testFeeds.add(new NewsVO("Whats going on in Berlin, master of masters is coming!","discription","http://www.rbb-online.de/nachrichten/politik/2013_06/obama_besuch_sorgt_fuer_ausnahmezustand.html", new Date(System.currentTimeMillis())));
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		initTestData();
		model = new NewAdvancedAndFancyAppModel();
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

		bigRedButton= createBigRedButton();
		chartTabGroup.getChildren().add(bigRedButton);


		logic = new BusinessLogic(model);
		
		//hier erst das erste mal zeichnen!!!
		System.out.println("Refreshing GUI...");		
		logic.refresh(1);
//		this.refresh();
		System.out.println("Refresh finished.");
		
		
	}

	protected Button createBigRedButton() {
		final Button button;
		button = new Button("Do not Push the RED-Button!");
		button.setStyle("-fx-base: red;");
		button.setLayoutX(500);
		button.setLayoutY(15);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				button.setText("BÄMMMMMMMMM!!!!");
				logic.refresh();
//				
//				// aktualisiere Chart
//				model.setSelectedChart(model.returnChartById(1));
//				lineChart.setTitle(model.getSelectedChart().getName());
//				lineChart.setUserData(model.getSelectedChart().getChart());
//				lineChart.getData().clear();
//				lineChart.getData().add(model.getSelectedChart().getChart());
//				
//				choiceBox.getItems().add("bla " + System.currentTimeMillis());
				
				//TODO
				System.out.println(millisToHHMM(System.currentTimeMillis()));
				System.out.println(choiceBox.itemsProperty().get().size());
				
				for(String act: choiceBox.itemsProperty().get()){
					System.out.println(act);
				}
				
//				choiceBox.itemsProperty().get().add(millisToHHMM(System.currentTimeMillis()));
//				model.getChartNamesList().add("test");
			}

		});
		return button;
	}
	
	
	protected void refresh(){
		
		//Choicebox refesh
		/*this.choiceBox.getItems().clear();
		
		for (String actual : this.model.getChartNamesList()) {
			this.choiceBox.getItems().add(actual);
		}*/
		
		//TODO chart refresh
		
//		String selectedStock= choiceBox.getSelectionModel().getSelectedItem();
//		if(model.getSelectedChart()!=null){
//			lineChart.setTitle(selectedStock);
//			lineChart.setUserData(model.getSelectedChart());
//			lineChart.getData().clear();
//			lineChart.getData().add(model.getSelectedChart().getChart());
//		}
		
		
		//TODO ticker
		
		
		//TODO listView refresh
		
//		listView.getItems().clear();
		/*for(final NewsVO feed: this.model.getSelectedChart().getNewsFeeds()){
			
			Hyperlink actualLink = HyperlinkBuilder.create()
			.textFill(Color.WHITE)
			.text(feed.getTitle())
			.translateY(3)
			.tooltip(new Tooltip(feed.getUrl()))
			.build();
			
			
//			listView.getItems().
			
			
//			listView.getItems().add(actualLink);
		}*/

	}

	protected TilePane createStockNewsPane() {

		//TODO Anbindung ans Model
		
		stockNewsPane = new TilePane();
//		stockNewsPane.setStyle("-fx-background-color: DAE6F3;");
//		stockNewsPane.setOrientation(Orientation.VERTICAL);
		stockNewsPane.setStyle("-fx-background-color: F9FCB6;");
		stockNewsPane.setPadding(new Insets(10, 10, 10, 10));
		stockNewsPane.setPrefColumns(1);
		stockNewsPane.setTileAlignment(Pos.CENTER_LEFT);

		//experimental Hyperlink	
		List<Node> hyperlinks= new ArrayList<Node>();
		
		for(final NewsVO feed: testFeeds){
			
			Hyperlink actualLink = HyperlinkBuilder.create()
			.textFill(Color.BLACK)
			.font(Font.font("Verdana", FontWeight.BOLD, 10))
			.text(feed.getTitle())
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

			stockNewsPane.getChildren().add(actualLink);	
		}

		return 	stockNewsPane;
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
//		if (this.model!= null)
//		if (this.model.getSelectedChart() != null) {
//			for(final NewsVO feed: this.model.getSelectedChart().getNewsFeeds()){
//				
//				Hyperlink actualLink = HyperlinkBuilder.create()
//				.textFill(Color.WHITE)
//				.font(Font.font("Verdana", FontWeight.BOLD, 10))
//				.text(feed.getTitle())
//				.translateY(3)
//				.tooltip(new Tooltip(feed.getUrl()))
//				.build();
//				
//				
//				actualLink.setOnAction(new EventHandler<ActionEvent>() {
//		            @Override
//		            public void handle(ActionEvent e) {
//		            	System.out.println("This link is clicked: " + feed.getUrl());
//		            	tabPane.getSelectionModel().select(webTab);
//		            	webContainer.webEngine.load(feed.getUrl());
//		            }
//		        });
//				
//				hyperlinks.add(actualLink);
//				hyperlinks.add(TextBuilder
//						.create()
//						.text("  +++++  ")
//						.translateY(3)
//						.fill(Color.WHITE).build());
//				
//			}
//		}
		
		// insert Stock values in red or green color
		//*****************************************************
		//with testseries
		
		double stockDayDiff= testSeries.getData().get(0).getYValue().doubleValue()-testSeries.getData().get(testSeries.getData().size()-1).getYValue().doubleValue();
		double percentageDiff= 0d;
		
		if(stockDayDiff!=0d){
			percentageDiff=stockDayDiff/testSeries.getData().get(0).getYValue().doubleValue();	
		}
		percentageDiff=-percentageDiff;
		
		hyperlinks.add(TextBuilder
				.create()
				.text("  +++++  ")
				.font(Font.font("Verdana", FontWeight.BOLD, 12))
				.translateY(3)
				.fill(Color.WHITE).build());
		
		if(stockDayDiff>=0d){
			hyperlinks.add(TextBuilder
					.create()
					.text("  "+ testSeries.getName() + " " + String.format(" %.2f", percentageDiff) + "%")
					.font(Font.font("Verdana", FontWeight.BOLD, 12))
					.translateY(3)
					.fill(Color.DARKRED).build());
		}else{
			hyperlinks.add(TextBuilder
					.create()
					.text("  "+ testSeries.getName() + " " + String.format("+ %.2f", percentageDiff) + "%")
					.font(Font.font("Verdana", FontWeight.BOLD, 12))
					.translateY(3)
					.fill(Color.DARKGREEN).build());	
		}

		hyperlinks.add(TextBuilder
				.create()
				.text("  +++++  ")
				.translateY(3)
				.fill(Color.WHITE).build());
		//****************************************************************
		
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

		choiceBox = new ChoiceBox<String>(this.model.getChartNamesList());
		
		choiceBox.getSelectionModel().selectFirst();
		choiceBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> arg0,
					final String arg1, final String name) {

				System.out.println("ChoiceBox: " + name);

				model.setChartByName(name);
				// aktualisiere Chart
				lineChart.setTitle(name);
//				lineChart.setUserData(model.getSelectedChart().getChart());
//				lineChart.getData().clear();
//				lineChart.getData().add(model.getSelectedChart().getChart());
			}

		});

		return choiceBox;
	}

	protected LineChart<String, Number> createChart() {

		xAxis = new CategoryAxis();
		xAxis.setLabel("Time");
		yAxis = new NumberAxis();
		yAxis = new NumberAxis(0, 100, 1);
		
		lineChart=new LineChart<>(xAxis, yAxis);	
		// Model mit linechart verknuepfen - Databinding
		lineChart.setUserData(this.model.getSelectedChart());

		// linechrt config
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
	
	
	/**
	 * wandelt Zeit von millisekunden in einen hh:mm-Formatierten String um
	 * @param millis
	 * @return Zeit "hh:mm"
	 */
	public String millisToHHMM(long millis){
		
		Date d = new Date(millis);
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		int mm = c.get(Calendar.MINUTE);
		int hh =c.get(Calendar.HOUR_OF_DAY);
		
		return String.format("%02d:%02d",hh,mm);
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
