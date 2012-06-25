package com.gotosauna.activity.show;

import java.util.ArrayList;

import com.gotosauna.R;
import com.gotosauna.core.Sauna;
import com.gotosauna.core.SaunaItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class SaunaItemsShowActivity extends Activity  {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sauna_items_show);
  		
  		Sauna sauna = (Sauna) getIntent().getSerializableExtra("Sauna");  		
  	    ArrayList<String> groups = new ArrayList<String>();
        ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();
             
        for (int i = 0; i < sauna.getItems().size(); i++) {
            SaunaItem item = sauna.getItems().get(i);
            groups.add(item.getName());            
            ArrayList<String> content = new ArrayList<String>();
            content.add(item.getDescription());
            content.add(item.getPrice());
            content.add(item.getCapacity());
            children.add(content);
        }                   
        initExpandable(groups, children); 
	}	
	
    private void initExpandable(ArrayList<String> groups, ArrayList<ArrayList<String>> children){
    	ExpandableListView listView = (ExpandableListView) findViewById(R.id.sauna_items);
    	listView.setGroupIndicator(null);        
        ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), groups, children);
        listView.setAdapter(adapter);	      
        if (groups.size() == 1) {
        	listView.expandGroup(0);
        }
    }  			   
}