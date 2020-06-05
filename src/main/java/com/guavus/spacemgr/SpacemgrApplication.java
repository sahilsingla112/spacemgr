package com.guavus.spacemgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class SpacemgrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpacemgrApplication.class, args);
	}

}
