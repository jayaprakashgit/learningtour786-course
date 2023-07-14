package com.interlan.test.dp;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Visitable> shoppingCartList = new ArrayList<>();
		shoppingCartList.add(new Book("jb1","Java"));
		shoppingCartList.add(new CD("cd1","movie-1"));
		
		Visitor visitor = new PostageVisitor();
		for (Visitable visitable : shoppingCartList) {
			visitable.accept(visitor);
		}
		
		for (Visitable visitable : shoppingCartList) {
			System.out.println(((PostageDetails)visitable).getPostageAmount());
		}
		
	}

}
