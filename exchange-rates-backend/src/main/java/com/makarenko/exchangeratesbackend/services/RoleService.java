package com.makarenko.exchangeratesbackend.services;

import com.makarenko.exchangeratesbackend.models.EnumRole;
import com.makarenko.exchangeratesbackend.models.Role;

public interface RoleService {

  Role findByName(EnumRole name);
}
