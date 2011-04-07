package com.anteboth.agrisys.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.anteboth.agrisys.AgrisysHelper;
import com.anteboth.agrisys.client.model.Aktivitaet;

public class AktivitaetFactory {


	public static Aktivitaet createAktivitaet(Long schlagErntejahrId, JSONObject json)
	throws JSONException {
		long id = getLong(json, "id");
		double flaeche = json.getDouble("flaeche");
		String d = getString(json, "datum");
		String bem = getString(json, "bemerkung");
		String modifiedOn = json.getString("lastModification");
		int type = json.getInt("type");


		Aktivitaet a = new Aktivitaet(schlagErntejahrId);
		a.setFlaeche(flaeche);
		a.setBemerkung(bem);
		a.setId(Long.valueOf(id));
		a.setDatum(AgrisysHelper.toDate(d));
		a.setLastModification(AgrisysHelper.toDate(modifiedOn));
		a.setType(type);
		
		//TODO read and sed the other - type-specific - properties 

		//the data is just loaded, so set the synchron flag to true
		a.setSynchron(true);
		return a;
	}
	
	
	private static String getString(JSONObject o, String fieldName) throws JSONException {
		return JSONObjectHelper.getString(o, fieldName);
	}

	private static long getLong(JSONObject o, String fieldName) throws JSONException {
		return JSONObjectHelper.getLong(o, fieldName);
	}
}
