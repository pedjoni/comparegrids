package com.pjcode.domain;

public enum OrderStatus {
	NEW(1), PROCESSED(2), CANCELLED(3), ERRORED(4);
	
	private int code;
	
	private OrderStatus(int i) {
		this.code = i;
	}
	
	public int getCode() {
		return this.code;
	}
}
