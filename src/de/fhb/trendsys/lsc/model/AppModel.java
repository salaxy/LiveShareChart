package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import java.util.Date;

/**
 * Das Model beinhaltet immer die aktuellen Chart-Daten.
 * 
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * @author Frank Mertens
 */
@Deprecated
public class AppModel {

	private Series<String, Number> currentDataSeries;	
	private List<Series<String, Number>> availableStockData = new ArrayList<Series<String, Number>>();	
	private String actualStock="AktieXY";
	
	//TODO FRank wir brauchen heir Datenpaare, da hgaben wir doch schon nen Datenformat oder
	// der Titel + Link beinhaltet
	private List<NewsVO> newsFeeds = new ArrayList<NewsVO>();	
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

	public List<NewsVO> getActualNewsFeeds() {
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
		
		this.newsFeeds= new ArrayList<NewsVO>();
		newsFeeds.add(new NewsVO("Flying away, amaizing machines!","description","http://www.tagesschau.de/inland/eurohawk152.html", new Date(System.currentTimeMillis())));
		newsFeeds.add(new NewsVO("Water, water annnd water again!","description","http://www.tagesschau.de/inland/hochwasser1142.html", new Date(System.currentTimeMillis())));
		newsFeeds.add(new NewsVO("Dictator at work!","description","http://www.tagesschau.de/ausland/eu-erdogan100.html", new Date(System.currentTimeMillis())));
		newsFeeds.add(new NewsVO("Check thisss out!","description","http://www.tagesschau.de/inland/geheimdienste110.html", new Date(System.currentTimeMillis())));
		newsFeeds.add(new NewsVO("Whats going on in Berlin, master of masters is coming!","discription","http://www.rbb-online.de/nachrichten/politik/2013_06/obama_besuch_sorgt_fuer_ausnahmezustand.html", new Date(System.currentTimeMillis())));
		
	}
	
}