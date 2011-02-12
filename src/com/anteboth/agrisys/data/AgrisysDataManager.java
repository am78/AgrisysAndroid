package com.anteboth.agrisys.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountsException;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.anteboth.agrisys.AgrisysHelper;
import com.anteboth.agrisys.R;
import com.anteboth.agrisys.SystemException;
import com.anteboth.agrisys.client.model.AgrisysData;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;

public class AgrisysDataManager {
	
//	private static final String TAG = AgrisysDataManager.class.getName();

	/** The singleton instance. */
	private static AgrisysDataManager instance = new AgrisysDataManager();

	/** Flag needs to be set to false in production mode.
	 *  If true we will connect against the dev server. */
	private static boolean DEV = true;
	
	/** The local data file which holds the agrisys data. */
	private static final String DATA_FILE_NAME = "agrisys_data.dat";

	/** The TAG value. */
	private static final String TAG = AgrisysData.class.getName();

	/** Holds the cached data. */
	private AgrisysData cachedData;
	
	/**
	 * Returns the singleton instance.
	 * @return the {@link AgrisysDataManager} instance
	 */
	public static AgrisysDataManager getInstance() {
		return instance;
	}

	
	/**
	 * Returns the cached {@link AgrisysData} enrty.
	 * @return the {@link AgrisysData}
	 */
	public AgrisysData getCachedData() {
		return this.cachedData;
	}
	
	/**
	 * Synchronizes the data with the agrisys server.
	 * Refreshes the cached data.
	 * 
	 * @param context the context
	 * @throws SystemException if something went wrong
	 */
	public void synchronizeData(Context context) throws SystemException {
		AgrisysData ad = new AgrisysData();
		try {
			ad = loadAgrisysData(context);
		} finally {
			//this set the cached data, if something went wrong it's set back to default value 
			this.cachedData = ad;
		}
	}
	
	/**
	 * Loads all the neede data including base data
	 * from the agrisys server.
	 *  
	 * @param context the context
	 * @return the loaded {@link AgrisysData}
	 * @throws SystemException if something went wrong
	 */
	private AgrisysData loadAgrisysData(Context context) 
	throws SystemException {
	
		AgrisysData ad = new AgrisysData();
		
		Stammdaten sd = loadStammdaten(context);
		ad.setStammdaten(sd);
		
		List<SchlagErntejahr> sl = loadFlurstueckList(context, sd);
		ad.setFlurstueckList(sl);
		
		for (SchlagErntejahr se : sl) {
			Long schlagErntejahrId = se.getId();
			List<Aktivitaet> al = loadAktivitaetList(schlagErntejahrId, context);
			ad.putAktivitaet(schlagErntejahrId, al);
		}
		
		ad.setEmpty(false);
		return ad;
	}
	
	/**
	 * Stores the {@link #cachedData} {@link AgrisysData} entry to the
	 * file system.
	 * 
	 * @param context			the context
	 * @throws SystemException	if something went wrong
	 */
	public void storeAgrisysDataToFileSystem(Context context) 
	throws SystemException {
		try {
			storeData(getCachedData(), context);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			throw new SystemException(e);
		}
	}
	
	/**
	 * Loads the {@link AgrisysData} from the file system and updates the {@link #cachedData}
	 * item which is accessible thru {@link #getCachedData()}.
	 * 
	 * @param context			the context
	 * @throws SystemException  if something went wrong
	 */
	public void loadAgrisysDataFromFileSystem(Context context) throws SystemException {
		AgrisysData ad = new AgrisysData();
		try {
			ad = loadFromDataFile(context);
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage());
			throw new SystemException(e);
		} finally {
			this.cachedData = ad;
		}
	}
	
	/**
	 * Stores the {@link AgrisysData} object to the data file.
	 * Any existing file content will be overwritten.
	 * 
	 * @param data 			the data object to save
	 * @param context		the context
	 * @throws IOException	if something went wrong
	 */
	private void storeData(AgrisysData data, Context context) throws IOException {
		storeToDataFile(data, context);
	}
	
	/**
	 * Loads the stored {@link AgrisysData} item.
	 * @param context	the context
	 * @return			the loaded {@link AgrisysData} item
	 * @throws IOException 				if something went wrong
	 * @throws ClassNotFoundException 	if the stored data isn't a {@link AgrisysData} entry
	 */
	private AgrisysData loadFromDataFile(Context context) 
	throws IOException, ClassNotFoundException {
		FileInputStream fis = context.openFileInput(DATA_FILE_NAME);
		ObjectInputStream in = new ObjectInputStream(fis);
		AgrisysData data = (AgrisysData) in.readObject();
		in.close();
		return data;
	}
	
	/**
	 * Stores a {@link Serializable} object to the applications data file.
	 * 
	 * @param o the object to store
	 * @param context	the context
	 * @throws IOException	if something went wrong
	 */
	private void storeToDataFile(Object o, Context context) 
	throws IOException {
		FileOutputStream fos = context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(o);
		out.close();
	}
	
	/**
	 * Returns the base URL for the agrisys server from the application resources.
	 * (string.xml) 
	 * 
	 * @param context the context
	 * @return the base url
	 */
	private String getBaseUrl(Context context) {
		return context.getString(
			DEV ? R.string.agrisys_base_url_dev : R.string.agrisys_base_url);
	}
	
	/**
	 * Loads the {@link Stammdaten} values from the agrisys server.
	 * @param context the context
	 * @return {@link Stammdaten} if data could loaded
	 * @throws SystemException if something went wrong 
	 */
	private Stammdaten loadStammdaten(Context context) throws SystemException {
		String baseUri = getBaseUrl(context);
		String urlSuffix = "/service/stammdaten?media=json";
		
		Stammdaten sd = new Stammdaten();
		try {
			JSONObject json = getJSONObject(baseUri, urlSuffix, context);
			
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
		} catch (Throwable e) {
			throw new SystemException(e);
		}
		return sd;
	}
	
	/**
	 * Get the long value for the specified fieldName from the json object.
	 * @param o 			the json object
	 * @param fieldName 	the field name
	 * @return				the long value, is -1 if the field is not defined
	 * @throws JSONException	if something went wrong
	 */
	private long getLong(JSONObject o, String fieldName) throws JSONException {
		long l = -1;
		if (o != null && o.has(fieldName)) {
			l = o.getLong(fieldName);
		}
		return l;
	}
	
	/**
	 * Get the string value for the specified field name from the json object.
	 * 
	 * @param o				the json object
	 * @param fieldName		the field name
	 * @return				the defined string, is null if field not present
	 * @throws JSONException	if something went wrong
	 */
	private String getString(JSONObject o, String fieldName) throws JSONException {
		String s = "";
		if (o != null && o.has(fieldName)) {
			s = o.getString(fieldName);
		}
		return s;
	}

	/**
	 * Loads the {@link Aktivitaet} values for the specified schlagErntejahrID.
	 * 
	 * @param schlagErntejahrId the schlagErntejahrId
	 * @param context 			the context
	 * @return	the list of found {@link Aktivitaet} values
	 * @throws SystemException if something went wrong 
	 */
	private List<Aktivitaet> loadAktivitaetList(Long schlagErntejahrId, Context context) throws SystemException {
		String baseUri = getBaseUrl(context);
		String urlSuffix = "/service/aktivitaetList/" + schlagErntejahrId + "?media=json";
		Log.d(TAG, "SchlagErntejahr ID: " + schlagErntejahrId);
		
		List<Aktivitaet> data = new ArrayList<Aktivitaet>();
		try {
			JSONArray array = getJSONArray(baseUri, urlSuffix, context);
			if (array != null) {
				for (int i=0; i<array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					
					long id = getLong(json, "id");
					double flaeche = json.getDouble("flaeche");
					String d = getString(json, "datum");
					String bem = getString(json, "bemerkung");
					
//					JSONObject t = json.getJSONObject("typ");
//					String typ = getString(t, "kindClassName");
					
					Aktivitaet a = new Aktivitaet();
					a.setFlaeche(flaeche);
					a.setBemerkung(bem);
					a.setId(Long.valueOf(id));
					a.setDatum(AgrisysHelper.toDate(d));
//					a.setTyp(typ);
					
					data.add(a);
				}
			}
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage());
			throw new SystemException(e);
		}
		
		return data;
	}
	
	/**
	 * Load the {@link SchlagErntejahr} entries from the agrisys server.
	 * 
	 * @param context 	the context
	 * @return			the found {@link SchlagErntejahr} entries
	 * @throws SystemException if something went wrong 
	 */
	private List<SchlagErntejahr> loadFlurstueckList(Context context, Stammdaten sd) throws SystemException {
		String baseUri = getBaseUrl(context);
		String urlSuffix = "/service/schlagList?media=json";

		List<SchlagErntejahr> data = new ArrayList<SchlagErntejahr>();
		try {
			JSONArray array = getJSONArray(baseUri, urlSuffix, context);
			if (array != null) {
				for (int i=0; i<array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
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
					data.add(se);
				}
			}
		} catch (Throwable e) {
			throw new SystemException(e);
		}
		return data;
	}

	/**
	 * Loads a {@link JSONArray} from the specified URL.
	 * 
	 * @param baseUri 	the base ammount of the URL to call
	 * @param urlSuffix	the service ammount of the URL to call
	 * @param context	the context
	 * 
	 * @return	the loaded {@link JSONArray} object
	 * 
	 * @throws AuthenticationException	on authentication errors
	 * @throws AccountsException		on account errors
	 * @throws IOException				on IO problems
	 * @throws JSONException			on JSON problem
	 */
	private JSONArray getJSONArray(String baseUri, String urlSuffix, Context context) 
	throws AuthenticationException, AccountsException, IOException, JSONException {
		JSONArray array = null; 
		
		AndroidHttpClient client = AndroidHttpClient.newInstance( "IntegrationTestAgent");//, context);
		String result = null;
		try {
			//String baseUri = "https://android-gae-http-test.appspot.com";
			//"/authenticated/get"
			HttpGet get = new HttpGet(baseUri + urlSuffix);
			
			
			//do not use the authentication stuff when connection to test server
			HttpResponse response = null;
			if (!baseUri.startsWith("http://192.168")) {
				HttpContext httpContext = AuthenticatedAppEngineContext.newInstance(context, baseUri);
				response = client.execute(get, httpContext);
			} else {
				response = client.execute(get);
			}
			
			result = EntityUtils.toString(response.getEntity());
			
		} finally {
			client.close();
		}

		if (result != null) {			
			array = new JSONArray(result);
		}
		return array;
	}
	
	/**
	 * Loads a {@link JSONObject} from the specified URL.
	 * 
	 * @param baseUri 	the base ammount of the URL to call
	 * @param urlSuffix	the service ammount of the URL to call
	 * @param context	the context
	 * 
	 * @return	the loaded {@link JSONObject} object
	 * 
	 * @throws AuthenticationException	on authentication errors
	 * @throws AccountsException		on account errors
	 * @throws IOException				on IO problems
	 * @throws JSONException			on JSON problem
	 */
	private JSONObject getJSONObject(String baseUri, String urlSuffix, Context context) 
	throws AuthenticationException, AccountsException, IOException, JSONException {
		JSONObject data = null; 
		
		AndroidHttpClient client = AndroidHttpClient.newInstance( "IntegrationTestAgent");//, context);
		String result = null;
		try {
			//String baseUri = "https://android-gae-http-test.appspot.com";
			//"/authenticated/get"
			HttpGet get = new HttpGet(baseUri + urlSuffix);
			
			
			//do not use the authentication stuff when connection to test server
			HttpResponse response = null;
			if (!baseUri.startsWith("http://192.168")) {
				HttpContext httpContext = AuthenticatedAppEngineContext.newInstance(context, baseUri);
				response = client.execute(get, httpContext);
			} else {
				response = client.execute(get);
			}
			
			result = EntityUtils.toString(response.getEntity());
			
		} finally {
			client.close();
		}

		if (result != null) {			
			data = new JSONObject(result);
		}
		return data;
	}
}