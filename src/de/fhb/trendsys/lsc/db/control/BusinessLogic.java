package de.fhb.trendsys.lsc.db.control;

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

	/**
	 * Erzeugt eine Instanz der Businesslogik.
	 * Sie startet dabei auch einen {@link Worker}-Thread, der regelm��ig das Model aktialisiert.
	 * @param model Model
	 */
	public BusinessLogic(NewAdvancedAndFancyAppModel model) {
		Worker updateWorker = Worker.getInstance(model);
		this.model=model;
	}
	
	/**
	 * Informiert den {@link Worker}-Thread, jetzt zu �berpr�fen, ob er zu arbeiten hat.
	 */
	public void refresh(){
		System.out.println("refresh in Businesslogic");
		Worker updateWorker = Worker.getInstance(model);
		synchronized (updateWorker) {
			updateWorker.notify();
		}
	}
	
	/**
	 * Informiert den {@link Worker}-Thread, dass eine andere Aktie jetzt die Priorit�t hat.
	 * @param id Prim�rschl�ssel der Aktie in der Datenbank
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
