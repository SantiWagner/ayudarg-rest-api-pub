package com.ayudarg.restapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayudarg.restapi.model.OwnerEntity;
import com.ayudarg.restapi.model.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
	
	Optional<Resource> findById(Long id);
	
	Page<Resource> findByName(String name, Pageable pageable);
	
	Page<Resource> findByOwner(OwnerEntity owner, Pageable pageable);
	
}