package com.gotosauna.activity.map;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.gotosauna.R;
import com.gotosauna.core.Sauna;
import com.gotosauna.util.JSONDownloader;

public class SaunaMapActivity extends MapActivity {
	private static final String URL_KEY = "url";
	
	MapView mapView;
	List<Overlay> mapOverlays;
	SaunaOverlay itemizedoverlay;
	Sauna sauna;
		
	@Override
	protected boolean isRouteDisplayed() {		
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);	 
	    
	    Bundle extras = getIntent().getExtras(); 
	    final String url = extras.getString(URL_KEY);
  	  		
  		runOnUiThread(new Runnable() {
  		     public void run() {
  		    	new DownloadWebpageText().execute(url);
  		    }
  		}); 
  		
	    
	}	  
	
	private void initUI() {
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    mapOverlays = mapView.getOverlays();	    
	    Drawable drawable = this.getResources().getDrawable(R.drawable.sauna);
	    itemizedoverlay = new SaunaOverlay(drawable, this);
	      		  		  		
  		Geocoder geocoder = new Geocoder(getApplicationContext());
	    List<Address> addressList;
		try {
			addressList = geocoder.getFromLocationName(sauna.getAddress(), 1);
			if(addressList != null && addressList.size() > 0)
			{
				addSaunaToMap(addressList.get(0), sauna);
			} else {
				String address = sauna.getAddress();
				String cityName = address.substring(0, address.indexOf(","));
				addressList = geocoder.getFromLocationName(cityName, 1);
				if(addressList != null && addressList.size() > 0)
				{
					addSaunaToMap(addressList.get(0), sauna);
				}
			}
		} catch (IOException e) {			
				AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
				dialog.setTitle(getResources().getString(R.string.application_error));
				dialog.setMessage(getResources().getString(R.string.could_not_find_location));				
				dialog.show();
		}		
	}
	
	private void addSaunaToMap(Address address, Sauna sauna){
	    int lat = (int)(address.getLatitude() * 1000000);
	    int lng = (int)(address.getLongitude() * 1000000);
	    GeoPoint point = new GeoPoint(lat, lng);
	    OverlayItem overlayitem = new OverlayItem(point, sauna.getName(), sauna.getPhoneNumber());
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	    
	    mapView.getController().setZoom(15);
	    mapView.getController().setCenter(point);		
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
					initUI();      
				} catch (JSONException e) {
					//TODO
				}   	 			 		
		    }
		    
	    } 		
}
