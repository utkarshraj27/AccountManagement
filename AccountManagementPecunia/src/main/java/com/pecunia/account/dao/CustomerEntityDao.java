package com.pecunia.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pecunia.account.entity.Customer;

@Repository
public interface CustomerEntityDao extends JpaRepository<Customer, String>{

}
