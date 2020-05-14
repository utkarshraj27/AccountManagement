package com.pecunia.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pecunia.account.entity.Account;

@Repository
public interface AccountEntityDao extends JpaRepository<Account, String>{

}
