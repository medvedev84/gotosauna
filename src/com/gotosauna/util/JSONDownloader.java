package com.gotosauna.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDownloader {
	
   public static Object downloadUrl(String myurl, boolean isArray) throws IOException {
	    InputStream is = null;		        
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        conn.connect();
	        conn.getResponseCode();
	        is = conn.getInputStream();	        
	        return isArray ? getJSONArray(is): getJSONObject(is);
	    } catch (JSONException ex){	    	 
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
		return null;
	}	
	   
	private static JSONArray getJSONArray(InputStream stream) throws JSONException{
		JSONArray obj = new JSONArray(convertStreamToString(stream));
	    return obj;
	}
	
	private static JSONObject getJSONObject(InputStream stream) throws JSONException{
		JSONObject obj = new JSONObject(convertStreamToString(stream));
	    return obj;
	}	
	
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }		   
}
