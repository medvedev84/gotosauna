package com.gotosauna.core;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SaunaItem implements Serializable {

	private String name;
	private String description;
	private String price;	
	private String capacity;
	
	public SaunaItem(){}
		
	public SaunaItem(String name, String description, String price, String capacity){
		this.name = name;
		this.description = description;
		this.price = price;
		this.capacity = capacity;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}		    
}
