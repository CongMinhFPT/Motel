package com.motel.exception;

public class BlogNotFoundException extends Exception{
	public BlogNotFoundException(String name) {
		super(name);
	}
}
