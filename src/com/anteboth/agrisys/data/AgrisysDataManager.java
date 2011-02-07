package com.anteboth.agrisys.data;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class AgrisysDataManager {

	//		TODO use resource to get URL:  R.string.service_url_schlagdata
//	private static final String BASE_URL =  "https://agri-sys.appspot.com";
	private static final String BASE_URL =  "http://192.168.178.28:8888";
	
	private static final String TAG = AgrisysDataManager.class.getName();
	
	
	public List<Aktivitaet> loadAktivitaetList(Long schlagErntejahrId, Context context) {
		String baseUri = BASE_URL;
		String urlSuffix = "/service/aktivitaetList/" + schlagErntejahrId + "?media=json";

		List<Aktivitaet> data = new ArrayList<Aktivitaet>();
		try {
			JSONArray array = getJSONArray(baseUri, urlSuffix, context);
			if (array != null) {
				for (int i=0; i<array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					
					double flaeche = json.getDouble("flaeche");
					String d = json.getString("datum");
					
					String bem = "";
					if (!json.isNull("bemerkung")) {
						bem = json.getString("bemerkung");
					}
					
					JSONObject t = json.getJSONObject("typ");
					String typ = t.getString("kindClassName");
					
					Aktivitaet a = new Aktivitaet();
					a.setFlaeche(flaeche);
					a.setBemerkung(bem);
					a.setTyp(typ);
					
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
						Date date = sdf.parse(d);
						a.setDatum(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					data.add(a);
				}
			}
		} catch (AuthenticationException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (AccountsException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		
		return data;
	}
	
	public List<Flurstueck> loadFlurstueckList(Context context) {
		String baseUri = BASE_URL;
		String urlSuffix = "/service/schlagList?media=json";

		List<Flurstueck> data = new ArrayList<Flurstueck>();
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
					
					Flurstueck fs = new Flurstueck();
					fs.setName(name);
					fs.setFlaeche(flaeche);
					fs.setId(id);
					
					data.add(fs);
				}
			}
		} catch (AuthenticationException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (AccountsException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		
		return data;
	}

	public JSONArray getJSONArray(String baseUri, String urlSuffix, Context context) 
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
}