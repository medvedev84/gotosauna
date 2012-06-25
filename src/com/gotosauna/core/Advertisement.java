package com.gotosauna.core;

public class Advertisement {
	
	private String cityId;
	private String companyName;
	private String description;
	private String phoneNumber;
	
	public Advertisement(){}
	
	public Advertisement(String cityId, String companyName, String description, String phoneNumber){
		this.cityId = cityId;
		this.companyName = companyName;
		this.description = description;
		this.phoneNumber = phoneNumber;		
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
