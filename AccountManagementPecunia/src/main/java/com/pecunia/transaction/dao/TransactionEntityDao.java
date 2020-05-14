package com.pecunia.transaction.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pecunia.transaction.entity.Transaction;



@Repository
public interface TransactionEntityDao extends JpaRepository<Transaction, Integer>  {

}
