package com.anteboth.agrisys.data;

import java.io.IOException;
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

public class AgrisysDataManager {

	private static final String TAG = AgrisysDataManager.class.getName();
	
	public List<Flurstueck> loadFlurstueckList(Context context) {
		List<Flurstueck> data = new ArrayList<Flurstueck>();

		//		TODO use resource to get URL:  R.string.service_url_schlagdata
		String baseUri = "https://agri-sys.appspot.com";
		String urlSuffix = "/mobile/SchlagList.jsp";

		try {
			JSONArray array = getJSONArray(baseUri, urlSuffix, context);
			if (array != null) {
				for (int i=0; i<array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					JSONArray nameArray = json.names();
					JSONArray valArray = json.toJSONArray(nameArray);
					Flurstueck fs = new Flurstueck();
					for(int j=0; j<valArray.length(); j++) {
						if (nameArray.getString(j).equals("name")) {
							fs.setName(valArray.getString(j));
						} 
						else if (nameArray.getString(j).equals("id")) {
							fs.setId(valArray.getLong(j));
						}
						else if (nameArray.getString(j).equals("flaeche")) {
							fs.setFlaeche(valArray.getDouble(j));
						}
					}
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
		
		AndroidHttpClient client = AndroidHttpClient.newInstance( "IntegrationTestAgent", context);
		String result = null;
		try {
			//String baseUri = "https://android-gae-http-test.appspot.com";
			//"/authenticated/get"
			HttpContext httpContext = AuthenticatedAppEngineContext.newInstance(context, baseUri);
			HttpGet get = new HttpGet(baseUri + urlSuffix);
			HttpResponse response = client.execute(get, httpContext);
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