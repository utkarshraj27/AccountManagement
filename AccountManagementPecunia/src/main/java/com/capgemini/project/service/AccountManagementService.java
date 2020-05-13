package com.capgemini.project.service;

import java.util.List;

import javax.security.auth.login.AccountException;

import com.capgemini.project.entity.Account;
import com.capgemini.project.entity.Address;
import com.capgemini.project.entity.Customer;

public interface AccountManagementService {
	
	
	public String addAccount(Account account,Customer customer,Address address);
	public boolean deleteAccount(Account account) ;
	
	public boolean updateAccountAddress(Account account, Address address) ;
	
	public boolean updateName(Account account , Customer customer) ;
	
	public boolean updateContact(Account account , Customer customer);
	
	public List<Account> showAllAccount() ;
	
	public Account fetchAccountByAccountId(String accountId)  ;
	
	public Customer fetchCustomerByCustomerId(String customerId)  ;
	
	public Address fetchAddressByAddressId(String addressId) ;
}
