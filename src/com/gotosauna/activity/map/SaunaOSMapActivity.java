package com.gotosauna.activity.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import com.gotosauna.R;
import com.gotosauna.core.Sauna;

import android.app.Activity;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class SaunaOSMapActivity extends Activity {
	protected static final String PROVIDER_NAME = LocationManager.GPS_PROVIDER;
	     
    private MapView mapView;
    private MapController mapController;
	private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
	private ItemizedOverlay<OverlayItem> mMyLocationOverlay;
	private ResourceProxy mResourceProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {	    
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.osmap);	 
	    mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
	       
	    Sauna sauna = (Sauna) getIntent().getSerializableExtra("Sauna");  		
		initUI(sauna);	    	    
    }
    
	private void initUI(Sauna sauna) {
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setTileSource(TileSourceFactory.MAPNIK);
	    mapView.setBuiltInZoomControls(true);	  
        mapController = this.mapView.getController();
        mapController.setZoom(15);

        GeocoderNominatim geocoder = new GeocoderNominatim(getApplicationContext());
	    List<Address> addressList;
		try {
			String cityName = sauna.getAddress().substring(0, sauna.getAddress().indexOf(","));
			String address = revertAddress(sauna.getAddress());
			addressList = geocoder.getFromLocationName(address, 1);
			if(addressList != null && addressList.size() > 0)
			{
				addSaunaToMap(addressList.get(0), sauna);
			} else {							
				addressList = geocoder.getFromLocationName(cityName, 1);
				if(addressList != null && addressList.size() > 0)
				{
					addSaunaToMap(addressList.get(0), sauna);
				}
			}
		} catch (IOException e) {
		}		
	}

	private String revertAddress(String address){
		String[] addressArray = address.split(",");			
		String result = "";		
		for (int i = addressArray.length - 1; i >= 0; i--) {
			result += addressArray[i] + ", ";
		}	
		return result;
	}
	
	private void addSaunaToMap(Address address, Sauna sauna){
	    int lat = (int)(address.getLatitude() * 1000000);
	    int lng = (int)(address.getLongitude() * 1000000);
	    
	    GeoPoint point = new GeoPoint(lat, lng);
	    OverlayItem overlayitem = new OverlayItem(sauna.getName(), sauna.getPhoneNumber(), point);	    
	    items.add(overlayitem);
		this.mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
	        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
	                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
	                    Toast.makeText(SaunaOSMapActivity.this, item.mTitle + " " + item.mDescription, Toast.LENGTH_LONG).show();
	                    return true; 
	                }
	                public boolean onItemLongPress(final int index, final OverlayItem item) {
	                	Toast.makeText(SaunaOSMapActivity.this, item.mTitle + " " + item.mDescription ,Toast.LENGTH_LONG).show();
	                	return false;
	                }
		}, mResourceProxy);	      	   
		mapView.getOverlays().add(this.mMyLocationOverlay);			
	    mapController.setCenter(point);	    
		mapView.invalidate();	    	
	}	  
}