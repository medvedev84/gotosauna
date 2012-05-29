package com.gotosauna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GotosaunaActivity extends Activity {
	private static final String URL_KEY="url";	   
	private static final int ACTIVITY_SEARCH=0;
	private static final String LIST_SAUNAS_URL = "http://go-to-sauna.ru/saunas?json=true";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
                
        initSeekBar(R.id.seekBarPrice, R.id.textViewPrice, R.string.price, R.string.less_than, R.string.rub_per_hour);
        initSeekBar(R.id.seekBarSize, R.id.textViewSize, R.string.size, R.string.great_than, R.string.persons);

        final Button buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener()
        	{
                public void onClick(View v)
                {                	
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {                    	                    	                    	                    	
                        Intent intent = new Intent(v.getContext(), SaunaListActivity.class);
                        intent.putExtra(URL_KEY, prepareUrl());
                        startActivityForResult(intent, ACTIVITY_SEARCH);                              	
                    } else {
                    	Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                    
                }
            });
    }
    
    private String prepareUrl(){
    	SeekBar seekBarSize = (SeekBar) findViewById(R.id.seekBarSize);
    	int size = seekBarSize.getProgress();    	
    	SeekBar seekBarPrice = (SeekBar) findViewById(R.id.seekBarPrice);
    	int price = seekBarPrice.getProgress();    	
    	StringBuffer sb = new StringBuffer(LIST_SAUNAS_URL);
    	sb.append("&q%5Bsauna_items_capacity_gteq=" + size);
    	sb.append("&q%5Bsauna_items_min_price_lteq=" + price);
    	sb.append("&q%5Baddress_city_id_eq=1");        
    	return sb.toString();
    }
    
    private void initSeekBar(int seekBarId, final int textViewId, final int stringId, final int stringDelimeterId, final int stringCountId){
    	SeekBar seekBar = (SeekBar) findViewById(seekBarId);
    	setTextView(textViewId, stringId, stringDelimeterId, seekBar.getProgress(), stringCountId);
    	seekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
		{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				setTextView(textViewId, stringId, stringDelimeterId, progress, stringCountId);		 
			}

			public void onStartTrackingTouch(SeekBar arg0) {
			}

			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});    	
    }
    
    private void setTextView(int textViewId, int stringId, int stringDelimeterId, int progress, int stringCountId){    	
		TextView textView = (TextView) findViewById(textViewId);			
		textView.setText(getResources().getString(stringId) + " " + getResources().getString(stringDelimeterId) + " " + progress + " " + getResources().getString(stringCountId));    
    }
}