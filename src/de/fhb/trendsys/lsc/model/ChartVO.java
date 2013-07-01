package de.fhb.trendsys.lsc.model;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

/**
 * Ein Value Object, dass einen Aktienkurs und dazugehörige News umfasst.
 * 
 * @author Frank Mertens
 *
 */
public class ChartVO {
	private String name;
	private int id;
	private Series<String, Number> chart;
	private ArrayList<NewsVO> newsFeeds;
	
	/**
	 * Instanziiert ein VO, dass einen Aktienkurs und dazugehörige News umfasst.
	 * 
	 * @param Name der Aktie
	 */
	public ChartVO(int id, String name) {
		super();
		this.setId(id);
		this.name=name;
		this.setChart(new Series<String, Number>());
		this.getChart().setName(this.getName());
		this.setNewsFeeds(new ArrayList<NewsVO>());
	}
	
	/**
	 * @return Name der Aktie
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param Name der Aktie
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Datenbank-ID der Aktie
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @param Datenbank-ID der Aktie
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return eine Reihe von XY-Werten, die den Aktienkursverlauf anzeigen
	 */
	public Series<String, Number> getChart() {
		return chart;
	}
	
	/**
	 * @param neuer Aktienkursverlauf
	 */
	public void setChart(Series<String, Number> chart) {
		this.chart = chart;
	}
	
	/**
	 * @return Liste von News
	 */
	public ArrayList<NewsVO> getNewsFeeds() {
		return newsFeeds;
	}
	
	/**
	 * @param neue Liste von News
	 */
	public void setNewsFeeds(ArrayList<NewsVO> newsFeeds) {
		this.newsFeeds = newsFeeds;
	}

	/**
	 * Gibt die Änderung des Aktienskurs seit Mitternacht in Prozent aus.
	 * Positive Zahlen bedeuten, der Kurs ist gestiegen und vice versa.
	 * Die Prozentzahl ist relativ: Ein Steigerung auf 134% wird als 34%
	 * zurückgegeben.
	 * 
	 * @return Prozent
	 */
	public double getChangeInPercents() {
		double percents = 0d;
		ObservableList<Data<String, Number>> dataList = chart.getData();
		if (dataList != null && dataList.size() > 0) {
			double oldestValue = dataList.get(0).getYValue().doubleValue();
			double latestValue = dataList.get(dataList.size() - 1).getYValue().doubleValue();
			
			if (oldestValue > 0d && latestValue > 0d)
				percents = (oldestValue - latestValue) / oldestValue;
			
		}

		return percents;
	}
}
