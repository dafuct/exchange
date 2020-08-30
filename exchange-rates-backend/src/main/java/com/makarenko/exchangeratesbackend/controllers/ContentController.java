package com.makarenko.exchangeratesbackend.controllers;

import com.makarenko.exchangeratesbackend.dto.CurrencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/content")
public class ContentController {

  @Value("${app.rest.url}")
  private String restUrl;

  private final RestTemplate restTemplate;

  @Autowired
  public ContentController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> userAccess() {
    CurrencyDTO[] forObject = restTemplate.getForObject(
        restUrl, CurrencyDTO[].class);
    return new ResponseEntity<>(forObject, HttpStatus.OK);
  }
}
