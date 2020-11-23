package com.ayudarg.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayudarg.restapi.model.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

	Optional<Donation> findById(Long id);
	
}
