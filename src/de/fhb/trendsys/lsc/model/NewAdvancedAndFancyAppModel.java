package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NewAdvancedAndFancyAppModel {
	private ArrayList<ChartVO> chartList;
	private ChartVO selectedChart;
	private ObservableList<String> chartNamesList;
	
	public NewAdvancedAndFancyAppModel() {
		chartList = new ArrayList<ChartVO>();
		chartNamesList = FXCollections.observableArrayList();
	}
	
	/**
	 * @return Liste aller Aktiendaten
	 */
	private ArrayList<ChartVO> getChartList() {
		return chartList;
	}
	
	public void addToChartList(final ChartVO chart) {
		this.chartList.add(chart);
		Platform.runLater(new Runnable() {
										  @Override
										  public void run() {
											  chartNamesList.add(chart.getName());
											  System.out.println("Run, Run, Run!");
										  }
										 });
		
	}
	
	/**
	 * @param Liste aller Aktiendaten
	 */
	public void setChartList(ArrayList<ChartVO> chartList) {
		this.chartList = chartList;
	}
	/**
	 * @return ausgewählte Aktie
	 */
	public ChartVO getSelectedChart() {
		return selectedChart;
	}
	/**
	 * @param ausgewählte Aktie
	 */
	public void setSelectedChart(ChartVO selectedChart) {
		this.selectedChart = selectedChart;
	}
	
	/**
	 * @return Liste der neusten News jeder Aktie
	 */
	public ArrayList<NewsVO> getTickerNews() {
		ArrayList<NewsVO> returnList = new ArrayList<NewsVO>();
		
		for (ChartVO chart : chartList) {
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
		for (ChartVO chart : chartList) {
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
		for (ChartVO chart : chartList) {
			if (chart.getName().equals(name))
				return chart;
		}
		
		return null;
	}
	
	/**
	 * @return Namen aller Aktien
	 */
	public ObservableList<String> getChartNamesList() {
		return chartNamesList;
	}

	/**
	 * @param Namen aller Aktien
	 */
	public void setChartNamesList(ObservableList<String> chartNamesList) {
		this.chartNamesList = chartNamesList;
	}
}
