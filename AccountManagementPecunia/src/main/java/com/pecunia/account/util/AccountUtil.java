package com.pecunia.account.util;

import java.util.Date;
import java.util.Map;

import com.pecunia.account.entity.Account;
import com.pecunia.account.entity.Address;
import com.pecunia.account.entity.Customer;

public class AccountUtil {
	
	public static Account convertToAccount(Map<String ,Object> map) {
			
		Account account=new Account();
		
		double accountBalance=(double)map.get("accountBalance");
		String branchId=(String)map.get("branchId");
		String accountType=(String)map.get("accountType");
		String accountStatus=(String)map.get("accountStatus");
		double accountInterest=(double)map.get("accountInterest");
		Date lastUpdate=(Date)map.get("lastUpdate");
		
		account.setAccountBalance(accountBalance);
		account.setAccountInterest(accountInterest);
		account.setAccountStatus(accountStatus);
		account.setAccountType(accountType);
		account.setLastUpdate(lastUpdate);
		account.setBranchId(branchId);
		
		return account;
	}
	public static Customer convertToCustomer(Map<String ,Object> map) {
			
			Customer customer = new Customer();
			
			String customerName=(String)map.get("customerName");
			Date customerDob=(Date)map.get("customerDob");
			String customerGender=(String)map.get("customerGender");
			String customerContactNumber=(String)map.get("customerContactNumber");
			String customerPanNumber=(String)map.get("customerPanNumber");
			String customerAadharNumber=(String)map.get("customerAadharNumber");
			
			customer.setAadharNumber(customerAadharNumber);
			customer.setCustomerName(customerName);
			customer.setDOB(customerDob);
			customer.setGender(customerGender);
			customer.setContactNumber(customerContactNumber);
			customer.setPanNumber(customerPanNumber);
			
			return customer;
		}
		public static Address convertToAddress(Map<String ,Object> map) {
			
			Address address = new Address();
			String addressLine=(String)map.get("addressLine");
			String city=(String)map.get("city");
			String state=(String)map.get("state");
			String country=(String)map.get("country");
			String zipcode=(String)map.get("zipcode");
			
			address.setAddressLine(addressLine);
			address.setCity(city);
			address.setState(state);
			address.setCountry(country);
			address.setZipCode(zipcode);
			
			return address;
		}

}
