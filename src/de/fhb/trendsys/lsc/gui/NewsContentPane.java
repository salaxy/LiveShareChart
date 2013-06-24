package de.fhb.trendsys.lsc.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * GUI-Klasse zur einbettung eines browser-Fensters
 * 
 */
public class NewsContentPane extends Region {

	private double myHeight = 300;
	private double myWidth = 300;

	final WebView newsContentPane = new WebView();
	final WebEngine webEngine = newsContentPane.getEngine();

	public NewsContentPane() {
		webEngine.loadContent(getHtml("<h1>News-Content-Pane</h1>"));
		getChildren().add(newsContentPane);
	}

	public NewsContentPane(double height, double width) {
		this();
		myHeight = height;
		myWidth = width;
	}

	public WebView getWebView() {
		return newsContentPane;
	}

	public WebEngine getWebEngine() {
		return webEngine;
	}

	@Override
	protected void layoutChildren() {
		layoutInArea(newsContentPane, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER,
				VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return myWidth;
	}

	@Override
	protected double computePrefHeight(double width) {
		return myHeight;
	}

	public void setSize(double height, double width) {
		myHeight = height;
		myWidth = width;
	}

	public void loadAnotherUrl(String url) {
		newsContentPane.getEngine().load(url);
	}
	
	private String getHtml(String content) {
		return "<html><body>" + "<div id=\"mydiv\">" + content + "</div>"
				+ "</body></html>";
	}
	
}