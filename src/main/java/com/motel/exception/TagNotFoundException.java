package com.motel.exception;

public class TagNotFoundException extends Exception{

	public TagNotFoundException(String name) {
		super(name);
	}
}
