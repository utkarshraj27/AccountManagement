package com.pecunia.transaction.exception;

public class InSufficientBalanceException extends RuntimeException {
	
	public InSufficientBalanceException(String msg)
	{
		super(msg);
	}

}
