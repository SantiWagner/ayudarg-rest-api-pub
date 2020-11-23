package com.ayudarg.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayudarg.restapi.model.Demand;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {

	Optional<Demand> findById(Long id);
}
