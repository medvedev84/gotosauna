package com.gotosauna.activity.list;

import java.util.ArrayList;
import java.util.HashMap;

import com.gotosauna.R;
import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CityListActivity extends ListActivity  {
	
	private static final String URL_KEY = "url";
	private static final String CITY_ID_KEY = "cityId";	
		
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
				intent.putExtra(CITY_ID_KEY, selectedCity.getId());
				startActivityForResult(intent, ACTIVITY_SHOW);                    
			}
		}); 

  		EditText filterText = (EditText) findViewById(R.building_list.search_box);
  	    filterText.addTextChangedListener(filterTextWatcher);		
	}
	    
    public String prepareUrl(){	    		
    	StringBuffer sb = new StringBuffer(LIST_SAUNAS_URL);
    	sb.append(selectedCity.getId());    	      
    	return sb.toString();
    }   
    
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (adapter != null) {
            	adapter.getFilter().filter(s);	
            }        	
        }

		public void afterTextChanged(Editable arg0) {}
    };    
}