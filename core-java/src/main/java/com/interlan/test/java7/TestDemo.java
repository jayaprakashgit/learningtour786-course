package com.interlan.test.java7;


class SuggestionDemo{
	private void onSelection(){
		System.out.println("Item selected");
	}
	public void onRemoveSelection(){
		System.out.println("Item removed");
	}
}

public class TestDemo {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SuggestionDemo officeSugg = new SuggestionDemo()
		{
			public void onSelection(){
				System.out.println("office sels");
			}
			public void onRemoveSelection(){
				onSelection();
				System.out.println("office removed");
			}
		};
		
		officeSugg.onRemoveSelection();
	}

}
