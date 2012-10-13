package com.foxitsdk.exception;

public class errorException extends Exception {

	private static final long serialVersionUID = 5254980548302558473L;

	public errorException(){
		super();
	}
	
	public errorException(String message){
		super(message);
	}
}
