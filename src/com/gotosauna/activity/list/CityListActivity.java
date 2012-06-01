package com.gotosauna.activity.list;

import java.util.ArrayList;
import java.util.HashMap;

import com.gotosauna.R;
import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CityListActivity extends ListActivity  {
	
	private static final String URL_KEY = "url";
		
	private static final int ACTIVITY_SHOW = 1;
	
	private static final String LIST_SAUNAS_URL = "http://go-to-sauna.ru/saunas?json=true&q%5Baddress_city_id_eq=";
		
	private HashMap<String, City> cities = new HashMap<String, City>();
	
	City selectedCity;
	
	ArrayAdapter<String> adapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
  	    setContentView(R.layout.sauna_list);
  	    
  	    GlobalStore globalStore = ((GlobalStore) getApplicationContext());
  	    
  	    ArrayList<City> cityArray = globalStore.getCities();
  	  	ArrayList<String> listItems = new ArrayList<String>();
        for (City city : cityArray) {                      
            listItems.add(city.getName());   
            cities.put(city.getName(), city);     
        }
        
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.city_item, listItems);
		setListAdapter(adapter); 
		
	  		ListView lv = getListView();
			lv.setTextFilterEnabled(true);		  			
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
					selectedCity = cities.get((String)((TextView) view).getText());  					
					String url = prepareUrl();
                Intent intent = new Intent(getApplicationContext(), SaunaListActivity.class);
                intent.putExtra(URL_KEY, url);
                startActivityForResult(intent, ACTIVITY_SHOW);                    
				}
			});    	    
	}
	    
    public String prepareUrl(){	    		
    	StringBuffer sb = new StringBuffer(LIST_SAUNAS_URL);
    	sb.append(selectedCity.getId());    	      
    	return sb.toString();
    }   
}