package com.gotosauna;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SaunaShowActivity extends Activity  {
	private static final String URL_KEY="url";
	private static final String DEBUG_TAG = "GoToSauna";
	private static final int ACTIVITY_PHOTOS=2;
	
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
  		
        final Button buttonPhotos = (Button) findViewById(R.id.buttonPhotos);
        buttonPhotos.setOnClickListener(new View.OnClickListener()
        	{
                public void onClick(View v)
                {                	
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {                    	                    	                    	                    	
                       // Intent intent = new Intent(v.getContext(), ImageDownloaderTestActivity.class);                      
                    	Intent intent = new Intent(v.getContext(), SaunaPhotosActivity.class);
                        startActivityForResult(intent, ACTIVITY_PHOTOS);                              	
                    } else {
                    	Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                    
                }
            });  		
	}
	    
    private class DownloadWebpageText extends AsyncTask<String, Integer, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... urls) {
            String url = urls[0];
			try {
                 return (JSONObject) Downloader.downloadUrl(url, false);
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
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(result.getString("name"));
                
                TextView address = (TextView) findViewById(R.id.address);
                address.setText(result.getString("full_address"));
                
                TextView phone = (TextView) findViewById(R.id.phone);
                phone.setText(result.getString("phone_number1"));  
                
                JSONArray array = result.getJSONArray("sauna_items");				
                ArrayList<String> groups = new ArrayList<String>();
                ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();
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
	    	ExpandableListView listView = (ExpandableListView) findViewById(R.id.saunaItems);
	        /*
	    	listView.setOnChildClickListener(new OnChildClickListener()
	        {	           
	            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
	            {
	                Toast.makeText(getBaseContext(), "Child clicked", Toast.LENGTH_LONG).show();
	                return false;
	            }
	        });
	        
	        listView.setOnGroupClickListener(new OnGroupClickListener()
	        {
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					Toast.makeText(getBaseContext(), "Group clicked", Toast.LENGTH_LONG).show();
					return false;
				}	            	     
	        });
	        */

	        ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), groups, children);
	        listView.setAdapter(adapter);	    
	    
	    }
    } 	
}