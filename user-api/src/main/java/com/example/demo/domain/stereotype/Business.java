package com.example.demo.domain.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

// Application Service
// Domain Service (Business)

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Business {
    
}