package com.makarenko.exchangeratesbackend.services.impl;

import com.makarenko.exchangeratesbackend.exceptions.UserException;
import com.makarenko.exchangeratesbackend.models.EnumRole;
import com.makarenko.exchangeratesbackend.models.Role;
import com.makarenko.exchangeratesbackend.repository.RoleRepository;
import com.makarenko.exchangeratesbackend.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  @Autowired
  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public Role findByName(EnumRole name) {
    if (name == null) {
      throw new UserException("EnumRole cannot null.");
    }
    return roleRepository.findByName(name);
  }
}
