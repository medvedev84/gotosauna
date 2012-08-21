package com.gotosauna;

import java.util.ArrayList;

import com.gotosauna.core.City;
import com.gotosauna.util.GlobalStore;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {

	GlobalStore globalStore;
	ArrayList<City> cities;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);  
                
        TextView about = (TextView) findViewById(R.id.textViewAbout);
        about.setMovementMethod(LinkMovementMethod.getInstance());
                
        final Button buttonSearch = (Button) findViewById(R.id.buttonSendEmail);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {                	
            	sendEmail();
            }
        });        
    }
        
    private void sendEmail(){    	
        Intent emailIntent = new Intent(Intent.ACTION_SEND); 
        emailIntent.setType("message/rfc822"); 
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { getResources().getString(R.string.gotosauna_email) });
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_using)));    	    	
    }        
}
