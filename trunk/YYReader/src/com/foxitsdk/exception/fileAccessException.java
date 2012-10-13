package com.foxitsdk.exception;

public class fileAccessException extends Exception {

	private static final long serialVersionUID = -5277884499218555829L;

	public fileAccessException(){
		super();
	}
	
	public fileAccessException(String message){
		super(message);
	}
}
