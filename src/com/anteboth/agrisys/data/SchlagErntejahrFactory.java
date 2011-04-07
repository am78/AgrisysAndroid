package com.anteboth.agrisys.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;

public class SchlagErntejahrFactory {

	public static SchlagErntejahr createSchlagErntejahr(Stammdaten sd, JSONObject json)
	throws JSONException {
		JSONObject flurstueck = json.getJSONObject("flurstueck");
		String name = flurstueck.getString("name");

		JSONObject schlagErntejahr = json.getJSONObject("schlagErntejahr");
		double flaeche = schlagErntejahr.getDouble("flaeche");
		long id = schlagErntejahr.getLong("id");

		long anbauSorteId = schlagErntejahr.getJSONObject("anbauSorte").getLong("id");
		long vorfruchtKulturId = schlagErntejahr.getJSONObject("vorfrucht").getLong("id");

		SchlagErntejahr se = new SchlagErntejahr();
		se.setName(name);
		se.setFlaeche(flaeche);
		se.setId(id);
		se.setSorte(sd.getSorte(anbauSorteId));
		se.setVorfrucht(sd.getKultur(vorfruchtKulturId));
		return se;
	}

}
