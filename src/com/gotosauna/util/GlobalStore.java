package com.gotosauna.util;

import java.util.ArrayList;

import com.gotosauna.core.City;

import android.app.Application;

public class GlobalStore extends Application {
	  
	private ArrayList<City> cities;
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
}
