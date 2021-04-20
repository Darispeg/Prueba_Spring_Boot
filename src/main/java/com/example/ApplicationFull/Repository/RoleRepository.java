package com.example.ApplicationFull.Repository;

import com.example.ApplicationFull.Entity.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    
}
