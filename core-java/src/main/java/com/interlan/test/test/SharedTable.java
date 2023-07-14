package com.interlan.test.test;

public class SharedTable {

	private String cookie;

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	
	public String eatCookie(){
		return cookie;
	}
	
	public boolean isEmpty(){
		return (cookie==null)?true:false;
	}
	
	
}
