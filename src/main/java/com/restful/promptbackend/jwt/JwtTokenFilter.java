package com.restful.promptbackend.jwt;

import com.restful.promptbackend.Role.Role;
import com.restful.promptbackend.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (hasAuthorizationBearer(request)) {
      String token = getAccessToken(request);
      if (jwtTokenUtil.validateAccessToken(token)) {
        setAuthenticationContext(token, request);
      }
    }

    filterChain.doFilter(request, response);
  }

  private boolean hasAuthorizationBearer(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
      return false;
    }
    return true;
  }

  private String getAccessToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    String token = header.split(" ")[1].trim();
    return token;
  }

  private void setAuthenticationContext(String token, HttpServletRequest request) {
    UserDetails userDetails = getUserDetails(token);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  private UserDetails getUserDetails(String token) {
    User user = new User();
    user.setUsername(jwtTokenUtil.getUsername(token));
    String[] rolesList = jwtTokenUtil.getRoles(token);
    for (String role : rolesList) {
      user.addRole(new Role(role));
    }

    return user;
  }
}
