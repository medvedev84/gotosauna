package com.gotosauna.activity.photo;

import java.util.ArrayList;

import com.gotosauna.R;
import com.gotosauna.util.ImageDownloader;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int itemBackground;
    ArrayList<String> items;
    ImageDownloader downloader;

    public ImageAdapter(Context c, ArrayList<String> items, ImageDownloader downloader) {
    	mContext = c;
    	this.items = items;
    	this.downloader = downloader;
        //---setting the style---
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.Gallery1);
        itemBackground = a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 0);
        a.recycle();   
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
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
        imageView.setBackgroundResource(itemBackground);
        downloader.download(url, imageView);        
        return imageView;

    }

}