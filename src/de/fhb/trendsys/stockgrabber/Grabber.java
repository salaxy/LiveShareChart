package de.fhb.trendsys.stockgrabber;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javaxt.http.Request;
import javaxt.rss.Feed;
import javaxt.rss.Item;
import javaxt.rss.Parser;

import org.w3c.dom.Document;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import de.fhb.trendsys.amazonfunctions.dynamodb.DynamoDBHandler;

/**
 * Diese Klasse erstellt mehrer Thread, die kontinuierlich Daten in die Datenbank speichern.
 
 * @author Frank
 *
 */
public class Grabber extends Thread {
	public static final long delayChartUpdate = 60000L; // 1 Min.
	private DynamoDBHandler ddbClient;
	private int id;
	private URL rssURL;
	private String stockUrl;
	
	public static void main(String[] args) throws MalformedURLException {
		// Sony
		new Grabber(new DynamoDBHandler(Regions.EU_WEST_1, "stockdata"), 1, new URL("http://www.sony.de/rss/de_DE/All.rss"), "http://www.sony.net").start();
		
		// Nintendo
		new Grabber(new DynamoDBHandler(Regions.EU_WEST_1, "stockdata"), 2, new URL("http://www.nintendolife.com/feeds/news"), "http://www.nintendo.com").start();

		// Software AG
		new Grabber(new DynamoDBHandler(Regions.EU_WEST_1, "stockdata"), 3, new URL("http://www.softwareag.com/blog/reality_check/index.php/feed/"), "http://www.softwareag.com").start();
	}
	
	/**
	 * Erzeugt einen thread, der kontinuierlich in der Datenbank Kursdaten zu der angegeben Aktie generiert.
	 * Weiterhin wird bei jedem Update überprüft, ob neue RSS-News vorliegen. Ist der Link zu einer RSS-News
	 * nicht verfügbar, wird ein Link zur Hauptseite der Firma stattdessen eingefügt.
	 * @param DynamoDBHandler
	 * @param Aktien-ID
	 * @param RSS-URL
	 * @param Link zur Hauptseite
	 * @throws MalformedURLException
	 */
	public Grabber(DynamoDBHandler ddbClient, int id, URL rssURL, String stockUrl) throws MalformedURLException {
		this.ddbClient = ddbClient;
		this.id = id;
		this.rssURL = rssURL;
		this.stockUrl = stockUrl;
	}
	
	public void run() {
		String lastNewsTitle = "";
		double newValue = 50d;
		double oldValue = 50d;
		
		while (true) {
			Long currentTime = System.currentTimeMillis();
			newValue = (oldValue > 60d) ? oldValue - Math.random() * 5d : oldValue + Math.random() * 5d;
			
			// DB-Item generieren
			Map<String, AttributeValue> stockDataItem = new HashMap<String, AttributeValue>();
			stockDataItem.put("id", new AttributeValue().withN(Long.toString(id)));
			stockDataItem.put("timestamp", new AttributeValue().withS(Long.toString(currentTime)));
			stockDataItem.put("stock", new AttributeValue().withS(Double.toString(newValue)));			
			oldValue = newValue;
			
			// News-Feed abholen			
			Document feedXml = new Request(rssURL).getResponse().getXML();
			Feed[] feeds = new Parser(feedXml).getFeeds();
			
			if (!feeds[0].getItems()[0].getTitle().equals(lastNewsTitle)) {
				Item item = feeds[0].getItems()[0];
				String feedTitle = item.getTitle();
				String feedDescription = item.getDescription();
				String feedUrl = item.getLink().toExternalForm();
				
				if (!feedTitle.isEmpty())
					stockDataItem.put("newstitle", new AttributeValue().withS(feedTitle));
				else
					stockDataItem.put("newstitle", new AttributeValue().withS("Neue Nachrichten"));
					
				if (!feedDescription.isEmpty())
					stockDataItem.put("newsdescription", new AttributeValue().withS(feedDescription));
				else
					stockDataItem.put("newsdescription", new AttributeValue().withS("keine Beschreibung"));
				
				if (!feedUrl.isEmpty())
					stockDataItem.put("newsurl", new AttributeValue().withS(feedUrl));
				else
					stockDataItem.put("newsurl", new AttributeValue().withS(stockUrl));
				
				lastNewsTitle = feedTitle;
			}

			ddbClient.addItem(id, stockDataItem);
			
			System.out.println("Item: " + stockDataItem.toString());
			System.out.println("Last news title: " + lastNewsTitle);
			
			try {
				Thread.sleep(delayChartUpdate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
