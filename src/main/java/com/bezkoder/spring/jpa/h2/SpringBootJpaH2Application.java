package com.bezkoder.spring.jpa.h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootJpaH2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaH2Application.class, args);
	}
	public String foo(int i ){
		if (i > 0)
			return null;
		else
			return "hello";
	}
	public int bar(){
		return foo(1).length();
	}
	public int divideByZero(){
		return 1/0;
	}
}
