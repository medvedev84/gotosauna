package com.gotosauna.activity.show;

import java.util.ArrayList;

import com.gotosauna.R;
import com.gotosauna.activity.map.SaunaOSMapActivity;
import com.gotosauna.activity.photo.SaunaPhotosActivity;
import com.gotosauna.core.City;
import com.gotosauna.core.Sauna;
import com.gotosauna.util.Constants;
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
		
	TabHost tabHost;
	 
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	  
	    setContentView(R.layout.sauna_tabs);	    

        Sauna sauna = (Sauna) getIntent().getSerializableExtra(Constants.SAUNA_KEY);            
        Bundle extras = getIntent().getExtras(); 
  		int tabNum = extras.getInt(Constants.TAB_NUM_ID);
  		int resolutionId = extras.getInt(Constants.RESOLUTION_ID_KEY);
  		initUI(sauna, tabNum, resolutionId);  		  	
   
	    tabHost = getTabHost();	  
	    changeColor();	               
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){
        	public void onTabChanged(String tabId) {
        		changeColor();        		     	   
        	 }
        }); 
	
	}
	
	private void changeColor(){
	    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
	    { 
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
	    } 		
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));  		
	}
	
	public void initUI(Sauna sauna, int tabNum, int resolutionId) {	 
		Resources res = getResources();
		 
	    TabHost tabHost = getTabHost();  
	    TabHost.TabSpec spec;  
	    Intent intent;  
	  
	    intent = new Intent().setClass(getApplicationContext(), SaunaShowActivity.class);		   	
	    intent.putExtra(Constants.SAUNA_KEY, sauna);
	    spec = tabHost.newTabSpec("description").setIndicator(getResources().getString(R.string.description), 
	    		res.getDrawable(R.drawable.ic_tab_desc)).setContent(intent);
	    tabHost.addTab(spec);
	   
	    intent = new Intent().setClass(getApplicationContext(), SaunaItemsShowActivity.class);
	    intent.putExtra(Constants.SAUNA_KEY, sauna);  
	    spec = tabHost.newTabSpec("view").setIndicator(getResources().getString(R.string.view_sauna_items), 
	    		res.getDrawable(R.drawable.ic_tab_more)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(getApplicationContext(), SaunaPhotosActivity.class);
	    intent.putExtra(Constants.SAUNA_KEY, sauna);
	    intent.putExtra(Constants.RESOLUTION_ID_KEY, resolutionId);	    
	    spec = tabHost.newTabSpec("photo").setIndicator(getResources().getString(R.string.photo_gallery), 
	    		res.getDrawable(R.drawable.ic_tab_photo)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(getApplicationContext(), SaunaOSMapActivity.class);		    
	    intent.putExtra(Constants.SAUNA_KEY, sauna);
	    spec = tabHost.newTabSpec("map").setIndicator(getResources().getString(R.string.show_on_map), 
	    		res.getDrawable(R.drawable.ic_tab_map)).setContent(intent);
	    tabHost.addTab(spec);	  
	    
	    tabHost.setCurrentTab(tabNum);		
	}	
    
    public void switchTab(int tab){
        tabHost.setCurrentTab(tab);
    }       
}
