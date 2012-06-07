package com.gotosauna;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.activity.list.SaunaListActivity;
import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import com.gotosauna.util.JSONDownloader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SplashActivity extends Activity {

	GlobalStore globalStore;
	ArrayList<City> cities;
	private static final String GET_CITIES_URL = "http://go-to-sauna.ru/cities?json=true";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {                    	                    	                    	                    	
            
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
            
            final Button buttonSearch = (Button) findViewById(R.id.buttonContinue);
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {                	
                	Intent intent = new Intent(getApplicationContext(), MainTabActivity.class);
                	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			startActivity(intent);                      
                }
            });
           
        } else {        	
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
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
