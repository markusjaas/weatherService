package com.mj.weatherservice.model;

public class CustomError {
	private String errorMessage;
	
	public CustomError(String errorMessage){
		this.setErrorMessage(errorMessage);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
