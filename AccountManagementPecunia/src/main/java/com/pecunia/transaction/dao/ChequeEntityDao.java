package com.pecunia.transaction.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pecunia.transaction.entity.Cheque;


@Repository
public interface ChequeEntityDao extends JpaRepository<Cheque, String> {

}
