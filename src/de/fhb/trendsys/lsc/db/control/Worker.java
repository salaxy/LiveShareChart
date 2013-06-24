package de.fhb.trendsys.lsc.db.control;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javafx.scene.chart.XYChart;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import de.fhb.trendsys.amazonfunctions.dynamodb.DynamoDBHandler;
import de.fhb.trendsys.lsc.model.ChartVO;
import de.fhb.trendsys.lsc.model.NewAdvancedAndFancyAppModel;
import de.fhb.trendsys.lsc.model.NewsVO;

/**
 * Die Klasse stellt den Hintergrund-Thread dar, der periodisch
 * neue Aktiendaten aus der Amazon DynamoDB lädt.
 * 
 * @author Frank Mertens
 *
 */
public class Worker extends Thread {
	private static Worker instance;
	private static final long FAST_UPDATE_DELAY = 60000; // 1 Min.
	private static final long SLOW_UPDATE_RATE = FAST_UPDATE_DELAY * 10;
	
	private DynamoDBHandler ddbClient;
	private NewAdvancedAndFancyAppModel model;
	private Map<Integer, Long> stockUpdateQueue;
	private int priorityStock = 0;
	
	/**
	 * Gibt die Instanz des Thread zurück. Existiert sie nicht,
	 * wird der Thread erzeugt und gestartet und die neue Instanz zurückgegeben.
	 * @param model Referenz auf das Appmodel, um dort Daten aktualisieren zu können.
	 * @return Instanz des Threads
	 */
	public static Worker getInstance(NewAdvancedAndFancyAppModel model) {
		if (Worker.instance == null || Worker.instance.isAlive() == false || Worker.interrupted() == true) {
			Worker.instance = new Worker(model);
			Worker.instance.start();
		}
		
		return Worker.instance;
	}
	
	/**
	 * Konstruktor des Threads
	 * Bereit alles für den Thread vor und initialisiert auch eine Verbindung zur Amazon Cloud.
	 * 
	 * @param model
	 * @see Worker#getInstance(AppModel)
	 */
	private Worker(NewAdvancedAndFancyAppModel model) {
		this.model = model;
		this.stockUpdateQueue = new HashMap<Integer, Long>();
		this.ddbClient = new DynamoDBHandler(Regions.EU_WEST_1, "stockdata");
	}
	
	public void run() {
		this.stockUpdateQueue = this.getStockIds();
		
		while (!this.isInterrupted()) {
			this.processQueue();
			
			synchronized (this) {
				try {
					this.wait(Worker.FAST_UPDATE_DELAY);
				} catch (InterruptedException e) {
					// Interrupt 
				}
			}
		}
		
		
	}
	
	/**
	 * Gibt alle Aktien-IDs in der Datenbank zurück. Setzt auch den Zeitpunkt der letzten Aktualisierung auf von vor 24h.
	 * Die Methode bereitet alles für die Update Queue vor.
	 * 
	 * @return
	 * @see #processQueue()
	 * @see #updateStockChartData(List)
	 * @see #updateStock(int, long)
	 */
	private Map<Integer, Long> getStockIds() {
		Map<Integer, Long> returnMap = new HashMap<Integer, Long>();
		Map<String, AttributeValue> resultMap = (this.ddbClient.getAllItems(0, 0L)).get(0);
		Set<Entry<String, AttributeValue>> resultSet = resultMap.entrySet();
		int stockId;
		long lastUpdateTime;
		
		for (Entry<String, AttributeValue> item : resultSet) {
			if (item.getKey().startsWith("stockid")) {
				stockId = Integer.parseInt(item.getValue().getS());
				lastUpdateTime = System.currentTimeMillis() - 86400000L; // vor einem Tag
				returnMap.put(stockId, lastUpdateTime);
			}
		}
		
		return returnMap;
	}
	
	/**
	 * Fragt alle Tupel einer Aktie ab, die seit dem angegeben Zeitstempel in die Datenbank geschrieben wurden.
	 * Es wird auch das {@link AppModel} aktualisiert.
	 * 
	 * @param id Primärschlüssel der Aktie in der DB
	 * @param timestamp Ältester Zeitpunkt der Daten
	 * @see #processQueue()
	 * @see #updateStockChartData(List)
	 * 
	 */
	private void updateStock(int id, long timestamp) {
		List<Map<String, AttributeValue>> updatedStockDataList = this.ddbClient.getAllItems(id, timestamp);
		this.updateStockChartData(updatedStockDataList);
	}
	
	/**
	 * Verarbeitet die Ergebnisse der DB-Abfrage und aktualisiert damit das Model.
	 * 
	 * @param updatedStockDataList Ergebnis der DB-Abfrage
	 * @see #processQueue()
	 * @see #updateStock(int, long)
	 * 
	 */
	private void updateStockChartData(List<Map<String, AttributeValue>> updatedStockDataList) {
		if (this.model != null) {
			for (Map<String, AttributeValue> stockMap : updatedStockDataList) {
				Set<Entry<String, AttributeValue>>  stockSet = stockMap.entrySet();
				
				int id = 0;
				String timeStamp = null;
				double stockValue = 0d;
				String newsTitle = null;
				String newsDescription = null;
				String newsUrl = null;
				
				for (Entry<String, AttributeValue> item : stockSet) {
					System.out.println("Entry: " + item);
					String type = item.getKey();
					
					if (type.equals("id")) id = Integer.parseInt(item.getValue().getN());
					if (type.equals("timestamp")) timeStamp = item.getValue().getS();
					if (type.equals("stock")) stockValue = Double.parseDouble(item.getValue().getS());
					if (type.equals("newstitle")) newsTitle = item.getValue().getS();
					if (type.equals("newsdescription")) newsDescription = item.getValue().getS();
					if (type.equals("newsurl")) newsUrl = item.getValue().getS();
				}
				
				if (id > 0 && !timeStamp.isEmpty()) {
					ChartVO chart = model.returnChartById(id);
					System.out.println("chart id:" + id);
					if (chart != null) {
						chart.getChart().getData().add(new XYChart.Data<String, Number>(timeStamp, stockValue));
						System.out.println("timestamp: " + timeStamp + "  stock value: " + stockValue);
					}
					else {
						chart = new ChartVO(id, "no name");
						chart.getChart().getData().add(new XYChart.Data<String, Number>(timeStamp, stockValue));
						System.out.println("Adding chart " + chart.getId());
						model.addToChartList(chart);
						System.out.println("new chart item: " + chart.getChart());
					}
				}
				
				if (newsTitle != null && newsDescription != null && newsUrl != null) {
					NewsVO news = new NewsVO(newsTitle, newsDescription, newsUrl, new Date(Long.parseLong(timeStamp)));
					ChartVO chart = model.returnChartById(id);
					chart.getNewsFeeds().add(news);
					System.out.println("new news item: " + news);
				}
			}
		}
	}
	
	/**
	 * Aktualisiert entsprechend der Queue die Aktiendaten.
	 * 
	 * @see #updateStock(int, long)
	 */
	private void processQueue() {
		int id;
		long nextUpdateTime;
		Set<Entry<Integer, Long>> stockSet = stockUpdateQueue.entrySet();
		long currentTime = System.currentTimeMillis();
		
		for (Entry<Integer, Long> stock : stockSet) {
			id = stock.getKey();
			nextUpdateTime = stock.getValue();
			
			if (nextUpdateTime < currentTime) {
				this.updateStock(stock.getKey(), nextUpdateTime);
				
				nextUpdateTime = currentTime + ((id == this.priorityStock) ? Worker.FAST_UPDATE_DELAY : Worker.SLOW_UPDATE_RATE);
				stock.setValue(nextUpdateTime);
			}
		}
	}
	
	/**
	 * Legt die Aktie fest, die jede Minute aktualisiert werden soll.
	 * Alle anderen Aktien werden alle 10 Minuten aktualisiert.
	 * 
	 * @param id Aktie, die öfters aktualisiert werden soll
	 */
	public void setPriorityStock(int id) {
		if (id > 0)
			this.priorityStock = id;
		
		int stockId;
		Set<Entry<Integer, Long>> stockSet = stockUpdateQueue.entrySet();
		
		for (Entry<Integer, Long> stock : stockSet) {
			stockId = stock.getKey();
			
			if (stockId == this.priorityStock) {
				stock.setValue(System.currentTimeMillis());
			}
		}
	}
}
