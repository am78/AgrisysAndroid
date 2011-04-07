package com.anteboth.agrisys.data;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectHelper {

	/**
	 * Get the long value for the specified fieldName from the json object.
	 * @param o 			the json object
	 * @param fieldName 	the field name
	 * @return				the long value, is -1 if the field is not defined
	 * @throws JSONException	if something went wrong
	 */
	public static long getLong(JSONObject o, String fieldName) throws JSONException {
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
	public static String getString(JSONObject o, String fieldName) throws JSONException {
		String s = "";
		if (o != null && o.has(fieldName)) {
			s = o.getString(fieldName);
		}
		return s;
	}
	
}
