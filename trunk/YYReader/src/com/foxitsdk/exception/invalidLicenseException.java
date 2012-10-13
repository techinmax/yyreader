package com.foxitsdk.exception;

public class invalidLicenseException extends Exception {

	private static final long serialVersionUID = -3405776690109175868L;

	public invalidLicenseException(){
		super();
	}
	
	public invalidLicenseException(String message){
		super(message);
	}
}
