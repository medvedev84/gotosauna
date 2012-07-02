package com.gotosauna.activity.map;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.gotosauna.R;
import com.gotosauna.core.Sauna;

public class SaunaMapActivity extends MapActivity {		
	MapView mapView;
	List<Overlay> mapOverlays;
	SaunaOverlay itemizedoverlay;	
		
	@Override
	protected boolean isRouteDisplayed() {		
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);	 
	    
	    Sauna sauna = (Sauna) getIntent().getSerializableExtra("Sauna");  		
		initUI(sauna);
		    
	}	  
	
	private void initUI(Sauna sauna) {
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
}
