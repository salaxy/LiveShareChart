package de.fhb.trendsys.lsc.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

//TODO Refractoring to NewsContentPane
public class Browser extends Region {

	private double myHeight = 300;
	private double myWidth = 300;

	final WebView browser = new WebView();
	public final WebEngine webEngine = browser.getEngine();

	public Browser() {
		webEngine.loadContent(getHtml("<h1>News-Content-Pane</h1>"));
		getChildren().add(browser);
	}

	public Browser(double height, double width) {
		this();
		myHeight = height;
		myWidth = width;
	}

	public WebView getWebView() {
		return browser;
	}

	public WebEngine getWebEngine() {
		return webEngine;
	}

	@Override
	protected void layoutChildren() {
		layoutInArea(browser, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER,
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

	public void loadanotherUrl(String url) {
		browser.getEngine().load(url);
	}
	
	private String getHtml(String content) {
		return "<html><body>" + "<div id=\"mydiv\">" + content + "</div>"
				+ "</body></html>";
	}
	
}