package com.gotosauna.core;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Sauna implements Serializable {
	
	private String id;
	private String name;
	private String phoneNumber;
	private String address;
	private String cityId;
	
	private ArrayList<SaunaItem> items = new ArrayList<SaunaItem>();
	
	public Sauna(){}
	
	public Sauna(String id, String name, String phoneNumber){
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;		
	}
	
	public Sauna(String id, String name, String phoneNumber, String address){
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ArrayList<SaunaItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<SaunaItem> items) {
		this.items = items;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
}