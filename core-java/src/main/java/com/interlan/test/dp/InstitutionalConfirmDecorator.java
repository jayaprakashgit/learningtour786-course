package com.interlan.test.dp;

public class InstitutionalConfirmDecorator extends ConfirmDecorator {

	private iTradeConfirm tradeConfirm;
	
	public InstitutionalConfirmDecorator(iTradeConfirm tradeConfirm) {
		// TODO Auto-generated constructor stub
		this.tradeConfirm = tradeConfirm;
	}
	
	@Override
	public String getConfirmData() {
		// TODO Auto-generated method stub
		String confirmData = tradeConfirm.getConfirmData();
		confirmData = confirmData + "\n This is for Institutional Clients";
		return confirmData;
	}
}
