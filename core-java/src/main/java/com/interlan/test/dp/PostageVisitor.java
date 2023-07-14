package com.interlan.test.dp;

public class PostageVisitor implements Visitor{
	
	@Override
	public void visit(Book book) {
		// TODO Auto-generated method stub
		book.setPostageAmount(100);
	}
	
	@Override
	public void visit(CD cd) {
		// TODO Auto-generated method stub
		cd.setPostageAmount(50);
	}
}
