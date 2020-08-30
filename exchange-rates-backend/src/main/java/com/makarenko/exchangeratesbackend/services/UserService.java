package com.makarenko.exchangeratesbackend.services;

import com.makarenko.exchangeratesbackend.models.User;

public interface UserService {

  User findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  User save(User user);
}
