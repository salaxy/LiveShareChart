package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;

public class NewAdvancedAndFancyAppModel {
	private ArrayList<ChartVO> chartList;
	private ChartVO selectedChart;
	
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
}
