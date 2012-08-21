package com.gotosauna;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.core.Advertisement;
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
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	GlobalStore globalStore;
	ArrayList<City> cities;
	private static final String GET_CITIES_URL = "http://go-to-sauna.ru/cities?json=true";
	private static final String GET_ADVERTISEMENTS_URL = "http://go-to-sauna.ru/advertisements?json=true";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
                
        Display display = getWindowManager().getDefaultDisplay();  
    	globalStore = ((GlobalStore) getApplicationContext());       	  		 		
  		globalStore.setScreenWidth(display.getWidth());
  		globalStore.setScreenHeight(display.getHeight());
  		
        TextView about = (TextView) findViewById(R.id.textViewSplash);
        about.setMovementMethod(LinkMovementMethod.getInstance());  		
  		
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {                    	                    	                    	                    	                    	
            cities = globalStore.getCities();
            
            if (cities == null || cities.size() == 0) {
            	cities = new ArrayList<City>();        	
            	runOnUiThread(new Runnable() {
	   	 		    public void run() {
	   	 		    	new DownloadWebpageText().execute(new String[] {GET_CITIES_URL, GET_ADVERTISEMENTS_URL});
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
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_network), Toast.LENGTH_SHORT).show();
        }            
    }
    	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exit_menu, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	  
	    switch (item.getItemId()) {
	        case R.id.quit:
	        	finish();
	            return true;	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
	
    private class DownloadWebpageText extends AsyncTask<String, Integer, ArrayList<JSONArray>> {
    	Dialog progress;
    	
        @Override
        protected void onPreExecute() {        	
            progress = ProgressDialog.show(SplashActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.please_wait));
            super.onPreExecute();
        }
        
    	@Override
		protected ArrayList<JSONArray> doInBackground(String... urls) {            
			try {
			    ArrayList<JSONArray> array = new ArrayList<JSONArray>();
				JSONArray cities = (JSONArray) JSONDownloader.downloadUrl(urls[0], true);
				JSONArray advertisements = (JSONArray) JSONDownloader.downloadUrl(urls[1], true);
                array.add(cities);
                array.add(advertisements);
                return array;
            } catch (IOException e) {
                return null;
            }		
		}
       
		@Override
		 protected void onProgressUpdate(Integer... progress) {
			 super.onProgressUpdate(progress);			 
		 }
	     
		@Override
		protected void onPostExecute(ArrayList<JSONArray> result) {
			processData(result);
			progress.dismiss();
		}		
		
	    private void processData(ArrayList<JSONArray> result){	    	
	    	JSONArray citiesJSONArray = result.get(0);
	    	JSONArray advertisementsJSONArray = result.get(1);
	    	try {				
                for (int i = 0; i < citiesJSONArray.length(); i++) {
                    JSONObject jo = (JSONObject) citiesJSONArray.get(i);                                        
                    cities.add( new City(jo.getString("id"), jo.getString("name")));                                     
                }				
                globalStore.setCities(cities);   
                	
            	ArrayList<Advertisement> advertisements = new ArrayList<Advertisement>();
                for (int i = 0; i < advertisementsJSONArray.length(); i++) {
                    JSONObject jo = (JSONObject) advertisementsJSONArray.get(i);                                        
                    advertisements.add( new Advertisement(jo.getString("city_id"), jo.getString("company_name"), jo.getString("description"), jo.getString("phone_number")));                                     
                }				
                globalStore.setAdvertisements(advertisements); 
                
				Intent intent = new Intent(getApplicationContext(), MainTabActivity.class);				
				startActivity(intent);  
			} catch (JSONException e) {
				//TODO
			}   	   			 
	    }	   
    }     
}
