package com.gotosauna;

import java.util.ArrayList;

import com.gotosauna.core.City;
import com.gotosauna.util.Constants;
import com.gotosauna.util.GlobalStore;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        GlobalStore globalStore = ((GlobalStore) getApplicationContext());
        ArrayList<City> cityArray = globalStore.getCities();
        
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        int cityId = Integer.parseInt(settings.getString(Constants.CITY_ID_KEY, "0"));
        int resolutionId = settings.getInt(Constants.QUALITY_KEY, 0);         
            
        final Spinner spinnerCity = (Spinner) findViewById(R.id.spinnerCity);       
        ArrayAdapter<String> adapterCity = new ArrayAdapter(getApplicationContext(), R.layout.city_item, cityArray);                
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);              
        spinnerCity.setAdapter(adapterCity);  
        spinnerCity.setSelection(cityId - 1); 
        
        final Spinner spinnerResolution = (Spinner) findViewById(R.id.spinnerResolution);          	
        ArrayAdapter adapterResolution = ArrayAdapter.createFromResource(this, R.array.resolutions_ext, android.R.layout.simple_spinner_item);                       
        adapterResolution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);              
        spinnerResolution.setAdapter(adapterResolution);  
        spinnerResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {                
			    int selectedResolution = spinnerResolution.getSelectedItemPosition();        
				if (selectedResolution > 0) {
                	Toast.makeText(getApplicationContext(), getResources().getString(R.string.resolution_info), Toast.LENGTH_SHORT).show();
                }
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// nothing				
			}
        	
        });
        spinnerResolution.setSelection(resolutionId);         
        
                
        final Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener()
        	{
                public void onClick(View v)
                {                	
                	City city = (City) spinnerCity.getSelectedItem();                	 
                    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Constants.CITY_ID_KEY, city.getId());
                    
                    int selectedResolution = spinnerResolution.getSelectedItemPosition();                                          
                    editor.putInt(Constants.QUALITY_KEY, selectedResolution);                                        
                    editor.commit();     
                  	
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.settings_saved), Toast.LENGTH_LONG).show();
                    
                }
            });        
    }  
}
