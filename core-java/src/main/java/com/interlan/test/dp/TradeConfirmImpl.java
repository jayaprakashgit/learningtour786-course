package com.interlan.test.dp;

public class TradeConfirmImpl implements iTradeConfirm{

	private String confirmData;
	
	public TradeConfirmImpl(String confirmData) {
		// TODO Auto-generated constructor stub
		this.confirmData = confirmData;
	}
	
	@Override
	public String getConfirmData() {
		// TODO Auto-generated method stub
		return confirmData;
	}
}
