package com.gotosauna.activity.photo;

import java.util.ArrayList;

import com.gotosauna.util.ImageDownloader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> items;
    ImageDownloader downloader;
      
    public ImageAdapter(Context c, ArrayList<String> items, ImageDownloader downloader) {
    	mContext = c;
    	this.items = items;
    	this.downloader = downloader;    	
    }

    public int getCount() {
    	 return items.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
		String url = items.get(position);        
        ImageView imageView = new ImageView(mContext);               
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setPadding(8, 8, 8, 8);        
        downloader.download(url, imageView);        
        return imageView;
    }

}