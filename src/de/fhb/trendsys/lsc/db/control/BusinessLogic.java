package de.fhb.trendsys.lsc.db.control;

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

	/**
	 * Erzeugt eine Instanz der Businesslogik.
	 * Sie startet dabei auch einen {@link Worker}-Thread, der regelmäßig das Model aktialisiert.
	 * @param model Model
	 */
	public BusinessLogic(AppModel model) {
		Worker updateWorker = Worker.getInstance(model);
		
		this.model=model;
	}
	
	/**
	 * Informiert den {@link Worker}-Thread, jetzt zu überprüfen, ob er zu arbeiten hat.
	 */
	public void refresh(){
		Worker updateWorker = Worker.getInstance(model);
		synchronized (updateWorker) {
			updateWorker.notify();
		}
	}
	
	/**
	 * Informiert den {@link Worker}-Thread, dass eine andere Aktie jetzt die Priorität hat.
	 * @param id Primärschlüssel der Aktie in der Datenbank
	 */
	public void refresh(int id) {
		Worker updateWorker = Worker.getInstance(model);
		updateWorker.setPriorityStock(id);
		this.refresh();
	}
	
	public void shutdown() {
		Worker updateWorker = Worker.getInstance(model);
		updateWorker.interrupt();
	}
}
