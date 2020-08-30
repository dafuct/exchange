package com.makarenko.exchangeratesbackend.services.impl;

import com.makarenko.exchangeratesbackend.exceptions.UserException;
import com.makarenko.exchangeratesbackend.models.User;
import com.makarenko.exchangeratesbackend.repository.UserRepository;
import com.makarenko.exchangeratesbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public User findByUsername(String username) {
    checkInputParameter(username);
    return userRepository.findByUsername(username);
  }

  @Transactional(readOnly = true)
  @Override
  public Boolean existsByUsername(String username) {
    checkInputParameter(username);
    return userRepository.existsByUsername(username);
  }

  @Transactional(readOnly = true)
  @Override
  public Boolean existsByEmail(String email) {
    checkInputParameter(email);
    return userRepository.existsByEmail(email);
  }

  @Transactional
  @Override
  public User save(User user) {
    if (user == null) {
      throw new UserException("User cannot null.");
    }
    return userRepository.save(user);
  }

  private void checkInputParameter(String inputParameter) {
    if (inputParameter == null || inputParameter.isEmpty()) {
      throw new UserException("Input parameter: " + inputParameter + "cannot null or empty.");
    }
  }
}
