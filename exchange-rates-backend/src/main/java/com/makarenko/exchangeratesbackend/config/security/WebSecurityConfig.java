package com.makarenko.exchangeratesbackend.config.security;

import com.makarenko.exchangeratesbackend.config.AppConfiguration;
import com.makarenko.exchangeratesbackend.config.security.jwt.AuthEntryPointJwt;
import com.makarenko.exchangeratesbackend.config.security.jwt.AuthTokenFilter;
import com.makarenko.exchangeratesbackend.config.security.jwt.JwtUtils;
import com.makarenko.exchangeratesbackend.config.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({AppConfiguration.class})
@ComponentScan(basePackages = {"com.makarenko.exchangeratesbackend"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsServiceImpl userDetailsService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final JwtUtils jwtUtils;

  @Autowired
  public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
      AuthEntryPointJwt unauthorizedHandler,
      JwtUtils jwtUtils) {
    this.userDetailsService = userDetailsService;
    this.unauthorizedHandler = unauthorizedHandler;
    this.jwtUtils = jwtUtils;
  }

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter(jwtUtils, userDetailsService);
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests().antMatchers("/api/auth/**").permitAll()
        .antMatchers("/api/content/**").permitAll()
        .anyRequest().authenticated();

    http.addFilterBefore(authenticationJwtTokenFilter(),
        UsernamePasswordAuthenticationFilter.class);
  }
}
