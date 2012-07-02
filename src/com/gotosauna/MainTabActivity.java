package com.gotosauna;

import com.gotosauna.activity.list.CityListActivity;
import com.gotosauna.activity.search.SaunaSearchActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class MainTabActivity extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	  
	    setContentView(R.layout.main_tabs);
	    initUI();
	    
	    final TabHost tabHost = getTabHost();	  
	    changeColor(tabHost);	               
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){
        	public void onTabChanged(String tabId) {
        		changeColor(tabHost);        		     	   
        	 }
        });        
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	  
	    switch (item.getItemId()) {
	        case R.id.about:
            	Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
    			startActivity(intent);    
	            return true;
	        case R.id.settings:
            	Intent intent2 = new Intent(getApplicationContext(), SettingsActivity.class);
    			startActivity(intent2);   
	            return true;            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
	
	public void initUI() {	   
	    Resources res = getResources();
		  
	    TabHost tabHost = getTabHost();  
	    TabHost.TabSpec spec;  
	    Intent intent;  
	  
	    intent = new Intent().setClass(getApplicationContext(), SaunaSearchActivity.class);		   	
	    spec = tabHost.newTabSpec("search").setIndicator(getResources().getString(R.string.menu_search), 
	    		res.getDrawable(R.drawable.ic_tab_search)).setContent(intent);
	    tabHost.addTab(spec);	    	    

	    intent = new Intent().setClass(getApplicationContext(), CityListActivity.class);
	    spec = tabHost.newTabSpec("cities").setIndicator(getResources().getString(R.string.menu_cities), 
	    		res.getDrawable(R.drawable.ic_tab_city)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);		    	   
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
}
