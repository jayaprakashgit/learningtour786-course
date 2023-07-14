package com.interlan.test.dp;

public class TradeConfirmGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		iTradeConfirm tradeConfirm = new TradeConfirmImpl("This is to confirm the trade transaction");
		ConfirmDecorator confirmDecorator = new InstitutionalConfirmDecorator(tradeConfirm);
		System.out.println(confirmDecorator.getConfirmData());
	}

}
