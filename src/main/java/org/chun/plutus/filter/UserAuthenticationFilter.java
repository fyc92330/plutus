//package com.chun.plutus.filter;
//
//import com.chun.plutus.util.CacheUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Slf4j
//public class UserAuthenticationFilter extends BasicAuthenticationFilter {
//
//  private final CacheUtil cacheUtil;
//
//  /**
//   * Creates an instance which will authenticate against the supplied
//   * {@code AuthenticationManager} and which will ignore failed authentication attempts,
//   * allowing the request to proceed down the filter chain.
//   *
//   * @param authenticationManager the bean to submit authentication requests to
//   */
//  public UserAuthenticationFilter(AuthenticationManager authenticationManager, CacheUtil cacheUtil) {
//    super(authenticationManager);
//    this.cacheUtil = cacheUtil;
//  }
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request,
//                                  HttpServletResponse response,
//                                  FilterChain chain) {
//
//  }
//
//}
