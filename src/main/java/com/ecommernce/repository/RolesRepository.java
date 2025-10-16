package com.ecommernce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommernce.model.APIRole;
import com.ecommernce.model.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long>{

	Optional<Roles> findByRoleName(APIRole roleName);

}
