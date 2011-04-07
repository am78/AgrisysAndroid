package com.anteboth.agrisys.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;

public class StammdatenFactory {
	
	public static Stammdaten createStammdaten(JSONObject json) throws JSONException {
		Stammdaten sd;
		sd = new Stammdaten();
		
		JSONArray array = json.getJSONArray("kulturList");
		int l = array.length();
		for (int i=0; i<l; i++) {
			JSONObject o = array.getJSONObject(i);
			long id = getLong(o, "id");
			String name = getString(o, "name");
			String beschreibung = getString(o, "beschreibung");
			Kultur k = new Kultur();
			k.setId(id);
			k.setName(name);
			k.setBeschreibung(beschreibung);
			sd.getKulturList().add(k);
		}
		
		array = json.getJSONArray("sorteList");
		l = array.length();
		for (int i=0; i<l; i++) {
			JSONObject o = array.getJSONObject(i);
			long id = getLong(o, "id");
			String name = getString(o, "name");
			String beschreibung = getString(o, "beschreibung");
			long kulturID = o.getJSONObject("kultur").getLong("id");
			Kultur k = sd.getKultur(kulturID);
			Sorte s = new Sorte();
			s.setId(id);
			s.setName(name);
			s.setBeschreibung(beschreibung);
			s.setKultur(k);
			sd.getSorteList().add(s);
		}
		
		array = json.getJSONArray("duengerartList");
		l = array.length();
		for (int i=0; i<l; i++) {
			JSONObject o = array.getJSONObject(i);
			long id = getLong(o, "id");
			String name = getString(o, "name");
			String beschreibung = getString(o, "beschreibung");
			Duengerart d = new Duengerart();
			d.setId(id);
			d.setName(name);
			d.setBeschreibung(beschreibung);
			sd.getDuengerartList().add(d);
		}
		
		array = json.getJSONArray("psMittelList");
		l = array.length();
		for (int i=0; i<l; i++) {
			JSONObject o = array.getJSONObject(i);
			long id = getLong(o, "id");
			String name = getString(o, "name");
			String beschreibung = getString(o, "beschreibung");
			PSMittel p = new PSMittel();
			p.setId(id);
			p.setName(name);
			p.setBeschreibung(beschreibung);
			sd.getPsMittelList().add(p);
		}
		
		array = json.getJSONArray("bodenbearbeitungTypList");
		l = array.length();
		for (int i=0; i<l; i++) {
			JSONObject o = array.getJSONObject(i);
			long id = getLong(o, "id");
			String name = getString(o, "name");
			String beschreibung = getString(o, "beschreibung");
			BodenbearbeitungTyp b = new BodenbearbeitungTyp();
			b.setId(id);
			b.setName(name);
			b.setBeschreibung(beschreibung);
			sd.getBodenbearbeitungTypList().add(b);
		}
		return sd;
	}

	private static String getString(JSONObject o, String fieldName) throws JSONException {
		return JSONObjectHelper.getString(o, fieldName);
	}

	private static long getLong(JSONObject o, String fieldName) throws JSONException {
		return JSONObjectHelper.getLong(o, fieldName);
	}
	

}
