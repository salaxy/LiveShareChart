package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * Das Model beinhaltet immer die aktuellen Chart-Daten.
 * 
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * @author Frank Mertens
 */
public class AppModel {

	private Series<String, Number> currentDataSeries;	
	private List<Series<String, Number>> availableStockData = new ArrayList<Series<String, Number>>();	
	private String actualStock="AktieXY";
	
	//TODO FRank wir brauchen heir Datenpaare, da hgaben wir doch schon nen Datenformat oder
	// der Titel + Link beinhaltet
	private List<FeedVO> newsFeeds = new ArrayList<FeedVO>();	
	private String openedNewsURL = "http://www.finanzen.net/nachricht/aktien/China-und-Costa-Rica-unterzeichnen-Wirtschaftsabkommen-2469118";
	
	public AppModel() {
		initTestData();
	}
	
	public XYChart.Series<String, Number> getDataSeries() {
		return currentDataSeries;
	}
	
	/**
	 * this method is for changing the actual dataseries, so if
	 * the dataseries with the given name is found, so it the actual series will change
	 * 
	 * @param name - given name for series
	 * @return was dataseries found  or not
	 */
	public boolean changeActualDataSeries(String name){
		
		boolean found=false;

		for( Series<String, Number> dataSeries : availableStockData ){
			if(dataSeries.getName().equals(name)){
				this.currentDataSeries= dataSeries;
				found=true;
			}
		}
		
		return found;
	}

	public String getActualStock() {
		return actualStock;
	}

	public void setActualStock(String actualStock) {
		this.actualStock = actualStock;
	}

	public List<FeedVO> getActualNewsFeeds() {
		return newsFeeds;
	}

	public String getActualOpenedNewsURL() {
		return openedNewsURL;
	}

	public void setActualOpenedNewsURL(String actualOpenedNewsURL) {
		this.openedNewsURL = actualOpenedNewsURL;
	}

	
	public List<String> getStockNames(){
		
		List<String> nameList = new ArrayList<String>();
		
		for(XYChart.Series<String, Number> series : availableStockData){
			nameList.add(series.getName());
		}
		
		return nameList;
	}

	//Bitte als TestDaten drin lassen
	private void initTestData() {

		//Test-DatenSerie 1
		Series<String, Number> numberSeries = new XYChart.Series<String, Number>();
		numberSeries.setName("TestAktie-Numbers");
		this.availableStockData.add(numberSeries);
		
		numberSeries.getData().add(new XYChart.Data("1", 22));
		numberSeries.getData().add(new XYChart.Data("2", 13));
		numberSeries.getData().add(new XYChart.Data("3", 16));
		numberSeries.getData().add(new XYChart.Data("4", 23));
		numberSeries.getData().add(new XYChart.Data("5", 35));
		numberSeries.getData().add(new XYChart.Data("6", 33));
		numberSeries.getData().add(new XYChart.Data("7", 40));
		numberSeries.getData().add(new XYChart.Data("8", 44));
		numberSeries.getData().add(new XYChart.Data("9", 35));
		numberSeries.getData().add(new XYChart.Data("10", 30));
		numberSeries.getData().add(new XYChart.Data("11", 28));
		numberSeries.getData().add(new XYChart.Data("12", 22));
		
		//Test-DatenSerie 2
		Series<String, Number> monthValueSeries = new XYChart.Series<String, Number>();;
		monthValueSeries.setName("TestAktie-MonthValue");
		this.availableStockData.add(monthValueSeries);
		
		monthValueSeries.getData().add(new XYChart.Data("Jan", 23));
		monthValueSeries.getData().add(new XYChart.Data("Feb", 14));
		monthValueSeries.getData().add(new XYChart.Data("Mar", 15));
		monthValueSeries.getData().add(new XYChart.Data("Apr", 24));
		monthValueSeries.getData().add(new XYChart.Data("May", 34));
		monthValueSeries.getData().add(new XYChart.Data("Jun", 36));
		monthValueSeries.getData().add(new XYChart.Data("Jul", 22));
		monthValueSeries.getData().add(new XYChart.Data("Aug", 45));
		monthValueSeries.getData().add(new XYChart.Data("Sep", 43));
		monthValueSeries.getData().add(new XYChart.Data("Oct", 17));
		monthValueSeries.getData().add(new XYChart.Data("Nov", 29));
		monthValueSeries.getData().add(new XYChart.Data("Dec", 25));
        
		
		//Test News-Ticker Daten
//        this.actualNewsFeeds = "Java FX 2.0 News "  + " ++++ "
//        +" tomorrow 85 °F and sunny :)" + " ++++ "
//		+  "DAX	8.241	-1,3% " + " ++++ "
//		+ " Dow	15.116	-1,4% " + " ++++ "
//		+ " Euro	1,3021	0,2%" + " ++++ "
//		+ " ESt50	2.738	-1,1% " + " ++++ "
//		+ " Nas	3.456	-1,0% " + " ++++ "
//		+ " Öl	100,1	0,0%"+ " ++++ " 
//		+ "TDax	949,7	-1,6%  " + " ++++ "
//		+ " Nikkei	13.262	-3,7%  	" + " ++++ "
//		+ " Gold	1.399	0,8%";
		
		this.newsFeeds= new ArrayList<FeedVO>();
		newsFeeds.add(new FeedVO("Flying away, amaizing machines!","http://www.tagesschau.de/inland/eurohawk152.html"));
		newsFeeds.add(new FeedVO("Water, water annnd water again!","http://www.tagesschau.de/inland/hochwasser1142.html"));
		newsFeeds.add(new FeedVO("Dictator at work!","http://www.tagesschau.de/ausland/eu-erdogan100.html"));
		newsFeeds.add(new FeedVO("Check thisss out!","http://www.tagesschau.de/inland/geheimdienste110.html"));
		newsFeeds.add(new FeedVO("Whats going on in Berlin, master of masters is coming!","http://www.rbb-online.de/nachrichten/politik/2013_06/obama_besuch_sorgt_fuer_ausnahmezustand.html"));
		
	}
	
}