package com.restful.promptbackend.User;

import com.restful.promptbackend.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginInfo loginInfo) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginInfo.getUsername(), loginInfo.getPassword()));

    User user = (User) authentication.getPrincipal();
    String accessToken = jwtTokenUtil.generateAccessToken(user);
    LoginResponse response = new LoginResponse(user.getUsername(), accessToken);

    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegistrationInfo registrationInfo) {
    String password = passwordEncoder.encode(registrationInfo.getPassword());
    User newUser = new User(
            registrationInfo.getUsername(),
            password,
            registrationInfo.getName(),
            registrationInfo.getAddress()
            );
    User savedUser = userRepository.save(newUser);
    return new ResponseEntity<>("", HttpStatus.CREATED);
  }
}