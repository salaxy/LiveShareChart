package de.fhb.trendsys.lsc.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {

	private double myHeight = 300;
	private double myWidth = 300;

	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();

	public Browser() {
//		webEngine.load("http://www.oracle.com/products/index.html");
		webEngine.load("http://www.computerbase.de");
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
	
//    @SuppressWarnings("unused")
//	private Node createSpacer() {
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        return spacer;
//    }

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
	
	public void loadanotherUrl(String url){
		browser.getEngine().load(url);
	}
}