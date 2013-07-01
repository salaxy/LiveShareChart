package de.fhb.trendsys.lsc.db.control;

import de.fhb.trendsys.lsc.model.ChartVO;
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
	Worker updateWorker;

	/**
	 * Erzeugt eine Instanz der Businesslogik.
	 * Sie startet dabei auch einen {@link Worker}-Thread, der regelmäßig das Model aktialisiert.
	 * @param model Model
	 */
	public BusinessLogic(AppModel model) {
		updateWorker = Worker.getInstance(model);
		this.model=model;
		
		while (updateWorker.getState() != Thread.State.TIMED_WAITING) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Informiert den {@link Worker}-Thread, jetzt zu überprüfen, ob er zu arbeiten hat.
	 */
	public void refresh(){
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
		this.refresh();
	}
}
