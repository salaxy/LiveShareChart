package de.fhb.trendsys.lsc.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * GUI-Klasse zur Einbettung von Web Content
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class NewsContentPane extends Region {

	private double myHeight = 300;
	private double myWidth = 300;

	final private WebView newsContentPane = new WebView();
	final private WebEngine webEngine = newsContentPane.getEngine();

	/**
	 * Konstruktor mit default HTML-Content
	 */
	public NewsContentPane() {
		webEngine.loadContent(getHtml("<h1>News-Content-Pane</h1>"));
		getChildren().add(newsContentPane);
	}

	/**
	 * Konstruktor mit default HTML-Content
	 * und Größen-Angabe
	 * @param height
	 * @param width
	 */
	public NewsContentPane(double height, double width) {
		this();
		myHeight = height;
		myWidth = width;
	}

	/**
	 * gibt die WebView zurück
	 * @return WebView
	 */
	public WebView getWebView() {
		return newsContentPane;
	}

	/**
	 * gibt die WebEngine zurück
	 * @return WebEngine
	 */
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

	/**
	 * setzen der Größe des Elements
	 * @param height
	 * @param width
	 */
	public void setSize(double height, double width) {
		myHeight = height;
		myWidth = width;
	}

	/**
	 * laden einer URL
	 * @param url
	 */
	public void loadAnotherUrl(String url) {
		newsContentPane.getEngine().load(url);
	}
	
	private String getHtml(String content) {
		return "<html><body>" + "<div id=\"mydiv\">" + content + "</div>"
				+ "</body></html>";
	}
	
}