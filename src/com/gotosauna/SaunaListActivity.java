package com.gotosauna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SaunaListActivity extends ListActivity  {
	private static final String URL_KEY="url";
	private static final String DEBUG_TAG = "GoToSauna";	
	private static final int ACTIVITY_SHOW=1;
	private static final String SHOW_SAUNA_URL = "http://go-to-sauna.ru/saunas/";
	 
	private HashMap<String, String> map = new HashMap<String, String>();
	ArrayAdapter<String> adapter = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

  		Bundle extras = getIntent().getExtras(); 
  		final String url = extras.getString(URL_KEY);

  	    setContentView(R.layout.sauna_list);
  	  
  		runOnUiThread(new Runnable() {
  		     public void run() {
  		    	new DownloadWebpageText().execute(url);
  		    }
  		});  	
  		
  		
  		EditText filterText = (EditText) findViewById(R.building_list.search_box);
  	    filterText.addTextChangedListener(filterTextWatcher);
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
				
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sauna_item, listItems);
				setListAdapter(adapter); 
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
    
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
            adapter.getFilter().filter(s);
        }

		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}
    };
    
}