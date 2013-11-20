package com.geeklub.vass.model;



public class SmsInfo {
	private String address;
	private Long date;
	private int type;
	private String body;
	
	
	public SmsInfo(){
		
	}


	public SmsInfo(String address, Long date, int type, String body) {
		super();
		this.address = address;
		this.date = date;
		this.type = type;
		this.body = body;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Long getdate() {
		return date;
	}


	public void setdate(Long date) {
		this.date = date;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}
	
	
	

}
