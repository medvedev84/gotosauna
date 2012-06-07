package com.gotosauna;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import com.gotosauna.util.JSONDownloader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashActivity extends Activity {

	GlobalStore globalStore;
	ArrayList<City> cities;
	private static final String GET_CITIES_URL = "http://go-to-sauna.ru/cities?json=true";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        globalStore = ((GlobalStore) getApplicationContext());    	
        cities = globalStore.getCities();
        if (cities == null || cities.size() == 0) {
        	cities = new ArrayList<City>();
	  		runOnUiThread(new Runnable() {
	 		     public void run() {
	 		    	new DownloadWebpageText().execute(GET_CITIES_URL);
	 		    }
	 		});         	
        }
    }
    	
    private class DownloadWebpageText extends AsyncTask<String, Integer, JSONArray> {
    	Dialog progress;
    	
        @Override
        protected void onPreExecute() {        	
            progress = ProgressDialog.show(SplashActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.please_wait));
            super.onPreExecute();
        }
        
    	@Override
		protected JSONArray doInBackground(String... urls) {
            String url = urls[0];
			try {
                 return (JSONArray) JSONDownloader.downloadUrl(url, true);
            } catch (IOException e) {
                return null;
            }		
		}
       
		@Override
		 protected void onProgressUpdate(Integer... progress) {
			 super.onProgressUpdate(progress);			 
		 }
	     
		@Override
		protected void onPostExecute(JSONArray result) {
			processData(result);
			progress.dismiss();
		}		
		
	    private void processData(JSONArray result){
			try {				
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = (JSONObject) result.get(i);                                        
                    cities.add( new City(jo.getString("id"), jo.getString("name")));                                     
                }				
                globalStore.setCities(cities);   
                				
				Intent intent = new Intent(getApplicationContext(), MainTabActivity.class);				
				startActivity(intent);  
			} catch (JSONException e) {
				//TODO
			}   	   			 
	    }	   
    }     
}
