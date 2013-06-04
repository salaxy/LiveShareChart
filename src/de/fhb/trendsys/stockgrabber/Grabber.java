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

public class Grabber {
	public static final long delayChartUpdate = 60000L; // 1 Min.

	public static void main(String[] args) throws MalformedURLException {
		DynamoDBHandler ddbClient = new DynamoDBHandler(Regions.EU_WEST_1, "stockdata");
		
		String lastNewsTitle = "";
		long id = 1;
		URL rssUrl = new URL("http://www.sony.de/rss/de_DE/All.rss");
		String stockUrl = "http://www.sony.net";
		
		while (true) {
			Long currentTime = System.currentTimeMillis();
			
			// DB-Item generieren
			Map<String, AttributeValue> stockDataItem = new HashMap<String, AttributeValue>();
			stockDataItem.put("id", new AttributeValue().withN(Long.toString(id)));
			stockDataItem.put("timestamp", new AttributeValue().withS(Long.toString(currentTime)));
			stockDataItem.put("stock", new AttributeValue().withS(Double.toString(Math.random())));			
			
			// News-Feed abholen			
			Document feedXml = new Request(rssUrl).getResponse().getXML();
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

			ddbClient.addItem(1, stockDataItem);
			
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
