package com.gotosauna.activity.photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gotosauna.R;
import com.gotosauna.activity.show.SaunaTabActivity;
import com.gotosauna.core.Sauna;
import com.gotosauna.util.Constants;
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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

public class SaunaPhotosActivity extends Activity {
	private static final int MAX_PHOTO_ARRAY_SIZE = 6;	
	
	private int imageWidth = 100;
	private int imageHeight = 100;
	
	private boolean isFirstTime = true;	
	private Sauna sauna;
	private int resolutionId;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sauna_photos_grid);                    

  		GlobalStore globalStore = ((GlobalStore) getApplicationContext());       	  
  		imageWidth = globalStore.getScreenWidth() / 2 - 10;
  		imageHeight = imageWidth;  	
  	  	
  		sauna = (Sauna) getIntent().getSerializableExtra(Constants.SAUNA_KEY); 
  		
  		Bundle extras = getIntent().getExtras(); 
  		resolutionId= extras.getInt(Constants.RESOLUTION_ID_KEY);

  		final String photoUrl = prepareShowPhotoUrl(sauna.getId(), resolutionId);
  		runOnUiThread(new Runnable() {
 		     public void run() {
 		    	new DownloadWebpageText().execute(photoUrl);
 		    }
 		}); 
    }	
    
    public String prepareShowPhotoUrl(String saunaId, int resolutionId){  
    	StringBuffer sb = new StringBuffer(Constants.PHOTOS_SAUNAS_URL);
    	sb.append(saunaId);
    	sb.append("?json=true");                      
    	sb.append(calculateResolution(Integer.parseInt(saunaId), resolutionId));
    	return sb.toString();
    }	    
    
    private String calculateResolution(int saunaId, int resolution){
    	String resolutionParameter = "&photo_type=";
    	if (saunaId >= 295) {
			switch (resolution) {
				case 0: {
					resolutionParameter += "thumb";
					break;
				}
    			case 1: {
    				resolutionParameter += "size200";
    				break;
    			}    
    			case 2: {
    				resolutionParameter += "size600";
    				break;
    			} 					
				case 3: {
					resolutionParameter += "original";
					break;
				}       
				default: {
					resolutionParameter += "thumb";
					break;					
				}
			}  	    		
    	} else {
			switch (resolution) {
				case 0: {
					resolutionParameter += "thumb";
					break;
				}
				case 1: {
					resolutionParameter += "original";
					break;
				}   
    			case 2: {
    				resolutionParameter += "original";
    				break;
    			} 					
				case 3: {
					resolutionParameter += "original";
					break;
				}       
				default: {
					resolutionParameter += "thumb";
					break;					
				}				
			}  	 	    		
    	}  	
    	return resolutionParameter;
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
                    initSpinner();            	
                } else {                      	   
                	Toast.makeText(getApplicationContext(), getResources().getString(R.string.foto_is_absent), Toast.LENGTH_LONG).show();         		   
                }
			} catch (JSONException e) {				
			}   	  			
	    }
	    
	    private void initSpinner(){	    	
            final Spinner spinnerResolution = (Spinner) findViewById(R.id.spinnerResolution); 
        	spinnerResolution.setVisibility(View.VISIBLE);
        	
            ArrayAdapter adapterResolution = 
            		Integer.parseInt(sauna.getId()) >= 295 ?             		
            		ArrayAdapter.createFromResource(getApplicationContext(), R.array.resolutions_ext, android.R.layout.simple_spinner_item) :
            		ArrayAdapter.createFromResource(getApplicationContext(), R.array.resolutions, android.R.layout.simple_spinner_item);
            		
            adapterResolution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);              
            spinnerResolution.setAdapter(adapterResolution);
           
            
            spinnerResolution.setSelection( Integer.parseInt(sauna.getId()) >= 295 || resolutionId <= 1 ? resolutionId : 1);
            
            spinnerResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
    			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {      
    				int selectedResolution = spinnerResolution.getSelectedItemPosition();        				
    				if (!isFirstTime) {
        				finish();        				
        			    Intent intent = new Intent(getApplicationContext(), SaunaTabActivity.class);
      	                intent.putExtra(Constants.SAUNA_KEY, sauna);
      	                intent.putExtra(Constants.TAB_NUM_ID, 2);
      	                intent.putExtra(Constants.RESOLUTION_ID_KEY, selectedResolution);      	                      	              
      	                startActivityForResult(intent, 1);                           					
    				}
    				isFirstTime = false;
    			}
    			
    			public void onNothingSelected(AdapterView<?> arg0) {}            	
            });	    	
	    }
    }     
}
