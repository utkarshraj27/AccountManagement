package com.pecunia.transaction.controller;
import java.util.Map;

import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pecunia.account.entity.Account;
import com.pecunia.account.entity.Customer;
import com.pecunia.account.service.AccountManagementService;
import com.pecunia.transaction.entity.Cheque;
import com.pecunia.transaction.entity.Transaction;
import com.pecunia.transaction.exception.ChequeBounceException;
import com.pecunia.transaction.exception.InSufficientBalanceException;
import com.pecunia.transaction.service.TransactionManagementService;
import com.pecunia.transaction.util.TransactionUtil;


@RestController
public class TransactionManagementController {
	
	private static final Logger Log = LoggerFactory.getLogger(TransactionManagementController.class);
	@Autowired
	private TransactionManagementService transactionService;
	
	@Autowired
	private AccountManagementService accountService;
	
	@PutMapping("/creditUsingSlip")
	public ResponseEntity<String> creditUsingSlip( @RequestBody Map<String ,Object> requestMap) {
		Map<String ,Object> map=requestMap;
		String msg="";
		String transAccountId = (String)map.get("transAccountId");
		double transAmount = (double)map.get("transAmount");
			Account userAccount = transactionService.getAccountById(transAccountId);
			String customerId = userAccount.getAccountHolderId();
			Customer myCustomer = accountService.fetchCustomerByCustomerId(customerId);
			String customerName = myCustomer.getCustomerName();
			double userBalance = transactionService.getBalance(userAccount);
			double newBalance = userBalance+transAmount;
			Transaction transaction = new Transaction();
			transaction.setTransAccountId(transAccountId);
			transaction.setTransAmount(transAmount);
			transaction.setTransClosingBalance(newBalance);
			transaction.setTransTo(customerName);
			int transactionId = transactionService.creditUsingSlip(transaction);
	        boolean isTrue = transactionService.updateBalance(userAccount, newBalance);
			if(isTrue)
			{
				msg = "Amount creditted successfully";
			}
			else
			{
				msg="No Such Account Found with Id "+transAccountId;
			}
		
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
			return response;
		}
	
	
	@PutMapping("/debitUsingSlip")
	public ResponseEntity<String> debitUsingSlip( @RequestBody Map<String ,Object> requestMap) {
		Map<String ,Object> map=requestMap;
		String msg="";
		
		String transAccountId = (String)map.get("transAccountId");
		double transAmount = (double)map.get("transAmount");
	
			Account userAccount = transactionService.getAccountById(transAccountId);
			String customerId = userAccount.getAccountHolderId();
			Customer myCustomer = accountService.fetchCustomerByCustomerId(customerId);
			String customerName = myCustomer.getCustomerName();
			double userBalance = transactionService.getBalance(userAccount);
			if(transAmount>userBalance)
			{
				throw new InSufficientBalanceException("Low Balance In The Account");
			}
			double newBalance = userBalance-transAmount;
			Transaction transaction = new Transaction();
			transaction.setTransAccountId(transAccountId);
			transaction.setTransAmount(transAmount);
			transaction.setTransClosingBalance(newBalance);
			transaction.setTransFrom(customerName);
			int transId = transactionService.debitUsingSlip(transaction);
			boolean isTrue = transactionService.updateBalance(userAccount, newBalance);
			if(isTrue)
			{
				msg = "Amount debitted successfully";
			}
			else
			{
				msg="No Such Account Found with Id "+transAccountId;
			}
		
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
		return response;
	}
	
	
	@PutMapping("/debitUsingCheque")
	public ResponseEntity<String> debitUsingCheque( @RequestBody Map<String ,Object> requestMap) {
		Map<String ,Object> map=requestMap;
		String msg="";
		Transaction transaction = TransactionUtil.convertToTransaction(map);
		Cheque cheque = TransactionUtil.convertToCheque(map);
		String transAccountId = transaction.getTransAccountId();
		String chequeAccountNo = cheque.getChequeAccountNo();
		double transAmount = transaction.getTransAmount();
			Account userAccount = transactionService.getAccountById(transAccountId);
			Account payeeAccount = transactionService.getAccountById(chequeAccountNo);
			if(!userAccount.equals(payeeAccount))
			{
				cheque.setChequeStatus("Cheque Bounced");
				throw new ChequeBounceException("Payee & Beneficiary Account Not Matched");
			}
			String customerId = userAccount.getAccountHolderId();
			Customer myCustomer = accountService.fetchCustomerByCustomerId(customerId);
			String customerName = myCustomer.getCustomerName();
			double myBalance = transactionService.getBalance(userAccount);
			if(transAmount>myBalance)
			{
				cheque.setChequeStatus("Cheque Bounced");
				throw new InSufficientBalanceException("Low Balance In The Account");
			}
			double newBalance = myBalance-transAmount;
			cheque.setChequeStatus("Cheque cleared");
			transaction.setTransClosingBalance(newBalance);
			transaction.setTransFrom(customerName);
			
			int transId = transactionService.debitUsingCheque(transaction, cheque);
			boolean isTrue = transactionService.updateBalance(userAccount, newBalance);
			if(isTrue)
			{
				msg = "Amount debitted successfully";
			}
			else
			{
				msg="No Such Account Found with Id "+transAccountId;
			}
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
		return response;
	
	}
	
	@PutMapping("/creditUsingCheque")
	public ResponseEntity<String> creditUsingCheque( @RequestBody Map<String ,Object> requestMap) {
		Map<String ,Object> map=requestMap;
		String msg="";
		Transaction transaction = TransactionUtil.convertToTransaction(map);
		Cheque cheque = TransactionUtil.convertToCheque(map);
		String transAccountId = transaction.getTransAccountId();
		double transAmount = transaction.getTransAmount();
		String chequeAccountNo = cheque.getChequeId();
		
			Account beneficiaryAccount = transactionService.getAccountById(transAccountId);
			Account payeeAccount = transactionService.getAccountById(chequeAccountNo);
			String beneficiaryId = beneficiaryAccount.getAccountHolderId();
			Customer myBeneficiary = accountService.fetchCustomerByCustomerId(beneficiaryId);
			String beneficiaryName = myBeneficiary.getCustomerName();
			String payeeId = payeeAccount.getAccountHolderId();
			Customer myPayee = accountService.fetchCustomerByCustomerId(payeeId);
			String payeeName = myPayee.getCustomerName();
			double beneficiaryBalance = transactionService.getBalance(beneficiaryAccount);
			double payeeBalance = transactionService.getBalance(payeeAccount);
			double newBeneficiaryBalance = beneficiaryBalance+transAmount;
			double newPayeeBalance = payeeBalance-transAmount;
			transaction.setTransClosingBalance(newBeneficiaryBalance);
			transaction.setTransFrom(payeeName);
			transaction.setTransTo(beneficiaryName);
			cheque.setChequeStatus("Cheque cleared");
			int transId = transactionService.creditUsingCheque(transaction, cheque);
			boolean beneficiaryisTrue = transactionService.updateBalance(beneficiaryAccount, newBeneficiaryBalance);
			boolean payeeisTrue = transactionService.updateBalance(payeeAccount, newPayeeBalance);
			if(beneficiaryisTrue==true && payeeisTrue==true)
			{
				msg = "Amount creditted successfully";
			}
			else
			{
				msg="No Such Account Found with Id "+transAccountId;
			}
			ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
			return response;
	}
	
	@ExceptionHandler(AccountException.class)
	public ResponseEntity<String> handleAccountNotFound(AccountException ex) {
		Log.error("Account not found exception ", ex);
		String msg = ex.getMessage();
		ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
		return response;
	}
	
	@ExceptionHandler(InSufficientBalanceException.class)
	public ResponseEntity<String> handleLowBalance(InSufficientBalanceException ex) {
		Log.error("Low Balance Available for The Operation ", ex);
		String msg = ex.getMessage();
		ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
		return response;
	}
	
	@ExceptionHandler(ChequeBounceException.class)
	public ResponseEntity<String> handleChequeBounce(ChequeBounceException ex) {
		Log.error("Your Cheque is Bounced ", ex);
		String msg = ex.getMessage();
		ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
		return response;
}
	
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<String> handleAll(Throwable ex) {
		Log.error("Something went wrong ", ex);
		String msg = ex.getMessage();
		ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		return response;
	}
	
	
}


