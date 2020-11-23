package com.ayudarg.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayudarg.restapi.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	Optional<Project> findById(Long id);
}
