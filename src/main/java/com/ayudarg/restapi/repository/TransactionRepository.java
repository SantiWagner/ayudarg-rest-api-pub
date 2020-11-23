package com.ayudarg.restapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayudarg.restapi.model.OwnerEntity;
import com.ayudarg.restapi.model.Resource;
import com.ayudarg.restapi.model.Transaction;
import com.ayudarg.restapi.model.enums.TransactionStatus;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<Transaction> findById(Long id);
	
	Page<Transaction> findByResource(Resource resource, Pageable pageable);
	
	
}
