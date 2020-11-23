package com.ayudarg.restapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayudarg.restapi.model.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

	Optional<Institution> findById(Long id);
	
	Page<Institution> findByName(String name, Pageable pageable);
	
	Page<Institution> findByNameContaining(String name, Pageable pageable);
}
