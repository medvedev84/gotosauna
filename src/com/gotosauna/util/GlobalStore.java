package com.gotosauna.util;

import java.util.ArrayList;

import com.gotosauna.core.Advertisement;
import com.gotosauna.core.City;

import android.app.Application;

public class GlobalStore extends Application {
	  
	private ArrayList<City> cities;
	private ArrayList<Advertisement> advertisements;
	private int screenWidth;
	private int screenHeight;

	public ArrayList<City> getCities() {
		return cities;
	}

	public void setCities(ArrayList<City> cities) {
		this.cities = cities;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public ArrayList<Advertisement> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(ArrayList<Advertisement> advertisements) {
		this.advertisements = advertisements;
	}
	
	public ArrayList<Advertisement> getAdvertisementsByCity(String cityId) {
		ArrayList<Advertisement> result = new ArrayList<Advertisement>();		
		for (Advertisement adv : advertisements) {
			if (adv.getCityId().equals(cityId)) {
				result.add(adv);	
			}			
		}		
		return result;
	}	
}
