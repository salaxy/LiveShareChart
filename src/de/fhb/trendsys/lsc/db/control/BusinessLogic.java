package de.fhb.trendsys.lsc.db.control;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.scene.chart.XYChart;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import de.fhb.trendsys.amazonfunctions.dynamodb.DynamoDBHandler;
import de.fhb.trendsys.lsc.model.AppModel;


/**
 * Business-Logik der Applikation
 * Hier werden die Daten aus der DB gezogen,
 * verarbeitet und ins Model geschrieben.
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * @author Frank Mertens
 */
public class BusinessLogic {

	private AppModel model;

	public BusinessLogic(AppModel model) {
		this.model=model;
	}
	
	public void refresh(int id){
		Thread worker = new WorkerThread(id);
		worker.run();
	}
	
	private class WorkerThread extends Thread {
		private DynamoDBHandler ddbClient;
		private int id;
		
		public WorkerThread(int id) {
			ddbClient = new DynamoDBHandler(Regions.EU_WEST_1, "stockdata");
			this.id = id;
		}
		
		public void run() {
			List<Map<String, AttributeValue>> itemList = ddbClient.getAllItems(id);
			for (Map<String, AttributeValue> itemMap : itemList) {
				Set<Entry<String, AttributeValue>>  itemSet = itemMap.entrySet();
				
				String timeStamp = null;
				Double stockValue = 0d;
				
				for (Entry<String, AttributeValue> item : itemSet) {
					if (timeStamp == null)
						timeStamp = item.getValue().getS();
					else
						stockValue = Double.parseDouble(item.getValue().getS());
				}
				//TODO hier d�rfen wirklich nur neue Daten hinzu kommen,
				//denn deshalb entsteht die Verbindung von letzten zum neuen Punkt
				//also TEST was war der letzte wert, ab da an alle neuen Werte holen
				//und in den aktuellen Datensatz schreiben
				if(model.getActualDataSeries()!=null)
				model.getActualDataSeries().getData().add(new XYChart.Data<String, Number>(timeStamp, stockValue));
			}
				
		}
	}

}
