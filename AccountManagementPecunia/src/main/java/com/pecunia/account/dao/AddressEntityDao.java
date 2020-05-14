package com.pecunia.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pecunia.account.entity.Address;

@Repository
public interface AddressEntityDao extends JpaRepository<Address, String> {

}
