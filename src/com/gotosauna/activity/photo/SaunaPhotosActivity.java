package com.gotosauna.activity.photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.R;
import com.gotosauna.util.GlobalStore;
import com.gotosauna.util.ImageDownloader;
import com.gotosauna.util.JSONDownloader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class SaunaPhotosActivity extends Activity {
	private static final String URL_KEY="url";	
	private static final int MAX_PHOTO_ARRAY_SIZE = 6;
	
	private int imageWidth = 100;
	private int imageHeight = 100;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sauna_photos_grid);
        
  		Bundle extras = getIntent().getExtras(); 
  		final String url = extras.getString(URL_KEY);

  		GlobalStore globalStore = ((GlobalStore) getApplicationContext());       	  
  		imageWidth = globalStore.getScreenWidth() / 2 - 10;
  		imageHeight = imageWidth;
  		
  		runOnUiThread(new Runnable() {
 		     public void run() {
 		    	new DownloadWebpageText().execute(url);
 		    }
 		}); 
    }	
    
    private class DownloadWebpageText extends AsyncTask<String, Integer, JSONObject> {
    	Dialog progress;
    	
        @Override
        protected void onPreExecute() {
        	progress = ProgressDialog.show(SaunaPhotosActivity.this, getResources().getString(R.string.loading_data), getResources().getString(R.string.please_wait));             
            super.onPreExecute();
        }
        
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
			fillListView(result);			
			progress.dismiss();
		}		
		
	    private void fillListView(JSONObject result){
			try {										
                JSONArray array = result.getJSONArray("sauna_photos");		
                
                final ArrayList<String> urls = new ArrayList<String>();
                int size = Math.min(array.length(), MAX_PHOTO_ARRAY_SIZE);
                
                for (int i = 0; i < size; i++) {
                    JSONObject jo = (JSONObject) array.get(i);
                    urls.add(jo.getString("photo_url_thumb"));
                }
                           
                if (urls.size() > 0) {     
                	ImageAdapter ia = new ImageAdapter(SaunaPhotosActivity.this, urls, new ImageDownloader(), imageWidth, imageHeight);                     
                    GridView gridview = (GridView) findViewById(R.id.gridview);
                    gridview.setAdapter(ia);                                   
                    
                    gridview.setOnItemClickListener(new OnItemClickListener() {            
						public void onItemClick(AdapterView<?> parent, View v, int position, long id) {		
							 String filename = String.valueOf(urls.get(position).hashCode()) + ".png";
							 File f = new File(ImageDownloader.getCacheDirectory(v.getContext()), filename);				 						
							 if (f.exists()) {
								 Uri uri = Uri.fromFile(f);
								 Intent intent = new Intent(Intent.ACTION_VIEW);
								 Uri hacked_uri = Uri.parse("file://" + uri.getPath());								
								 intent.setDataAndType(hacked_uri, "image/*");
								 startActivity(intent);  
							 } else {
								 Toast.makeText(SaunaPhotosActivity.this, "No file", Toast.LENGTH_SHORT).show();									
							 }
						}
                    });                    
                } else {                   
                    TextView header = (TextView) findViewById(R.id.gallery_header);
                	header.setText(getResources().getString(R.string.no_data));
                }
			} catch (JSONException e) {				
			}   	  			
	    }
    }     
}
