package com.foxitsdk.exception;

public class searchNotFoundException extends Exception {

	private static final long serialVersionUID = 4946885427679704403L;

	public searchNotFoundException(){
		super();
	}
	
	public searchNotFoundException(String message){
		super(message);
	}
}
