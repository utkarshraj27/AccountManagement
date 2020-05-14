package com.pecunia.transaction.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pecunia.account.dao.AccountEntityDao;
import com.pecunia.account.entity.Account;
import com.pecunia.account.exception.AccountException;
import com.pecunia.account.service.SequenceGeneratorService;
import com.pecunia.transaction.dao.ChequeEntityDao;
import com.pecunia.transaction.dao.TransactionEntityDao;
import com.pecunia.transaction.entity.Cheque;
import com.pecunia.transaction.entity.Transaction;


@Service
@Transactional
public class TransactionManagementServiceImp implements TransactionManagementService {

	@Autowired
	private AccountEntityDao accountEntityDao;
	
	@Autowired
	private SequenceGeneratorService mySequence; 
	@Autowired
	private TransactionEntityDao transactionDao;
	
	@Autowired
	private ChequeEntityDao chequeDao;

	@Override
	public double getBalance(Account account) {
		double myBalance = account.getAccountBalance();
		return myBalance;
		
	}

	@Override
	public boolean updateBalance(Account account,double balance)  {
			account.setAccountBalance(balance);
			accountEntityDao.save(account);
			return true;
		
	}

	@Override
	public int generateTransactionId(Transaction transaction) {
		long myTransactionId = mySequence.generateTransId();
		String transId = String.valueOf(myTransactionId);
		int id = Integer.parseInt(transId);
		return id;
		
		
	}

	@Override
	public int generateChequeId(Cheque cheque) {
		long myChequeId = mySequence.generateChequeId();
		String chequeId = String.valueOf(myChequeId);
		int id = Integer.parseInt(chequeId);
		return id;
	}

	@Override
	public Account getAccountById(String accountNumber) throws AccountException {
		Optional<Account> optional=accountEntityDao.findById(accountNumber);
		 if(optional.isPresent()) {
	            Account account=optional.get();
	            return account;
	        }
	        throw new AccountException("Account not found for id="+accountNumber);
	}

	@Override
	public int creditUsingSlip(Transaction transaction) {
		
		int transactionId = generateTransactionId(transaction);
		transaction.setTransId(transactionId);
		transaction.setTransType("Credit");
		transaction.setTransDate(new Date());
		transaction.setTransOption("Using Slip");
		transactionDao.save(transaction);
		return transactionId;
			
	}
		@Override
	public int creditUsingCheque(Transaction transaction, Cheque cheque) {
			int transactionId = generateTransactionId(transaction);
			int chequeId = generateChequeId(cheque);
			transaction.setChequeId(Integer.toString(chequeId));
			transaction.setTransDate(new Date());
			transaction.setTransId(transactionId);
			transaction.setTransOption("Using Cheque");
			transaction.setTransType("Credit");
			transactionDao.save(transaction);
			chequeDao.save(cheque);
			return transactionId;
	}

	@Override
	public int debitUsingSlip(Transaction transaction)  {
		int transactionId = generateTransactionId(transaction);
		transaction.setTransId(transactionId);
		transaction.setTransDate(new Date());
		transaction.setTransOption("Using Slip");
		transaction.setTransType("Debit");
		transactionDao.save(transaction);
		return transactionId;
	}

	@Override
	public int debitUsingCheque(Transaction transaction, Cheque cheque) {
		int transId = generateTransactionId(transaction);
		transaction.setTransId(transId);
		transaction.setTransDate(new Date());
		transaction.setTransOption("Using Cheque");
		transaction.setTransType("Debit");
		int myChequeId = generateChequeId(cheque);
		cheque.setChequeId(Integer.toString(myChequeId));
		transaction.setChequeId(Integer.toString(myChequeId));
		chequeDao.save(cheque);
		transactionDao.save(transaction);
		return transId;
		
	}
	
	
}
