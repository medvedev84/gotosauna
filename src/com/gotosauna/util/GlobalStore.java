package com.gotosauna.util;

import java.util.ArrayList;

import com.gotosauna.core.City;

import android.app.Application;

public class GlobalStore extends Application {
	  
	private ArrayList<City> cities;

	public ArrayList<City> getCities() {
		return cities;
	}

	public void setCities(ArrayList<City> cities) {
		this.cities = cities;
	}
}
