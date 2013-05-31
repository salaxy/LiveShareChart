package de.fhb.trendsys.stockgrabber;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import de.fhb.trendsys.amazonfunctions.dynamodb.DynamoDBHandler;

public class Grabber {

	public static void main(String[] args) {
		DynamoDBHandler ddbClient = new DynamoDBHandler(Regions.EU_WEST_1, "stockdata");
		
		while (true) {
			Map<String, AttributeValue> stockDataItem = new HashMap<String, AttributeValue>();
			stockDataItem.put("id", new AttributeValue().withN("1"));
			stockDataItem.put("timestamp", new AttributeValue().withS(Long.toString(System.currentTimeMillis())));
			stockDataItem.put("stock", new AttributeValue().withS(Double.toString(Math.random())));			
			
			if (System.currentTimeMillis() % 10 == 0) {
				stockDataItem.put("stock", new AttributeValue().withS(Double.toString(Math.random())));
				
				int numberOfNews = (int) (Math.random() * 2d);
				for (int i = 0; i < numberOfNews; i++) {
					stockDataItem.put("news" + i, new AttributeValue().withS("Nachricht " + i));
				}
			}
				
			ddbClient.addItem(1, stockDataItem);
			
			System.out.println("Item: " + stockDataItem.toString());
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
