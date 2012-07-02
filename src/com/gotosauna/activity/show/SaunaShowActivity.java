package com.gotosauna.activity.show;

import java.util.ArrayList;

import com.gotosauna.R;
import com.gotosauna.core.Advertisement;
import com.gotosauna.core.Sauna;
import com.gotosauna.core.SaunaItem;
import com.gotosauna.util.GlobalStore;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SaunaShowActivity extends Activity  {	
	ArrayList<SaunaItem> sauna_items = new ArrayList<SaunaItem>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sauna_show);
				  				  			
  	  	Sauna sauna = (Sauna) getIntent().getSerializableExtra("Sauna");  	  	  	  	  
  		initSaunaUI(sauna);  
  	  
  		GlobalStore globalStore = ((GlobalStore) getApplicationContext());
  	  	ArrayList<Advertisement> advertisements = globalStore.getAdvertisementsByCity(sauna.getCityId());
  	  	
  	  	if (advertisements.size() >= 1) {
  	  		TextView additional = (TextView) findViewById(R.id.additional);
  	  		additional.setVisibility(0);  	  	
  	  		LinearLayout container1 = (LinearLayout) findViewById(R.id.container1);
  	  		container1.setVisibility(0);
  	  		initAdvUI(advertisements.get(0), R.id.company1, R.id.description1, R.id.buttonCallAdv1);  	  		
  	  	} 
  			  	  	
  	  	if (advertisements.size() >= 2) {
  	  		LinearLayout container2 = (LinearLayout) findViewById(R.id.container2);
  	  		container2.setVisibility(0);  	  		
  	  		initAdvUI(advertisements.get(1), R.id.company2, R.id.description2, R.id.buttonCallAdv2);
  	  	}
	}
	
	private void initSaunaUI(final Sauna sauna){  	   		
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(sauna.getName());
        
        TextView address = (TextView) findViewById(R.id.address);
        address.setText(sauna.getAddress());

        Button buttonCall = (Button) findViewById(R.id.buttonCall);
        buttonCall.setText(sauna.getPhoneNumber());	
        buttonCall.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {                	
		    	Intent intent = new Intent(Intent.ACTION_CALL);
		    	intent.setData(Uri.parse("tel:" + sauna.getPhoneNumber()));
		    	startActivity(intent);                       
            }
        });          
	}
	
	private void initAdvUI(final Advertisement adv, int company_id, int description_id, int button_id){  	   		
        TextView company = (TextView) findViewById(company_id);
        company.setText(adv.getCompanyName());
          	
        TextView description = (TextView) findViewById(description_id);
        description.setText(adv.getDescription());
        
  		Button buttonCallAdv = (Button) findViewById(button_id);
  		buttonCallAdv.setText(adv.getPhoneNumber()); 
  		buttonCallAdv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {                	
		    	Intent intent = new Intent(Intent.ACTION_CALL);
		    	intent.setData(Uri.parse("tel:" + adv.getPhoneNumber()));
		    	startActivity(intent);                      
            }
        });  
	}				
}