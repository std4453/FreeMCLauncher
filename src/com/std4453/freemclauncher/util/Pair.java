package com.std4453.freemclauncher.util;

public class Pair<A,B> {
	protected A left;
	protected B right;
	public Pair() {
	}
	
	public Pair(A left, B right) {
		this.left = left;
		this.right = right;
	}

	public A getLeft() {
		return left;
	}

	public void setLeft(A left) {
		this.left = left;
	}

	public B getRight() {
		return right;
	}

	public void setRight(B right) {
		this.right = right;
	}
}
