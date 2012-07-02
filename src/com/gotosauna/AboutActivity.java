package com.gotosauna;

import java.util.ArrayList;

import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity {

	GlobalStore globalStore;
	ArrayList<City> cities;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);                        
    }
    
}
