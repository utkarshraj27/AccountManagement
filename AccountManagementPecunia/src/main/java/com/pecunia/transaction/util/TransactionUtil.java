package com.pecunia.transaction.util;

import java.util.Date;
import java.util.Map;

import com.pecunia.transaction.entity.Cheque;
import com.pecunia.transaction.entity.Transaction;



public class TransactionUtil {
	
	public static Cheque convertToCheque(Map<String,Object> request)
	{
		
		int chequeNum = (int)request.get("chequeNum");
		String chequeAccountNo = (String)request.get("chequeAccountNo");
		String chequeHolderName = (String)request.get("chequeHolderName");
		String chequeBankName = (String)request.get("chequeBankName");
		String chequeIFSC = (String)request.get("chequeIFSC");
		Date chequeIssueDate = (Date)request.get("chequeIssueDate");
		Cheque myCheque = new Cheque();
		myCheque.setChequeNum(chequeNum);
		myCheque.setChequeAccountNo(chequeAccountNo);
		myCheque.setChequeHolderName(chequeHolderName);
		myCheque.setChequeBankName(chequeBankName);
		myCheque.setChequeIFSC(chequeIFSC);
		myCheque.setChequeIssueDate(chequeIssueDate);
		return myCheque;
	}
	
	public static Transaction convertToTransaction(Map<String,Object> request)
	{
		String transAccountId = (String)request.get("transAccountId");
		double transAmount = (double)request.get("transAmount");
		Transaction myTransaction = new Transaction();
		myTransaction.setTransAccountId(transAccountId);
		myTransaction.setTransAmount(transAmount);
		return myTransaction;
	}
}
