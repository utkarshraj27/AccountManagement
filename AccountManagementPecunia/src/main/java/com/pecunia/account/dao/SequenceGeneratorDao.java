package com.pecunia.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pecunia.account.entity.SequenceGenerator;

@Repository
public interface SequenceGeneratorDao extends JpaRepository<SequenceGenerator, Integer> {

}
