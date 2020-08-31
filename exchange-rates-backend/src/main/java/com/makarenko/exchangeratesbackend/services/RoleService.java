package com.makarenko.exchangeratesbackend.services;

import com.makarenko.exchangeratesbackend.models.EnumRole;
import com.makarenko.exchangeratesbackend.models.Role;
import java.util.Optional;

public interface RoleService {

  Optional<Role> findByName(EnumRole name);
}
