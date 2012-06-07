package com.gotosauna;

import com.gotosauna.activity.list.CityListActivity;
import com.gotosauna.activity.search.SaunaSearchActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	  
	    setContentView(R.layout.main_tabs);
	    initUI();
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
}
