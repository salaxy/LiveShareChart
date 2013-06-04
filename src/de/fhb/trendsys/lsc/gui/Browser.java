package de.fhb.trendsys.lsc.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {

	private double myHeight = 300;
	private double myWidth = 300;

	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();

	public Browser() {
		webEngine.load("http://www.oracle.com/products/index.html");
		getChildren().add(browser);
	}

	public Browser(double height, double width) {
		this();
		myHeight = height;
		myWidth = width;
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
}