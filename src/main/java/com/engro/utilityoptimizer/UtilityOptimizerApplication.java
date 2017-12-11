package com.engro.utilityoptimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan("com.engro.utilityoptimizer,com.ge.predix.solsvc.restclient.config,com.ge.predix.solsvc.restclient.impl")
public class UtilityOptimizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilityOptimizerApplication.class, args);
	}
}

