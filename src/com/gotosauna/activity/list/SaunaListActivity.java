package com.gotosauna.activity.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.R;
import com.gotosauna.R.building_list;
import com.gotosauna.R.layout;
import com.gotosauna.activity.show.SaunaShowActivity;
import com.gotosauna.util.JSONDownloader;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SaunaListActivity extends ListActivity  {
	private static final String URL_KEY = "url";
	private static final String DEBUG_TAG = "GoToSauna";	
	private static final int ACTIVITY_SHOW = 1;
	private static final int ACTIVITY_CALL = 2;
	private static final String SHOW_SAUNA_URL = "http://go-to-sauna.ru/saunas/";
	
	private static final int ITEM_CALL = 1;
	private static final int ITEM_VIEW = 2;
	
	private HashMap<String, String> map = new HashMap<String, String>();
	
	
	ArrayAdapter<String> adapter = null;
	
	String saunaId;
	
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
	
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {      	
    	super.onCreateContextMenu(menu, v, menuInfo);     	
    	saunaId = map.get((String) ((TextView)((AdapterView.AdapterContextMenuInfo) menuInfo).targetView).getText());    	    	
        menu.setHeaderTitle("Context Menu");  
        menu.add(0, ITEM_CALL, 0, "Call");  
        menu.add(0, ITEM_VIEW, 0, "View");  
    }  	
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
    	switch (item.getItemId()) {
	    	case ITEM_CALL:
	    			callToSauna();
	    			break;
	    	case ITEM_VIEW:
	    			viewSauna();
	    			break;
	    	default:
	    		return false;    		
    	} 
        return true;  
    }  
  
    public void callToSauna(){  
    	Intent intent = new Intent(Intent.ACTION_CALL);
    	intent.setData(Uri.parse("tel:" + "+79043102536"));
    	startActivity(intent);    	
    }  
    
    public void viewSauna(){  
		String url = prepareUrl(saunaId);
        Intent intent = new Intent(getApplicationContext(), SaunaShowActivity.class);
        intent.putExtra(URL_KEY, url);
        startActivityForResult(intent, ACTIVITY_SHOW);      	    	
    } 
	    
    public String prepareUrl(String saunaId){	    		
    	StringBuffer sb = new StringBuffer(SHOW_SAUNA_URL);
    	sb.append(saunaId);
    	sb.append("?json=true");	    	       
    	return sb.toString();
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
  					saunaId = map.get((String)((TextView) view).getText());
  					/*
  					String url = prepareUrl((String)((TextView) view).getText());
                    Intent intent = new Intent(getApplicationContext(), SaunaShowActivity.class);
                    intent.putExtra(URL_KEY, url);
                    startActivityForResult(intent, ACTIVITY_SHOW);
                    */
  				}
  			});
  			 
  			registerForContextMenu(lv);
	    }	   
    } 	
    
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilter().filter(s);
        }

		public void afterTextChanged(Editable arg0) {}
    };
    
}