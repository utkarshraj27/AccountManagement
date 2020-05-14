package com.pecunia.transaction.service;

import com.pecunia.account.entity.Account;
import com.pecunia.account.exception.AccountException;
import com.pecunia.transaction.entity.Cheque;
import com.pecunia.transaction.entity.Transaction;

public interface TransactionManagementService {
	
	public double getBalance(Account account);
	
	public boolean updateBalance(Account account,double balance);
	
	public int generateTransactionId(Transaction transaction);
	
	public int generateChequeId(Cheque cheque);
	
	public Account getAccountById(String accountNumber) throws AccountException;
	
	public int creditUsingSlip(Transaction transaction);
	
	public int creditUsingCheque(Transaction transaction,Cheque cheque);
	
	public int debitUsingSlip(Transaction transaction) ;
	
	public int debitUsingCheque(Transaction transaction,Cheque cheque) ;
	

}
