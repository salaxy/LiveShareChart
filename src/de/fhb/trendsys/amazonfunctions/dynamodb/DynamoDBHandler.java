package de.fhb.trendsys.amazonfunctions.dynamodb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;

/**
 * Diese Klasse stellt CRUD-Funktionen f�r die Amazon DynamoDB zur Verf�gung.
 * F�r die genauere Funktionsweise siehe {@link http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/}.
 * 
 * @author Frank Mertens
 */
public class DynamoDBHandler {
	private AmazonDynamoDBClient ddbClient;
	private Regions region;
	private String selectedTable;
	
	/**
	 * Erzeugt einen Handler, der CRUD-Operationen f�r eine Tabelle in der Amazon DynamoDB zur Verf�gung stellt.
	 * Die zu verwendene Tabelle muss bereits angelegt und im Status "ACTIVE" sein.
	 * 
	 * @param region Standort der DynamoDB, zB Regions.EU_WEST_1
	 * @param tableName Tabelle, auf der gearbeitet werden soll.
	 * @see DynamoDBHandler#selectTable(String)
	 * @see DynamoDBHandler#getTableStatus()
	 */
	public DynamoDBHandler(Regions region, String tableName) {
		this.region = region;
		selectTable(tableName);	
		initDynamoDB();
	}
	
	/**
	 * Initialisiert die Verbindung zur Amazon DynamoDB �ber das AWS-SDK und stellt alles n�tige ein.
	 * Der Verbindungs-Timeout wird auf 2 Minuten gesetzt.
	 */
	private void initDynamoDB() {
		if (ddbClient != null)
			ddbClient.shutdown();
		
		ddbClient = new AmazonDynamoDBClient(new ClasspathPropertiesFileCredentialsProvider());
		ddbClient.setConfiguration(new ClientConfiguration().withConnectionTimeout(120000)); // 2 Min.
		
		Region currentRegion = Region.getRegion(region);
		ddbClient.setRegion(currentRegion);		
		
	}
	
	/**
	 * �ndert die Tabelle, auf der gearbeitet werden soll.
	 * Die Tabelle muss bereits angelegt und im Status "ACTIVE" sein.
	 * 
	 * @param tableName Tabelle, auf der gearbeitet werden soll.
	 * @see DynamoDBHandler#getTableStatus()
	 */
	public void selectTable(String tableName) {
		this.selectedTable = tableName;
	}
	
	/**
	 * Fragt ein Tupel ab.
	 * 
	 * @param id Prim�rschl�ssel des Tupels
	 * @return Tupel
	 * @see DynamoDBHandler#getAllItems(int)
	 * @see DynamoDBHandler#addItem(int, Map)
	 * @see DynamoDBHandler#deleteItem(int)
	 * @see DynamoDBHandler#updateItem(int, Map)
	 */
	public Map<String, AttributeValue> getItem(int id) {
		initDynamoDB();
		Map<String, AttributeValue> key = constructNewItem(id);
		GetItemRequest request = new GetItemRequest()
									 .withTableName(selectedTable)
									 .withKey(key);
		
		GetItemResult result = ddbClient.getItem(request);
		
		return result.getItem();		
	}
	
	/**
	 * Fragt alle Tupel ab, die dieselbe ID haben und deren Erstellungsdatum maximal 24h her ist.
	 * @param id Prim�rschl�ssel der Tupel
	 * @return Liste aller passenden Tupel
	 */
	public List<Map<String, AttributeValue>> getAllItems(int id) {
		return getAllItems(id, 86400000L);
//		return getAllItems(id, 10*86400000L);
	}
	
	/**
	 * Fragt alle Tupel ab, die dieselbe ID haben und deren Erstelldatum nicht vor dem angegeben Zeitstempel liegen.
	 * @param id Prim�rschl�ssel der Tupel
	 * @param sinceTimestamp �ltester Zeitpunkt eines Tupels 
	 * @return Liste aller passenden Tupel
	 */
	public List<Map<String, AttributeValue>> getAllItems(int id, long sinceTimestamp) {
		return getAllItems(id, sinceTimestamp, System.currentTimeMillis());
	}
	
	/**
	 * Fragt alle Tupel ab, die dieselbe ID haben und deren Erstelldatum innerhalb des angegeben Zeitstempels liegen.
	 * @param id Prim�rschl�ssel der Tupel
	 * @param fromTimestamp �ltester Zeitpunkt eines Tupels
	 * @param toTimestamp J�ngster Zeitpunkt eines Tupels 
	 * @return Liste aller passenden Tupel
	 */
	public List<Map<String, AttributeValue>> getAllItems(int id, long fromTimestamp, long toTimestamp) {
		Map<String, Condition> keyConditions = new HashMap<String, Condition>();
		
		Condition primaryKeyCondition = new Condition()
									 .withComparisonOperator(ComparisonOperator.EQ)
									 .withAttributeValueList(new AttributeValue()
									 							 .withN(Integer.toString(id)));
		keyConditions.put("id", primaryKeyCondition);
		
		Condition rangeCondition = new Condition()
									   .withComparisonOperator(ComparisonOperator.BETWEEN)
									   .withAttributeValueList(Arrays.asList(new AttributeValue().withS(Long.toString(fromTimestamp)),
											   								 new AttributeValue().withS(Long.toString(toTimestamp))));
		
		keyConditions.put("timestamp", rangeCondition);
		
		QueryRequest request = new QueryRequest()
								   .withTableName(selectedTable)
								   .withKeyConditions(keyConditions);
		
		QueryResult result = ddbClient.query(request);
		
		return result.getItems();
	}
	
	/**
	 * L�scht ein Tupel.
	 * 
	 * @param id Prim�rschl�ssel des Tupels
	 * @see DynamoDBHandler#addItem(int, Map)
	 * @see DynamoDBHandler#getItem(int)
	 * @see DynamoDBHandler#updateItem(int, Map)
	 */
    public void deleteItem(int id) {
		Map<String, AttributeValue> key = constructNewItem(id);
		DeleteItemRequest request = new DeleteItemRequest()
        								.withTableName(this.selectedTable)
        								.withKey(key);

    	ddbClient.deleteItem(request);
    }
    
    /**
     * Speichert ein Tupel in der Datenbank.
     * Exisitert bereits ein Tupel mit derselben ID, so wird dieses �berschrieben.
     * 
     * @param id Prim�rschl�ssel des Tupels
     * @param content Key/Values des Tupels
	 * @see DynamoDBHandler#getItem(int)
	 * @see DynamoDBHandler#deleteItem(int)
	 * @see DynamoDBHandler#updateItem(int, Map)
     */
    public void addItem(int id, Map<String, AttributeValue> content) {
        Map<String, AttributeValue> item = constructNewItem(id, content);

        PutItemRequest putItemRequest = new PutItemRequest()
        									.withTableName(selectedTable)
        									.withItem(item);
        
        ddbClient.putItem(putItemRequest);        
    }
    
    /**
     * Erstellt ein neues Tupel mit seiner ID als einzigen Inhalt.
     * 
     * @param id Prim�rschl�ssel des Tupels
     * @return Tupel
     * @see DynamoDBHandler#addItem(int, Map)
     */
    private Map<String, AttributeValue> constructNewItem(int id) {
    	return constructNewItem(id, new HashMap<String, AttributeValue>());
    }
    
    /**
     * Erstellt ein neues Tupel.
     * 
     * @param id Prim�rschl�ssel des Tupels
     * @param content Key/Values des Tupels
     * @return Tupel
     * @see DynamoDBHandler#addItem(int, Map)
     */
    private Map<String, AttributeValue> constructNewItem(int id, Map<String, AttributeValue> content) {
    	content.put("id", new AttributeValue()
        				     .withN(Integer.toString(id)));
                
        return content;
    }
    
    /**
     * Aktualisiert ein Tupel um neue Attribute und deren Inhalte.
     * 
     * @param id Prim�rschl�ssel des Tupels
     * @param content Key/Values des Tupels
     * @see DynamoDBHandler#addItem(int, Map)
     * @see DynamoDBHandler#deleteItem(int)
	 * @see DynamoDBHandler#getItem(int)
	 * @see DynamoDBHandler#constructUpdateContentItem(Map)
     */
    public void updateItem(int id, Map<String, AttributeValue> content) {
		Map<String, AttributeValue> key = constructNewItem(id);
    	Map<String, AttributeValueUpdate> value = constructUpdateContentItem(content);

        UpdateItemRequest request = new UpdateItemRequest()
        								.withTableName(selectedTable)
        								.withKey(key)
        								.withAttributeUpdates(value);
    	
        ddbClient.updateItem(request);        
    	
    }
    
    /**
     * Konvertiert einen Key/Value-Speicher in ein Format, dass zur Aktualisierung eines Tupel ben�tigt wird.
     * 
     * @param content Key/Values des Tupels
     * @return Key/ValueUpdates des Tupels
     * @see DynamoDBHandler#updateItem(int, Map)
     */
    private Map<String, AttributeValueUpdate> constructUpdateContentItem(Map<String, AttributeValue> content) {
    	Map<String, AttributeValueUpdate> updatedContent = new HashMap<String, AttributeValueUpdate>();
    	Set<Entry<String, AttributeValue>> contentSet = content.entrySet();
    	Iterator<Entry<String, AttributeValue>> iterator = contentSet.iterator();
    	
    	while (iterator.hasNext()) {
    		Entry<String, AttributeValue> keyValue = iterator.next();
    		
    		String key = keyValue.getKey();
    		AttributeValueUpdate value = new AttributeValueUpdate()
    										 .withValue(keyValue.getValue());
    		
    		updatedContent.put(key, value);
    	}
    	
        return updatedContent;
    }
    
    /**
     * Gibt den aktuellen Status der Tabelle zur�ck.
     * 
     * @return Status
     * @see DynamoDBHandler#selectTable(String)
     */
	public String getTableStatus() {
		DescribeTableRequest request = new DescribeTableRequest()
										   .withTableName(this.selectedTable);
        
		TableDescription tableDescription = ddbClient.describeTable(request).getTable();
        
        return tableDescription.getTableStatus();
	}
}
