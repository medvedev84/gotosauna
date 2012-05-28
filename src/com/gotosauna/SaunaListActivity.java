package com.gotosauna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SaunaListActivity extends ListActivity  {
	private static final String URL_KEY="url";
	private static final String DEBUG_TAG = "GoToSauna";	
	private static final int ACTIVITY_SHOW=1;
	private static final String SHOW_SAUNA_URL = "http://go-to-sauna.ru/saunas/";
	 
	private HashMap<String, String> map = new HashMap<String, String>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

  		Bundle extras = getIntent().getExtras(); 
  		final String url = extras.getString(URL_KEY);
  		
  		runOnUiThread(new Runnable() {
  		     public void run() {
  		    	new DownloadWebpageText().execute(url);
  		    }
  		});  	   
	}
	    
    private class DownloadWebpageText extends AsyncTask<String, Integer, JSONArray> {
		@Override
		protected JSONArray doInBackground(String... urls) {
            String url = urls[0];
			try {
                 return (JSONArray) Downloader.downloadUrl(url, true);
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
				ArrayList<String> listItems = new ArrayList<String>();
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = (JSONObject) result.get(i);
                    listItems.add(jo.getString("name"));
                    map.put(jo.getString("name"), jo.getString("id"));
                }
				
				setListAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.sauna_item, listItems)); 
			} catch (JSONException e) {
				Log.d(DEBUG_TAG, "JSON failed: " + e.getMessage());
			}   	 
			
  	  		ListView lv = getListView();
  			lv.setTextFilterEnabled(true);		
  			lv.setOnItemClickListener(new OnItemClickListener() {
  				public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
  					String url = prepareUrl((String)((TextView) view).getText());
                    Intent intent = new Intent(getApplicationContext(), SaunaShowActivity.class);
                    intent.putExtra(URL_KEY, url);
                    startActivityForResult(intent, ACTIVITY_SHOW);  
  				}
  			});   			
	    }
	    
	    private String prepareUrl(String saunaName){	    		
	    	StringBuffer sb = new StringBuffer(SHOW_SAUNA_URL);
	    	sb.append(map.get(saunaName));
	    	sb.append("?json=true");	    	       
	    	return sb.toString();
	    }

		
		
    } 	
}