package com.pecunia.account.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pecunia.account.entity.Account;
import com.pecunia.account.entity.Address;
import com.pecunia.account.entity.Customer;
import com.pecunia.account.service.AccountManagementService;
import com.pecunia.account.service.AccountManagementServiceImp;
import com.pecunia.account.service.SequenceGeneratorService;
import com.pecunia.account.util.AccountUtil;

@RestController
public class AccountManagementController {
	
	private static final Logger Log = LoggerFactory.getLogger(AccountManagementController.class);

	@Autowired
	private AccountManagementService accountServivce;
	
	@PostMapping("/addAccount")
	public ResponseEntity<String> addAccount( @RequestBody Map<String ,Object> map){
		
		Account account=AccountUtil.convertToAccount(map);
		Customer customer=AccountUtil.convertToCustomer(map);
		Address address=AccountUtil.convertToAddress(map);
		
		String msg="";
		
		msg = accountServivce.addAccount(account, customer, address);
		
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
		
		return response;
	}
	
	@PutMapping("/updateName")
	public ResponseEntity<String> updateName(@RequestBody Map<String ,Object> requestMap){
		Map<String ,Object> map=requestMap;
		String msg="";
		
		String accountNumber=(String)map.get("accountNumber");
		String newCustomerName=(String)map.get("newCustomerName");
		
		
		Account account=accountServivce.fetchAccountByAccountId(accountNumber);
		String customerId=account.getAccountHolderId();
		Customer customer=accountServivce.fetchCustomerByCustomerId(customerId);
		customer.setCustomerName(newCustomerName);
		boolean isTrue=accountServivce.updateName(account, customer);
		if(isTrue) {
			msg="Account Holder Name Updated Successfully ";
		}else {
			msg="No such Account found with Id"+accountNumber;
		}
		
		
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
		
		return response;
	}
	
	@PutMapping("/updateContact")
	public ResponseEntity<String> updateContact(@RequestBody Map<String ,Object> requestMap){
		Map<String ,Object> map=requestMap;
		String msg="";
		
		String accountNumber=(String)map.get("accountNumber");
		String newCustomerContact=(String)map.get("newCustomerContact");
		
		
		Account account=accountServivce.fetchAccountByAccountId(accountNumber);
		String customerId=account.getAccountHolderId();
		Customer customer=accountServivce.fetchCustomerByCustomerId(customerId);
		customer.setContactNumber(newCustomerContact);
		boolean isTrue=accountServivce.updateContact(account, customer);
		if(isTrue) {
			msg="Account Holder Contact Number Updated Successfully ";
		}else {
			msg="No such Account found with Id"+accountNumber;
		}
		
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
		
		return response;
	}
	
	@PutMapping("/updateAddress")
	public ResponseEntity<String> updateAddress(@RequestBody Map<String ,Object> requestMap){
		String msg="";
		
		String accountNumber=(String)requestMap.get("accountNumber");
		Address updatedAddress=AccountUtil.convertToAddress(requestMap);
		
		Account account=accountServivce.fetchAccountByAccountId(accountNumber);
		String customerId=account.getAccountHolderId();
		Customer customer=accountServivce.fetchCustomerByCustomerId(customerId);
		String addressId=customer.getAddress();
		Address address= accountServivce.fetchAddressByAddressId(addressId);
		updatedAddress.setAddressId(address.getAddressId());
		
		boolean isTrue=accountServivce.updateAccountAddress(account, updatedAddress);
		if(isTrue) {
			msg="Account Holder Address Updated Successfully ";
		}else {
			msg="No such Account found with Id"+accountNumber;
		}
		
		
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);
		
		return response;
	}
	
	@GetMapping("/accounts")
	public ResponseEntity<List<Account>> showAllAccounts(){
		
		List<Account> accounts=null;
		ResponseEntity<List<Account>> response = new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
		
		return response;
	}
	
	@PutMapping("/deleteAccount")
	public ResponseEntity<String> deleteAccount(@RequestBody Map<String ,Object> requestMap){
		Map<String ,Object> map=requestMap;
		String msg="";
		String accountNumber=(String)map.get("accountNumber");
		
		Account account=accountServivce.fetchAccountByAccountId(accountNumber);
		account.setAccountStatus("Closed");
		boolean isTrue=accountServivce.deleteAccount(account);
		if(isTrue) {
			msg="Account Closed Successfully ";
		}else {
			msg="No such Account found with Id"+accountNumber;
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
	
	
	
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<String> handleAll(Throwable ex) {
		Log.error("Something went wrong ", ex);
		String msg = ex.getMessage();
		ResponseEntity<String> response = new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		return response;
	}
}
