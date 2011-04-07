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

import com.anteboth.agrisys.R;
import com.anteboth.agrisys.SystemException;
import com.anteboth.agrisys.client.model.AgrisysData;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
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
		//1st put all changes made by the mobile clien to the server
		if (this.cachedData != null) {
			List<Aktivitaet> changed = this.cachedData.getChanged();
			//TODO put to server
		}
		
		
		//than get the data from the server
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
		
		Stammdaten sd = null;
		try {
			JSONObject json = getJSONObject(baseUri, urlSuffix, context);
			
			sd = StammdatenFactory.createStammdaten(json);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		return sd;
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
		Log.d(TAG, "SchlagErntejahr ID: " + schlagErntejahrId);
		
		//get all entries from the server
		List<Aktivitaet> data = new ArrayList<Aktivitaet>();
		try {
			String urlSuffix = "/service/aktivitaetList/" + schlagErntejahrId + "?media=json";
			JSONArray array = getJSONArray(baseUri, urlSuffix, context);
			if (array != null) {
				for (int i=0; i<array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					
					Aktivitaet a = AktivitaetFactory.createAktivitaet(schlagErntejahrId, json);
					
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
					SchlagErntejahr se = SchlagErntejahrFactory.createSchlagErntejahr(sd, json);
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
	throws AuthenticationException, AccountsException, IOException, JSONException, Throwable {
		JSONObject data = null; 
		
		AndroidHttpClient client = AndroidHttpClient.newInstance( "IntegrationTestAgent");//, context);
		String result = null;
		try {
			
			System.out.println("load data from: " + baseUri + urlSuffix);
			
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
			System.out.println("data loaded");
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			client.close();
		}
		

		if (result != null) {			
			data = new JSONObject(result);
		}
		return data;
	}
}