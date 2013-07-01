package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.fhb.trendsys.lsc.gui.StockChartGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * Model - Verwaltet und hält Daten der Appliaktion
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * @author Frank Mertens
 */
public class AppModel {
	
	private StockChartGUI gui;
	private ArrayList<ChartVO> availableChartVO;
	private ArrayList<String> availableChartNames;
	private ObservableList<String> observableChartNames;
	
	/**
	 * Standard-Konstruktor mit DummyVO
	 */
	public AppModel(StockChartGUI gui) {
		this.gui = gui;
		availableChartVO = new ArrayList<ChartVO>();
		availableChartNames = new ArrayList<String>();
		observableChartNames = FXCollections.<String>observableList(availableChartNames);

		//dummy nicht rausnehmen, erstmal benötigt, sonst exception in der GUI
		// Edit von Frank: jetzt nicht mehr ;)
		//createDummyChartVO();	
	}
	
	/**
	 * Ruft die GUI auf, den Newsticker zu aktualisieren.
	 * 
	 * @see StockChartGUI#refreshTicker()
	 */
	public void updateTicker() {
		if (gui != null)
			gui.refreshTicker();
	}

	/**
	 * Aktualisiert das ViewModel der Aktienkursverläufe.
	 */
	public void updateChartNames(){
		if (observableChartNames != null) {
			observableChartNames.clear();
		
			for(ChartVO currentChart : availableChartVO){
				observableChartNames.add(currentChart.getName());
			}
		}
	}
	
	/**
	 * Erstellt einen Dummy für einen ChartVO-Eintrag.
	 * Diese Methode ist zu Testzwecken vorhanden.
	 * 
	 * @see #initTestData()
	 */
	private void createDummyChartVO() {
		XYChart.Series<String, Number> dummyChart= new XYChart.Series<String, Number>();
		dummyChart.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(1*60000)), 16));
		dummyChart.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(2*60000)), 13));
		dummyChart.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(3*60000)), 18));
		dummyChart.setName("dummyChart");
		
		ArrayList<NewsVO> dummyFeeds = new ArrayList<NewsVO>();
		dummyFeeds.add(new NewsVO("Go, Goo, Goog, Googl, Google!","description","http://www.google.de", new Date(System.currentTimeMillis())));
		
		ChartVO dummyVO = new ChartVO(-1, "dummy");
		dummyVO.setChart(dummyChart);
		dummyVO.setNewsFeeds(dummyFeeds);
		availableChartNames.add("dummy");
		availableChartVO.add(dummyVO);
		
		updateTicker();
	}
	
	/**
	 * @return Liste aller Aktiendaten
	 */
	public ArrayList<ChartVO> getChartList() {
		return availableChartVO;
	}

	/**
	 * Fügt eine Aktie zu einer ArrayList aller verfügbaren Aktien hinzu.
	 * Helfermethode
	 * @param neue Aktie
	 */
	public void addToChartList(final ChartVO chart) {
		this.availableChartVO.add(chart);
		availableChartNames.add(chart.getName());
	}
	
	/**
	 * @return Liste der neusten News jeder Aktie
	 */
	public ArrayList<NewsVO> getTickerNews() {
		ArrayList<NewsVO> returnList = new ArrayList<NewsVO>();
		
		for (ChartVO chart : availableChartVO) {
			ArrayList<NewsVO> newsList = chart.getNewsFeeds();
			
			if (newsList.size() > 0) {
				NewsVO latestNews = newsList.get(newsList.size() - 1);
				returnList.add(latestNews);
			}
		}
		
		return returnList;
	}
	

	/**
	 * Gibt eine Aktie mit der passenden Datenbank-ID zurück.
	 * Wird keine gefunden, wird null zurückgegeben.
	 * 
	 * @param Datenbank-ID
	 * @return Aktie
	 * @see #returnChartByName(String)
	 */
	public ChartVO returnChartById(int id) {
		for (ChartVO chart : availableChartVO) {
			if (chart.getId() == id)
				return chart;
		}
		return null;
	}
	
	/**
	 * Gibt eine Aktie mit dem passenden Name zurück.
	 * Wird keine gefunden, wird null zurückgegeben.
	 * 
	 * @param Name der Aktie
	 * @return Aktie
	 * @see #returnChartById(int)
	 */
	public ChartVO returnChartByName(String name) {
		for (ChartVO chart : availableChartVO) {
			if (chart.getName().equals(name))
				return chart;
		}
		return null;
	}
	
	/**
	 * Ermittelt einen Aktienkurs anhand ihres Firmennamens.
	 * Wird keine gefunden, wird null zurückgegeben.
	 * @param Firmenname
	 * @return Aktienkurs
	 */
	public Series<String,Number> getCurrentChartByName(String name) {
		for (ChartVO chart : availableChartVO) {
			if (name!=null&&chart.getName().equals(name)){
				return chart.getChart();	
			}
		}
		return null;
	}
	
	/**
	 * @return Namen aller Aktien
	 */
	public ObservableList<String> getChartNamesList() {
		return observableChartNames;
	}

	/**
	 * @param Namen aller Aktien
	 */
	public void setChartNamesList(ObservableList<String> chartNamesList) {
		this.observableChartNames = chartNamesList;
	}
	
	/**
	 * Generiert einge Testdaten zu Debugzwecken.
	 * 
	 * @see #createDummyChartVO()
	 */
	public void initTestData(){
		
		Series<String, Number> testSeries;
		ArrayList<NewsVO> testFeeds;
		
		testSeries = new XYChart.Series<String, Number>();
		testSeries.setName("Microsoft");
		
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(1*60000)), 22));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(2*60000)), 13));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(3*60000)), 16));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(4*60000)), 23));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(5*60000)), 35));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(6*60000)), 33));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(7*60000)), 40));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(8*60000)), 44));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(9*60000)), 35));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(10*60000)), 30));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(11*60000)), 28));
		testSeries.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(12*60000)), 30));
		
		testFeeds= new ArrayList<NewsVO>();
		testFeeds.add(new NewsVO("Flying away, amaizing machines!","description","http://www.tagesschau.de/inland/eurohawk152.html", new Date(System.currentTimeMillis())));
		testFeeds.add(new NewsVO("EU findet Kompromiss zu CO2-Vorgaben für Autos","description","http://www.finanzen.net/nachricht/aktien/EU-findet-Kompromiss-zu-CO2-Vorgaben-fuer-Autos-2507406", new Date(System.currentTimeMillis())));
		testFeeds.add(new NewsVO("Dictator at work!","description","http://www.tagesschau.de/ausland/eu-erdogan100.html", new Date(System.currentTimeMillis())));

		ChartVO chartOne = new ChartVO(1, "Mircosoft");
		chartOne.setChart(testSeries);
		chartOne.setNewsFeeds(testFeeds);
		
		Series<String, Number> testSeries2;
		ArrayList<NewsVO> testFeeds2;
		
		testFeeds2= new ArrayList<NewsVO>();
		testFeeds2.add(new NewsVO("Check thisss out!","description","http://www.tagesschau.de/inland/geheimdienste110.html", new Date(System.currentTimeMillis())));
		testFeeds2.add(new NewsVO("USA-gehen-ebenfalls-moeglicher-Oelpreis-Manipulation-nach","discription","http://www.finanzen.net/nachricht/aktien/Kreise-USA-gehen-ebenfalls-moeglicher-Oelpreis-Manipulation-nach-2507397", new Date(System.currentTimeMillis())));
		
		testSeries2 = new XYChart.Series<String, Number>();
		testSeries2.setName("Samsung");
		
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(1*60000)), 55));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(2*60000)), 45));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(3*60000)), 34));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(4*60000)), 33));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(5*60000)), 35));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(6*60000)), 36));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(7*60000)), 40));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(8*60000)), 42));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(9*60000)), 35));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(10*60000)), 38));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(11*60000)), 43));
		testSeries2.getData().add(new XYChart.Data(millisToHHMM(System.currentTimeMillis()+(12*60000)), 46));
		
		ChartVO chartTwo = new ChartVO(2, "Samsung");
		chartTwo.setChart(testSeries2);
		chartTwo.setNewsFeeds(testFeeds2);
		
		
		this.availableChartVO.add(chartOne);
		this.availableChartVO.add(chartTwo);
		
		this.updateChartNames();
	}
	
	/**
	 * Wandelt Zeit von Millisekunden in einen hh:mm-formatierten String um.
	 * @param Millisekunden
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
}
