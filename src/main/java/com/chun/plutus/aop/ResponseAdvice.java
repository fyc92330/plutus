//package com.chun.plutus.aop;
//
//import com.chun.plutus.ro.PlutusApiRo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.MethodParameter;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@Slf4j
//@Order(999)
//@RestControllerAdvice
//public class ResponseAdvice implements ResponseBodyAdvice<PlutusApiRo> {
//  @Override
//  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//    return true;
//  }
//
//  @Override
//  public PlutusApiRo beforeBodyWrite(PlutusApiRo body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//    return null;
//  }
//}
