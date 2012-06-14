package com.gotosauna.activity.show;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.R;
import com.gotosauna.core.Sauna;
import com.gotosauna.util.JSONDownloader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class SaunaShowActivity extends Activity  {
	private static final String URL_KEY = "url";
	private static final String PHOTOS_SAUNAS_URL = "http://go-to-sauna.ru/sauna_photos/";
	private Sauna sauna;
	
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
	
	public void onButtonClicked(View v) {
    	Intent intent = new Intent(Intent.ACTION_CALL);
    	intent.setData(Uri.parse("tel:" + sauna.getPhoneNumber()));
    	startActivity(intent);   	    
	}
	
    public String prepareUrl(){    	
    	StringBuffer sb = new StringBuffer(PHOTOS_SAUNAS_URL);
    	sb.append(sauna.getId());
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
				sauna = new Sauna(result.getString("id"), result.getString("name"), result.getString("phone_number1"), result.getString("full_address"));
				
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(sauna.getName());
                
                TextView address = (TextView) findViewById(R.id.address);
                address.setText(sauna.getAddress());

                Button button = (Button) findViewById(R.id.buttonCall);
                button.setText(sauna.getPhoneNumber());
                
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
			}   	 			 		
	    }
	  
	    private void initExpandable(ArrayList<String> groups, ArrayList<ArrayList<String>> children){
	    	ExpandableListView listView = (ExpandableListView) findViewById(R.id.sauna_items);
	    	listView.setGroupIndicator(null);        
	        ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), groups, children);
	        listView.setAdapter(adapter);	
	        listView.expandGroup(0);
	    }   
    } 	
}