package com.makarenko.exchangeratesbackend.repository;

import com.makarenko.exchangeratesbackend.models.EnumRole;
import com.makarenko.exchangeratesbackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByName(EnumRole name);
}
