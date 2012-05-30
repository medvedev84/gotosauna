package com.gotosauna.activity.show;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.R;
import com.gotosauna.activity.photo.SaunaPhotosActivity;
import com.gotosauna.util.JSONDownloader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

public class SaunaShowActivity extends Activity  {
	private static final String URL_KEY="url";
	private static final String DEBUG_TAG = "GoToSauna";
	private static final int ACTIVITY_PHOTOS=2;
	private static final String PHOTOS_SAUNAS_URL = "http://go-to-sauna.ru/sauna_photos/";
	
	private int saunaId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sauna_show);
		
  		Bundle extras = getIntent().getExtras(); 
  		final String url = extras.getString(URL_KEY);
  		
  		runOnUiThread(new Runnable() {
  		     public void run() {
  		    	new DownloadWebpageText().execute(url);
  		    }
  		});  			
	}
	
    public String prepareUrl(){    	
    	StringBuffer sb = new StringBuffer(PHOTOS_SAUNAS_URL);
    	sb.append(saunaId);
    	sb.append("?json=true");	    	       
    	return sb.toString();
    }		
	
	    
    public class DownloadWebpageText extends AsyncTask<String, Integer, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... urls) {
            String url = urls[0];
			try {
                 return (JSONObject) JSONDownloader.downloadUrl(url, false);
            } catch (IOException e) {
                return null;
            }		
		}
       
		@Override
		 protected void onProgressUpdate(Integer... progress) {
			 super.onProgressUpdate(progress);			 
		 }
	     
		@Override
		protected void onPostExecute(JSONObject result) {
			fillView(result);
		}
		
	    private void fillView(JSONObject result){
			try {	
				saunaId = result.getInt("id");
				
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(result.getString("name"));
                
                TextView address = (TextView) findViewById(R.id.address);
                address.setText(result.getString("full_address"));
                
                TextView phone = (TextView) findViewById(R.id.phone);
                phone.setText(result.getString("phone_number1"));  
                
                JSONArray array = result.getJSONArray("sauna_items");				
                ArrayList<String> groups = new ArrayList<String>();
                ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();
                                
                // Add "Photo" row
                groups.add(getResources().getString(R.string.photo_gallery));
                children.add(new ArrayList<String>());
                
                // Add "Sauna items" rows
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jo = (JSONObject) array.get(i);
                    groups.add(jo.getString("name"));
                    
                    ArrayList<String> content = new ArrayList<String>();
                    content.add(jo.getString("description"));
                    content.add(jo.getString("min_price"));
                    content.add(jo.getString("capacity"));
                    children.add(content);
                }
                                
                initExpandable(groups, children);                
			} catch (JSONException e) {
				Log.d(DEBUG_TAG, "JSON failed: " + e.getMessage());
			}   	 			 		
	    }
	  
	    private void initExpandable(ArrayList<String> groups, ArrayList<ArrayList<String>> children){
	    	ExpandableListView listView = (ExpandableListView) findViewById(R.id.sauna_items);
	    	listView.setGroupIndicator(null);
	    	
	        listView.setOnGroupClickListener(new OnGroupClickListener()
	        {
				public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
					if (groupPosition == 0) {					
                    	Intent intent = new Intent(v.getContext(), SaunaPhotosActivity.class);
                    	intent.putExtra(URL_KEY, prepareUrl());
                        startActivityForResult(intent, ACTIVITY_PHOTOS);  						
					}					
					return false;
				}	            	     
	        });
	        
	        ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), groups, children);
	        listView.setAdapter(adapter);	    	    
	    }   
    } 	
}