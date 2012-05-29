package com.gotosauna;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class SaunaPhotosActivity extends Activity {
	
	ImageDownloader downloader;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.sauna_photos_gallery);
        Gallery gallery = (Gallery) findViewById(R.id.gallery1);
        
        this.downloader = new ImageDownloader();
        
        final ArrayList<String> urls = new ArrayList<String>();
        urls.add("http://www.futuretechnology500.com/images/future-airplane.jpg");
        urls.add("http://media.defenseindustrydaily.com/images/AIR_Future_Lynx_Concept_Naval_lg.jpg");
        urls.add("http://3.bp.blogspot.com/_CHG2GRbeET8/TEajDSYl0kI/AAAAAAAAPzo/GSffdu26uls/s1600/future_city_downtown.jpg");
        urls.add("http://cdx.dexigner.com/article/20883/LAVA_Home_of_the_Future_02_thumb.jpg");
        urls.add("http://www.davinciinstitute.com/wp-content/uploads/2010/12/City-of-the-Future-484.jpg");
        urls.add("http://2.bp.blogspot.com/-HMgdqt-8tWE/TnyM6LS4vqI/AAAAAAAACmw/RYOd6-JsBr4/s1600/Future+City+wallpaper_4.jpg");
        urls.add("http://www.chasepratt.com/wp-content/uploads/2010/05/5af1c364092969c1e5ea8c2df8f024f9.jpg");
        urls.add("http://3.bp.blogspot.com/_TGS61dlat8U/SjEviydcakI/AAAAAAAAArw/zaOwgWQ8_54/s400/Water+resort+04a.jpg");
        urls.add("http://www.project-humanity-earth.org/yahoo_site_admin/assets/images/city-2.226121338_std.jpg");
        urls.add("http://images.china.cn/attachement/jpg/site1007/20100512/001ec94a26ba0d54527b51.jpg");
        
        ImageAdapter ia = new ImageAdapter(SaunaPhotosActivity.this, urls, downloader);
        gallery.setAdapter(ia);        
        gallery.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {   
            	ImageView imageView = (ImageView) findViewById(R.id.image1);    
            	
            	File f = new File(imageView.getContext().getCacheDir(), String.valueOf(urls.get(position).hashCode()));              	            
                //imageView.setImageResource( R.drawable.sample_2);
                imageView.setImageBitmap(downloader.imageCache.get(f.getPath()));
                
   	    	 	
            }
        });        
    }	    
}
