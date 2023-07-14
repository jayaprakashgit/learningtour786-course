package com.interlan.test.dp;

public class CD extends PostageDetails implements Visitable{

	private String cdId;
	private String cdName;
	
	public CD(String cdId, String cdName) {
		super();
		this.cdId = cdId;
		this.cdName = cdName;
	}
	
	public String getCdId() {
		return cdId;
	}
	public void setCdId(String cdId) {
		this.cdId = cdId;
	}
	public String getCdName() {
		return cdName;
	}
	public void setCdName(String cdName) {
		this.cdName = cdName;
	}
	
	@Override
	public void accept(Visitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

}
