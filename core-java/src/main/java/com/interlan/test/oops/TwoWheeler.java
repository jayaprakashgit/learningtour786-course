package com.interlan.test.oops;

public class TwoWheeler extends Vehicle {

	private String type;
	
	public TwoWheeler(int id, String name) {
		super(id, name);
		// TODO Auto-generated constructor stub
	}

	public TwoWheeler(int id, String name, String type) {
		super(id, name);
		this.type=type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TwoWheeler [type=" + type + ", getId()=" + getId() + ", getName()=" + getName() + "]";
	}
	
}
