package com.gotosauna;

import java.util.ArrayList;

import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {	
	private static final String CITY_KEY="cityId";	  
	public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        GlobalStore globalStore = ((GlobalStore) getApplicationContext());
        ArrayList<City> cityArray = globalStore.getCities();
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int cityId = Integer.parseInt(settings.getString(CITY_KEY, "0"));         
            
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);       
        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.city_item, cityArray);                
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);              
        spinner.setAdapter(adapter);  
        spinner.setSelection(cityId - 1); 
        
        final Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener()
        	{
                public void onClick(View v)
                {                	
                	City city = (City) spinner.getSelectedItem();                	 
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(CITY_KEY, city.getId());
                    editor.commit();     
                    
                  	Toast.makeText(getApplicationContext(), getResources().getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
                    
                }
            });        
    }
    
}
