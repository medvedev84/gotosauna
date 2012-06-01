package com.gotosauna;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.activity.list.CityListActivity;
import com.gotosauna.activity.search.SaunaSearchActivity;
import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import com.gotosauna.util.JSONDownloader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class GotosaunaActivity extends ListActivity {	

	private static final int MENU_SEARCH = 0;
	private static final int MENU_MAP = 1;
	private static final int MENU_CITIES = 2;	
	private static final int MENU_ABOUT = 3;
	
	private static final int ACTIVITY_SEARCH = 1;
	private static final int ACTIVITY_MAP = 2;
	private static final int ACTIVITY_CITIES = 3;
	private static final int ACTIVITY_ABOUT = 4;
	
	private static final String GET_CITIES_URL = "http://go-to-sauna.ru/cities?json=true";
	
	private ArrayList<String> menu_list = new ArrayList<String>();
	ArrayList<City> cities = new ArrayList<City>();
	
	ArrayAdapter<String> adapter = null;
	GlobalStore	globalStore;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       	         
        menu_list.add(MENU_SEARCH, getResources().getString(R.string.menu_search));
        menu_list.add(MENU_MAP, getResources().getString(R.string.menu_map));
        menu_list.add(MENU_CITIES, getResources().getString(R.string.menu_cities));
        menu_list.add(MENU_ABOUT, getResources().getString(R.string.menu_about));
        	
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.menu_item, menu_list);
		setListAdapter(adapter); 
		
  		ListView lv = getListView();
		lv.setTextFilterEnabled(true);				
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Intent intent = null;
				switch (position) {
					case MENU_SEARCH:
						intent = new Intent(getApplicationContext(), SaunaSearchActivity.class);			     
			            startActivityForResult(intent, ACTIVITY_SEARCH);                    
						break;
					case MENU_MAP:
						intent = new Intent(getApplicationContext(), SaunaSearchActivity.class);			     
			            startActivityForResult(intent, ACTIVITY_MAP);                    
						break;
					case MENU_CITIES:
						intent = new Intent(getApplicationContext(), CityListActivity.class);			     
			            startActivityForResult(intent, ACTIVITY_CITIES);                    
						break;		
					case MENU_ABOUT:
						intent = new Intent(getApplicationContext(), SaunaSearchActivity.class);			     
			            startActivityForResult(intent, ACTIVITY_ABOUT);                    
						break;							
				}
			}
		});	
				
		globalStore = ((GlobalStore) getApplicationContext());
		if (globalStore.getCities().isEmpty()) {
	  		runOnUiThread(new Runnable() {
	 		     public void run() {
	 		    	new DownloadWebpageText().execute(GET_CITIES_URL);
	 		    }
	 		}); 
		}		
    }
    
    private class DownloadWebpageText extends AsyncTask<String, Integer, JSONArray> {
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
			fillListView(result);
		}		
		
	    private void fillListView(JSONArray result){
			try {				
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = (JSONObject) result.get(i);                                        
                    cities.add( new City(jo.getString("id"), jo.getString("name")));                                     
                }				
                globalStore.setCities(cities);
			} catch (JSONException e) {
				//TODO
			}   	   			 
	    }	   
    } 	  
}