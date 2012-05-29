package com.gotosauna;

import java.util.ArrayList;

import android.R.string;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<String> groups;
	private ArrayList<ArrayList<String>> children;
	    
   public ExpandableListAdapter(Context context, ArrayList<String> groups,
            ArrayList<ArrayList<String>> children) {
        this.context = context;
        this.groups = groups;
        this.children = children;
    }
	   
	public Object getChild(int groupPosition, int childPosition) {
		   return children.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {      
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_layout, null);
        }
        String textConent = getTextContent(groupPosition, childPosition);
        TextView tv = (TextView) convertView.findViewById(R.id.tvChild);        
        tv.setText(textConent); 
        return convertView;
	}	
	
	public int getChildrenCount(int groupPosition) {
		 return children.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		 return groups.get(groupPosition);
	}

	public int getGroupCount() {
		  return groups.size();
	}

	public long getGroupId(int groupPosition) {
		 return groupPosition;
	}

   public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_layout, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group);
        return convertView;
    }

	public boolean hasStableIds() {
		 return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {		
		return false;
	}
	
	private String getTextContent(int groupPosition, int childPosition){
		String textConent = (String) getChild(groupPosition, childPosition);
		String result = ""; 
		switch (childPosition) {
			case 0:
				result = textConent;
				break;
			case 1:
				result = "Цена: от " + textConent + " руб./час";
				break;
			case 2:
				result = "Вместимость: до " + textConent + " чел.";
				break;		
		}
		return result;
	}
	

}
