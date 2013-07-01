package de.fhb.trendsys.lsc.db.control;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javafx.scene.chart.XYChart;
import javafx.application.Platform;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import de.fhb.trendsys.amazonfunctions.dynamodb.DynamoDBHandler;
import de.fhb.trendsys.lsc.model.ChartVO;
import de.fhb.trendsys.lsc.model.AppModel;
import de.fhb.trendsys.lsc.model.NewsVO;

/**
 * Die Klasse stellt den Hintergrund-Thread dar, der periodisch
 * neue Aktiendaten aus der Amazon DynamoDB l�dt.
 * 
 * @author Frank Mertens
 *
 */
public class Worker extends Thread {
	private static Worker instance;
	private static final long FAST_UPDATE_DELAY = 60000; // 1 Min.
	private static final long SLOW_UPDATE_RATE = FAST_UPDATE_DELAY * 10L;
	
	private DynamoDBHandler ddbClient;
	private AppModel model;
	private Map<Integer, Long> stockUpdateQueue;
	private int priorityStock = 0;
	
	/**
	 * Gibt die Instanz des Thread zur�ck. Existiert sie nicht,
	 * wird der Thread erzeugt und gestartet und die neue Instanz zur�ckgegeben.
	 * @param model Referenz auf das Appmodel, um dort Daten aktualisieren zu k�nnen.
	 * @return Instanz des Threads
	 */
	public static Worker getInstance(AppModel model) {
		if (Worker.instance == null || Worker.instance.isAlive() == false || Worker.interrupted() == true) {
			Worker.instance = new Worker(model);
			Worker.instance.start();
		}
		
		return Worker.instance;
	}
	
	/**
	 * Konstruktor des Threads
	 * Bereit alles f�r den Thread vor und initialisiert auch eine Verbindung zur Amazon Cloud.
	 * 
	 * @param model
	 * @see Worker#getInstance(AppModel)
	 */
	private Worker(AppModel model) {
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
	 * Gibt alle Aktien-IDs in der Datenbank zur�ck. Setzt auch den Zeitpunkt der letzten Aktualisierung auf von vor 24h.
	 * Die Methode bereitet alles f�r die Update Queue vor.
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
	
	private String getStockName(int id) {
		List<Map<String, AttributeValue>> resultList = this.ddbClient.getAllItems(id, 0L, 0L);
		String returnString = null;
		
		for (Map<String, AttributeValue> resultMap : resultList) {
			Set<Entry<String, AttributeValue>> resultSet = resultMap.entrySet();
			
			for (Entry<String, AttributeValue> item : resultSet) {
				if (item.getKey().equals("stockname")) {
					returnString = item.getValue().getS();
				}
			}
		}
		
		return returnString;
	}
	
	/**
	 * Fragt alle Tupel einer Aktie ab, die seit dem angegeben Zeitstempel in die Datenbank geschrieben wurden.
	 * Es wird auch das {@link AppModel} aktualisiert.
	 * 
	 * @param id Prim�rschl�ssel der Aktie in der DB
	 * @param timestamp �ltester Zeitpunkt der Daten
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
		String stockName = null;
		int id = 0;
		String timeStamp = null;
		double stockValue = 0d;
		String newsTitle = null;
		String newsDescription = null;
		String newsUrl = null;
		
		if (this.model != null) {
			for (Map<String, AttributeValue> stockMap : updatedStockDataList) {
				Set<Entry<String, AttributeValue>>  stockSet = stockMap.entrySet();
				
				stockName = null;
				id = 0;
				timeStamp = null;
				stockValue = 0d;
				newsTitle = null;
				newsDescription = null;
				newsUrl = null;
				
				for (Entry<String, AttributeValue> item : stockSet) {
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

					if (chart != null) {
						updateModelAsync(chart, this.model.millisToHHMM(Long.parseLong(timeStamp)), stockValue);
					}
					else {
						if (stockName == null)
							stockName = getStockName(id);
						if (stockName == null)
							stockName = "unknown stock";
						
						chart = new ChartVO(id, stockName);
						model.addToChartList(chart);
						updateModelAsync(chart, this.model.millisToHHMM(Long.parseLong(timeStamp)), stockValue);
					}
				}
				
				if (newsTitle != null && newsDescription != null && newsUrl != null) {
					NewsVO news = new NewsVO(newsTitle, newsDescription, newsUrl, new Date(Long.parseLong(timeStamp)));
					ChartVO chart = model.returnChartById(id);
					chart.getNewsFeeds().add(news);
				}
			}
		}
	}
	
	private void updateModelAsync(final ChartVO chart, final String timeStamp, final double stockValue) {
		Platform.runLater(new Runnable() {
			 @Override
			 public void run() {
				 chart.getChart().getData().add(new XYChart.Data<String, Number>(timeStamp, stockValue));
				 model.updateTicker();
			 }
		 });
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
	 * @param id Aktie, die �fters aktualisiert werden soll
	 */
	public void setPriorityStock(int id) {
		if (id > 0)
			this.priorityStock = id;
		
		int stockId;
		Set<Entry<Integer, Long>> stockSet = stockUpdateQueue.entrySet();
		
		for (Entry<Integer, Long> stock : stockSet) {
			stockId = stock.getKey();
			
			if (stockId == this.priorityStock)
				stock.setValue(System.currentTimeMillis());
		}
	}
}
