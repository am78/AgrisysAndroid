package com.anteboth.agrisys;

public class SystemException extends Exception {

	private static final long serialVersionUID = -399526034315943934L;

	public SystemException(Throwable e) {
		super(e);
	}
	
	public SystemException() {
		super();
	}

}
