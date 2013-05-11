package fhb.trendsys.lsc.db.control;

import fhb.trendsys.lsc.model.AppModel;


/**
 * Business-Logik der Applikation
 * Hier werden die Daten aus der DB gezogen,
 * verarbeitet und ins Model geschrieben.
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 */
public class BusinessLogic {

	private AppModel model;

	public BusinessLogic(AppModel model) {
		this.model=model;
	}
	
	public void refresh(){
		
		//TODO Frank - Daten aktualsierung
		
	}

}
