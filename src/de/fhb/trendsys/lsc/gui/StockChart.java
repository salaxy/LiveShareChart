package de.fhb.trendsys.lsc.gui;

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
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import de.fhb.trendsys.lsc.db.control.BusinessLogic;
import de.fhb.trendsys.lsc.model.AppModel;

/**
 * Mit dieser Klasse wird die JavaFX-Applikation gestartet, Sie beinhaltet
 * GUI-Beschreibung und GUI-Logik
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class StockChart extends Application {

	private BusinessLogic logic;
	private AppModel model;

	private CategoryAxis xAxis;
	private NumberAxis yAxis;

	public static final String DEFAULT_URL = "http://www.oracle.com/us/index.html";

	@Override
	public void start(Stage stage) throws Exception {
		this.init(stage);
		stage.show();
	}

	private void init(Stage stage) {

		model = new AppModel();
		logic = new BusinessLogic(model);

		// GridPane grid = new GridPane();
		// grid.setHgap(10);
		// grid.setVgap(10);
		// grid.setPadding(new Insets(0, 10, 0, 10));


        final TabPane tabPane = new TabPane();
        tabPane.setPrefSize(950, 650);
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        final Tab chartTab = new Tab();
        chartTab.setText("Chart");

        final Tab webTab = new Tab();
        webTab.setText("WebView");
        
        tabPane.getTabs().addAll(chartTab, webTab);
        Group chartTabGroup = new Group();
        Group webTabGroup = new Group();
        
        chartTab.setContent(chartTabGroup);
		Group root = new Group();
		// root.getChildren().add(createChart());

		Scene scene = new Scene(root, 950, 650);
		

		// stage.setScene(new Scene(createChart()));
		// root.getChildren().add(createChart());
		stage.setScene(scene);
		stage.setResizable(false);

		// FlowPane flowPane = new FlowPane(2, 4);

		// flowPane.setPrefWrapLength(200);

		// HBox hBox = new HBox(3);
		// VBox vbox = new VBox(3);

		// hBox.getChildren().add(createChart());

		// createNewsTicker(stage, root, hBox);
		// createWebWindow();

		// root.getChildren().add(createChart(), );
		// grid.add(createChart(), 1, 1);
		// grid.add(createCoiceBox(),1,0);

		LineChart<String, Number> chart = createChart();
		chart.setLayoutX(20);
		chart.setLayoutY(10);
		chart.setPrefSize(600, 600);

		ChoiceBox<String> choice = createCoiceBox();
		choice.setLayoutX(10);
		choice.setLayoutY(10);

		ListView<String> listView = createListView();
		listView.setLayoutX(610);
		listView.setLayoutY(10);
		listView.setPrefSize(300, 500);


		
		chartTabGroup.getChildren().add(chart);
		chartTabGroup.getChildren().add(choice);
//		chartTab.add(chart);
//		chartTab.getChildren().add(choice);

		// hBox.setAlignment(Pos.CENTER_LEFT);

//		Group webView = createWebWindow();
//		webView.setLayoutX(610);
//		webView.setLayoutY(10);
//		webView.autosize();
//		webView.prefHeight(200);
//		webView.prefWidth(200);
		// webView.resize(100, 100);
		// root.getChildren().add(webView);
		// grid.addColumn(2, webView);

//		root.getChildren().add(webView);
//		root.getChildren().add(listView);
		Browser webView= new Browser();
		webTabGroup.getChildren().add(new Browser(600,950));
		
		webTab.setContent(webTabGroup);
		webView.layout();
		webTabGroup.autoSizeChildrenProperty().set(true);
		webTabGroup.autosize();
//		webView.getParent().prefWidth(100);
//		webView.getParent().autosize();
		
		
		
		chartTabGroup.getChildren().add(listView);

		// grid.add(, 2, 1);
		// hBox.setAlignment(Pos.CENTER_RIGHT);
		// root.getChildren().add(createNewsTicker(stage, root));

		// vbox.getChildren().add(hBox);
		// grid.getChildren().add(RectangleBuilder.create()
		// .arcHeight(30).arcWidth(15).x(0).y(0)
		// .fill(new Color(1, 1, 1, .55))
		// .width(stage.getScene().getWidth() - 6).height(30)
		// .stroke(Color.rgb(255, 255, 255, .70)).build());

		Group newsTicker = createNewsTicker(stage);
		newsTicker.toFront();
		// newsTicker.setBlendMode(BlendMode.COLOR_BURN);
		root.getChildren().add(tabPane);
		root.getChildren().add(newsTicker);
		// root.getChildren().add(grid);

		// vbox.setAlignment(Pos.BOTTOM_CENTER);
		// flowPane.getChildren().add(createNewsTicker(stage, root));

		// TODO Andy - timeline / animation

		System.out.println(webView.getBoundsInParent());

		System.out.println("Refreshing...");
		// logic.refresh(1);
		System.out.println("Refresh finished.");
	}

	protected ListView<String> createListView() {

		final ListView<String> listView = new ListView<String>();
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

		Button button = new Button("Red");
		button.setStyle("-fx-base: red;");

		javafx.scene.shape.Rectangle tickerRect = RectangleBuilder.create()
				.arcHeight(20).arcWidth(15).x(0).y(0)
				.fill(new Color(0, 0, 0, .55))
				.width(stage.getScene().getWidth() - 6).height(30)
				.stroke(Color.rgb(255, 255, 255, .70)).build();

		// tickerRect.setPickOnBounds(arg0)

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

		// hBox.getChildren().add(tickerArea);
		// root.getChildren().add(tickerArea);

		// add text

		Text news = TextBuilder.create().text(this.model.getActualNewsFeeds())
				.translateY(18).fill(Color.WHITE).build();

		tickerArea.getChildren().add(news);

		final TranslateTransition tickerAnimation = TranslateTransitionBuilder
				.create().node(news)
				.duration(Duration.millis((scene.getWidth() / 300) * 15000))
				.fromX(scene.widthProperty().doubleValue()).fromY(19)
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

	// public FlowPane addFlowPane() {
	// FlowPane flow = new FlowPane();
	// flow.setPadding(new Insets(5, 0, 5, 0));
	// flow.setVgap(4);
	// flow.setHgap(4);
	// flow.setPrefWrapLength(170); // preferred width allows for two columns
	// flow.setStyle("-fx-background-color: DAE6F3;");
	//
	// ImageView pages[] = new ImageView[8];
	// for (int i=0; i<8; i++) {
	// pages[i] = new ImageView(
	// new Image(LayoutSample.class.getResourceAsStream(
	// "graphics/chart_"+(i+1)+".png")));
	// flow.getChildren().add(pages[i]);
	// }
	//
	// return flow;
	// }

	protected Group createWebWindow() {

		Group web = new Group();
		// primaryStage.setScene(new Scene(root));

		WebView webView = new WebView();
		// webView.prefHeight(100);
		// webView.prefWidth(100);
		// webView.maxHeight(100);
		// webView.maxWidth(100);
		// webView.minHeight(50);
		// webView.minWidth(50);

		// webView.setLayoutX(2);
		// webView.setLayoutY(2);
		// webView.resize(200, 200);
		// webView.autosize();

		final WebEngine webEngine = webView.getEngine();

		// webEngine.load(DEFAULT_URL);

		webEngine.load(model.getActualOpenedNewsURL());

		// webEngine.loadContent("<b>asdf</b>");

		final TextField locationField = new TextField(DEFAULT_URL);

		webEngine.locationProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				locationField.setText(newValue);
			}
		});

		EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				webEngine.load(locationField.getText().startsWith("http://") ? locationField
								.getText() : "http://"
								+ locationField.getText());
			}
		};

		locationField.setOnAction(goAction);

		Button goButton = new Button("Go");

		goButton.setDefaultButton(true);

		goButton.setOnAction(goAction);

		System.out.println(webView.getHeight() + " " + webView.getWidth());

		// Layout logic

		// HBox hBox = new HBox(5);
		//
		// hBox.getChildren().setAll(locationField, goButton);

		// HBox.setHgrow(locationField, Priority.ALWAYS);

		// VBox vBox = new VBox(5);
		//
		// vBox.getChildren().setAll(hBox, webView);

		// VBox.setVgrow(webView, Priority.ALWAYS);

		// root.getChildren().add(webView);

		// hBox.getChildren().add(webView);

		webView.toBack();
//		web.getChildren().add(webView);
//		Region r = new Region();
//		r.getChildrenUnmodifiable().add(webView);
//		r.layout()

		return web;
	}

	protected ChoiceBox<String> createCoiceBox() {

		ChoiceBox<String> cb = new ChoiceBox<String>();

		for (String actual : this.model.getStockNames()) {
			cb.getItems().add(actual);
		}

		cb.getSelectionModel().selectFirst();
		cb.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> arg0,
					final String arg1, final String arg2) {
				// TODO Auto-generated method stub
				// value=arg2;

			}

		});
		// cb.addEventHandler(EventType.CHANGE, new EventHandler<ChangeEvent>(){
		//
		// @Override
		// public void handle(Event arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// });
		return cb;

		// root.getChildren().add(cb);
	}

	protected LineChart<String, Number> createChart() {

		xAxis = new CategoryAxis();
		// (0, 20, 1);;
		xAxis.setLabel("Time");
		yAxis = new NumberAxis();
		// yAxis = new NumberAxis(0, 100, 1);

		LineChart<String, Number> lineChart = new LineChart<String, Number>(
				xAxis, yAxis);

		// linechrt config
		lineChart.setId("Stockchart");
		// lineChart.setCreateSymbols(false);
		// lineChart.setAnimated(false);
		lineChart.setLegendVisible(false);
		lineChart.setTitle(this.model.getDataSeries().getName());
		xAxis.setLabel("Zeit");
		// xAxis.setForceZeroInRange(false);
		yAxis.setLabel("Preis");
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$",
				null));

		// Daten mit linechart verknuepfen - Databinding
		lineChart.getData().add(model.getDataSeries());

		return lineChart;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
