package com.foxitsdk.exception;

public class statusException extends Exception {

	private static final long serialVersionUID = 4604677950781984397L;

	public statusException(){
		super();
	}
	
	public statusException(String message){
		super(message);
	}
}
