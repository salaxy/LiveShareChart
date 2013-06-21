package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;

import javafx.collections.ObservableList;

public class NewAdvancedAndFancyAppModel {
	private ArrayList<ChartVO> chartList;
	private ChartVO selectedChart;
	private ObservableList<String> chartNamesList;
	
	public NewAdvancedAndFancyAppModel() {
		chartList = new ArrayList<ChartVO>();
	}
	
	/**
	 * @return Liste aller Aktiendaten
	 */
	public ArrayList<ChartVO> getChartList() {
		return chartList;
	}
	/**
	 * @param Liste aller Aktiendaten
	 */
	public void setChartList(ArrayList<ChartVO> chartList) {
		this.chartList = chartList;
	}
	/**
	 * @return ausgew�hlte Aktie
	 */
	public ChartVO getSelectedChart() {
		return selectedChart;
	}
	/**
	 * @param ausgew�hlte Aktie
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
	 * Gibt eine Aktie mit der passenden Datenbank-ID zur�ck.
	 * Wird keine gefunden, wird null zur�ckgegeben.
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
	 * Gibt eine Aktie mit dem passenden Name zur�ck.
	 * Wird keine gefunden, wird null zur�ckgegeben.
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
	
	public ArrayList<String> getChartNames() {
		ArrayList<String> returnList = new ArrayList<String>();
		
		for (ChartVO chart : chartList) {
			returnList.add(chart.getName());
		}
		
		return returnList;
	}
}
