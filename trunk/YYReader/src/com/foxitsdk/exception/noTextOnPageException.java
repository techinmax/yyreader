package com.foxitsdk.exception;

public class noTextOnPageException extends Exception {
	
	private static final long serialVersionUID = 8907582728504500920L;

	public noTextOnPageException(){
		super();
	}
	
	public noTextOnPageException(String message){
		super(message);
	}
}
