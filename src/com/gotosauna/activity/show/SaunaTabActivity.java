package com.gotosauna.activity.show;

import java.util.ArrayList;

import com.gotosauna.R;
import com.gotosauna.activity.map.SaunaMapActivity;
import com.gotosauna.activity.photo.SaunaPhotosActivity;
import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class SaunaTabActivity extends TabActivity {
	
	GlobalStore globalStore;
	ArrayList<City> cities = new ArrayList<City>();
	private static final String PHOTOS_SAUNAS_URL = "http://go-to-sauna.ru/sauna_photos/";
	private static final String SHOW_SAUNA_URL = "http://go-to-sauna.ru/saunas/";
	
	private static final String URL_KEY = "url";
	private static final String SAUNA_ID_KEY = "saunaId";
	
	private String saunaId;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	  
	    setContentView(R.layout.sauna_tabs);	    
	    Bundle extras = getIntent().getExtras(); 
  		saunaId = extras.getString(SAUNA_ID_KEY);  		
		initUI();
		
	    final TabHost tabHost = getTabHost();	  
	    changeColor(tabHost);	               
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){
        	public void onTabChanged(String tabId) {
        		changeColor(tabHost);        		     	   
        	 }
        });  		
	}
	
	private void changeColor(TabHost tabHost){
	    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
	    { 
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
	    } 		
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));  		
	}
	
	public void initUI() {	 
		Resources res = getResources();
		 
	    TabHost tabHost = getTabHost();  
	    TabHost.TabSpec spec;  
	    Intent intent;  
	  
	    intent = new Intent().setClass(getApplicationContext(), SaunaShowActivity.class);		   	
	    intent.putExtra(URL_KEY, prepareShowSaunaUrl());
	    spec = tabHost.newTabSpec("description").setIndicator(getResources().getString(R.string.description), 
	    		res.getDrawable(R.drawable.ic_tab_desc)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(getApplicationContext(), SaunaPhotosActivity.class);
		intent.putExtra(URL_KEY, prepareShowPhotoUrl());
	    spec = tabHost.newTabSpec("photo").setIndicator(getResources().getString(R.string.photo_gallery), 
	    		res.getDrawable(R.drawable.ic_tab_photo)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(getApplicationContext(), SaunaMapActivity.class);	    
		intent.putExtra(URL_KEY,  prepareShowSaunaUrl());
		
	    spec = tabHost.newTabSpec("map").setIndicator(getResources().getString(R.string.show_on_map), 
	    		res.getDrawable(R.drawable.ic_tab_map)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);		
	}	
	
    public String prepareShowPhotoUrl(){    	
    	StringBuffer sb = new StringBuffer(PHOTOS_SAUNAS_URL);
    	sb.append(saunaId);
    	sb.append("?json=true");	    	       
    	return sb.toString();
    }	
    
    public String prepareShowSaunaUrl(){	    		
    	StringBuffer sb = new StringBuffer(SHOW_SAUNA_URL);
    	sb.append(saunaId);
    	sb.append("?json=true");	    	       
    	return sb.toString();
    }    
}
