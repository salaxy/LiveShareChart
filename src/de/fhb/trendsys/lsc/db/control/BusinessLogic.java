package de.fhb.trendsys.lsc.db.control;

import de.fhb.trendsys.lsc.model.ChartVO;
import de.fhb.trendsys.lsc.model.NewAdvancedAndFancyAppModel;


/**
 * Business-Logik der Applikation
 * Hier werden die Daten aus der DB gezogen,
 * verarbeitet und ins Model geschrieben.
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * @author Frank Mertens
 */
public class BusinessLogic {

	private NewAdvancedAndFancyAppModel model;
	Worker updateWorker;

	/**
	 * Erzeugt eine Instanz der Businesslogik.
	 * Sie startet dabei auch einen {@link Worker}-Thread, der regelmäßig das Model aktialisiert.
	 * @param model Model
	 */
	public BusinessLogic(NewAdvancedAndFancyAppModel model) {
		updateWorker = Worker.getInstance(model);
		this.model=model;
	}
	
	/**
	 * Informiert den {@link Worker}-Thread, jetzt zu überprüfen, ob er zu arbeiten hat.
	 */
	public void refresh(){
		System.out.println("refresh in Businesslogic");
		updateWorker = Worker.getInstance(model);
		synchronized (updateWorker) {
			updateWorker.notify();
		}
	}
	
	/**
	 * Informiert den {@link Worker}-Thread, dass eine andere Aktie jetzt die Priorität hat.
	 * @param id Primärschlüssel der Aktie in der Datenbank
	 */
	public void refreshById(int id) {
		updateWorker = Worker.getInstance(model);
		updateWorker.setPriorityStock(id);
		this.refresh();
	}
	
	
	/**
	 * Informiert den {@link Worker}-Thread, dass eine andere Aktie jetzt die Priorität hat.
	 * @param name der Aktie in der Datenbank
	 */
	public void refreshByName(String name) {
		ChartVO currentChart =this.model.returnChartByName(name);
		
		if(currentChart!=null){
			updateWorker = Worker.getInstance(model);
			updateWorker.setPriorityStock(currentChart.getId());
			this.refresh();
		}
	}
	
	public void shutdown() {
		updateWorker = Worker.getInstance(model);
		updateWorker.interrupt();
	}
}
