package com.ayudarg.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayudarg.restapi.model.Role;
import com.ayudarg.restapi.model.enums.EnumRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(EnumRole name);
}