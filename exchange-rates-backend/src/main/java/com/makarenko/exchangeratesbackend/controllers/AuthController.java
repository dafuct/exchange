package com.makarenko.exchangeratesbackend.controllers;

import com.makarenko.exchangeratesbackend.config.security.jwt.AuthEntryPointJwt;
import com.makarenko.exchangeratesbackend.exceptions.UserException;
import com.makarenko.exchangeratesbackend.models.EnumRole;
import com.makarenko.exchangeratesbackend.models.Role;
import com.makarenko.exchangeratesbackend.models.User;
import com.makarenko.exchangeratesbackend.payload.request.LoginRequest;
import com.makarenko.exchangeratesbackend.payload.request.SignUpRequest;
import com.makarenko.exchangeratesbackend.payload.response.JwtResponse;
import com.makarenko.exchangeratesbackend.payload.response.MessageResponse;
import com.makarenko.exchangeratesbackend.config.security.jwt.JwtUtils;
import com.makarenko.exchangeratesbackend.config.security.services.UserDetailsImpl;
import com.makarenko.exchangeratesbackend.services.RoleService;
import com.makarenko.exchangeratesbackend.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final RoleService roleService;
  private final PasswordEncoder encoder;
  private final JwtUtils jwtUtils;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager, UserService userService,
      RoleService roleService,
      PasswordEncoder encoder, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.roleService = roleService;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
    logger.debug("constructor AuthController created");
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    logger.debug("method login() successful");
    return ResponseEntity.ok(new JwtResponse(
        jwtUtils.generateJwtToken(authentication),
        userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        userDetails.getPhone(),
        roles));
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userService.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already exist!"));
    }

    if (userService.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        signUpRequest.getPhone(), encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    checkRole(strRoles, roles);

    user.setRoles(roles);
    userService.save(user);

    logger.debug("method register() successful");
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  private void checkRole(Set<String> strRoles, Set<Role> roles) {
    if (strRoles == null) {
      Role userRole = roleService.findByName(EnumRole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        if (role.equals("user")) {
          Role adminRole = roleService.findByName(EnumRole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
        }
      });
    }
  }
}
