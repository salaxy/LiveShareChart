package de.fhb.trendsys.lsc.model;

import java.util.Date;

/**
 * Diese Klasse repräsentiert eine News aus einem Newsfeed.
 * 
 * @author Frank
 *
 */
public class NewsVO {
	private String title;
	private String description;
	private String url;
	private Date time;
	
	/**
	 * Erstellt ein News-Objekt mit den angegebenen Attributen.
	 * 
	 * @param title Titel der News
	 * @param description Kurzbeschreibung des News-Inhaltes
	 * @param url Link zur vollständigen News
	 * @param time Zeit, an der die News erschienen ist
	 */
	public NewsVO(String title, String description, String url, Date time) {
		setTitle(title);
		setDescription(description);
		setUrl(url);
		setTime(time);
	}

	/**
	 * Titel der News
	 * 
	 * @return Titel
	 * @see NewsVO#setTitle(String)
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Titel der News
	 * Ist er null oder leer, wird er auf "News" gesetzt.
	 * 
	 * @param title neuer Titel der News
	 * @see NewsVO#getTitle()
	 */
	public void setTitle(String title) {
		if (title == null)
			this.title = "News";
		else
			if (title.isEmpty())
				this.title = "News";
			else
				this.title = title;
	}

	/**
	 * Kurzbeschreibung ("snippet") der News
	 * 
	 * @return Kurzbeschreibung
	 * @see NewsVO#setDescription(String)
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Kurzbeschreibung ("snippet") der News
	 * Ist sie null oder leer, wird sie auf "Klicken, um die News zu lesen" gesetzt.
	 * 
	 * @param neue Kurzbeschreibung
	 * @see NewsVO#getDescription()
	 */
	public void setDescription(String description) {
		if (description == null)
			this.description = "Klicken, um die News zu lesen";
		else
			if (description.isEmpty())
				this.description = "Klick, um die News zu lesen";
			else
				this.description = description;
	}

	/**
	 * Url, die auf die vollständige News zeigt.
	 * Kann remote oder lokal liegen.
	 * 
	 * @return Url
	 * @see NewsVO#setUrl(String)
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Url, die auf die vollständige News zeigt.
	 * Kann remote oder lokal liegen.
	 * Darf null sein.
	 * 
	 * @param url the url to set
	 * @see NewsVO#getUrl()
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Zeitpunkt, zu dem die News (im Feed) erschien.
	 * 
	 * @return Zeitpunkt
	 * @see NewsVO#setTime(Date)
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Zeitpunkt, zu dem die News (im Feed) erschien.
	 * Wird sie auf null gesetzt, wird der 1.1.1970 00:00 Uhr als Zeitpunkt eingetragen. 
	 * 
	 * @param neuer Zeitpunkt
	 * @see NewsVO#getTime()
	 */
	public void setTime(Date time) {
		if (time == null)
			this.time = new Date(0L);
		else
			this.time = time;
	}	
}
