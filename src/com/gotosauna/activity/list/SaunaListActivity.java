package com.gotosauna.activity.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.R;
import com.gotosauna.activity.show.SaunaShowActivity;
import com.gotosauna.activity.show.SaunaTabActivity;
import com.gotosauna.core.Sauna;
import com.gotosauna.core.SaunaItem;
import com.gotosauna.util.Constants;
import com.gotosauna.util.JSONDownloader;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	private static final String CITY_ID_KEY = "cityId";	
	private static final int ACTIVITY_SHOW = 1;
	private static final String SHOW_SAUNA_URL = "http://go-to-sauna.ru/saunas/";
	
	private static final int ITEM_CALL = 1;
	private static final int ITEM_VIEW = 2;
	
	private HashMap<String, Sauna> saunas = new HashMap<String, Sauna>();
	Sauna selectedSauna;
	private String cityId;
	
	ArrayAdapter<String> adapter = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
  	    setContentView(R.layout.sauna_list);
  	    
  		Bundle extras = getIntent().getExtras(); 
  		final String url = extras.getString(URL_KEY);
  		cityId = extras.getString(CITY_ID_KEY);
  		
  		runOnUiThread(new Runnable() {
  		     public void run() {
  		    	new DownloadWebpageText().execute(url);
  		    }
  		});  		  		  		
		
	}
	
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {      	
    	super.onCreateContextMenu(menu, v, menuInfo);     	
    	selectedSauna = saunas.get((String) ((TextView)((AdapterView.AdapterContextMenuInfo) menuInfo).targetView).getText());    	    	
        menu.setHeaderTitle(getResources().getString(R.string.select_action));  
        menu.add(0, ITEM_CALL, 0, getResources().getString(R.string.call_to_sauna));  
        //menu.add(0, ITEM_VIEW, 0, getResources().getString(R.string.view_sauna));  
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
    	intent.setData(Uri.parse("tel:" + selectedSauna.getPhoneNumber()));
    	startActivity(intent);    	
    }  
    
    public void viewSauna(){  
		String url = prepareUrl();
        Intent intent = new Intent(getApplicationContext(), SaunaShowActivity.class);
        intent.putExtra(URL_KEY, url);
        startActivityForResult(intent, ACTIVITY_SHOW);      	    	
    } 
	    
    public String prepareUrl(){	    		
    	StringBuffer sb = new StringBuffer(SHOW_SAUNA_URL);
    	sb.append(selectedSauna.getId());
    	sb.append("?json=true");	    	       
    	return sb.toString();
    }
    
    private class DownloadWebpageText extends AsyncTask<String, Integer, JSONArray> {
    	Dialog progress;
    	
        @Override
        protected void onPreExecute() {
        	progress = ProgressDialog.show(SaunaListActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.please_wait));                         
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
			fillListView(result);
			progress.dismiss();
		}		
		
	    private void fillListView(JSONArray result){
	    	if (result.length() > 0) {
				try {				
					ArrayList<String> listItems = new ArrayList<String>();
	                for (int i = 0; i < result.length(); i++) {
	                    JSONObject jo = (JSONObject) result.get(i);                    
	                    listItems.add(jo.getString("name"));        
	                    Sauna sauna = new Sauna(jo.getString("id"), jo.getString("name"), jo.getString("phone_number1"));
	                    sauna.setCityId(cityId);
	                    saunas.put(jo.getString("name"), sauna);                    
	                }				
	                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sauna_item, listItems);
					setListAdapter(adapter); 
				} catch (JSONException e) {				
				}   	 
				
	  	  		ListView lv = getListView();
	  			lv.setTextFilterEnabled(true);		
	  			
	  			lv.setOnItemClickListener(new OnItemClickListener() {
	  				public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
	  	                SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);  	              
	  	                int savedResolutionId = settings.getInt(Constants.QUALITY_KEY, 0);     	                
	  					selectedSauna = saunas.get((String)((TextView) view).getText());    					
	  					StringBuffer sb = new StringBuffer(SHOW_SAUNA_URL).append(selectedSauna.getId()).append("?json=true");
	  	                Intent intent = new Intent(getApplicationContext(), SaunaTabActivity.class);
	  	                intent.putExtra(Constants.SAUNA_KEY, popualteSauna(selectedSauna, sb.toString()));
	  	                intent.putExtra(Constants.TAB_NUM_ID, 0);
	  	                intent.putExtra(Constants.RESOLUTION_ID_KEY, savedResolutionId);  	 
	  	                startActivityForResult(intent, ACTIVITY_SHOW);                    
	  				}
	  			});  			 
	  			registerForContextMenu(lv);
	  			
		  		EditText filterText = (EditText) findViewById(R.building_list.search_box);
		  	    filterText.addTextChangedListener(filterTextWatcher);		    		
	    	} else {
	        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.saunas_not_found), Toast.LENGTH_LONG).show();         		   	            
	    	}	    		  		
	    }    
    } 	
    
    private Sauna popualteSauna(Sauna sauna, String url){		  					
		try {					
			JSONObject result = (JSONObject) JSONDownloader.downloadUrl(url, false);
			sauna.setAddress(result.getString("full_address"));
            JSONArray array = result.getJSONArray("sauna_items");				              
            ArrayList<SaunaItem> sauna_items = new ArrayList<SaunaItem>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = (JSONObject) array.get(i);
                sauna_items.add(new SaunaItem(jo.getString("name"), jo.getString("description"), jo.getString("min_price"), jo.getString("capacity")));                                    
            } 
            sauna.setItems(sauna_items);						
		} catch (IOException e) {
		   	Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_io), Toast.LENGTH_SHORT).show();		     
		} catch (JSONException e) {
		   	Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_json), Toast.LENGTH_SHORT).show();
		     
		}      	
    	return sauna;
    }
    
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (adapter != null) {
            	adapter.getFilter().filter(s);	
            }        	
        }

		public void afterTextChanged(Editable arg0) {}
    };
    
}