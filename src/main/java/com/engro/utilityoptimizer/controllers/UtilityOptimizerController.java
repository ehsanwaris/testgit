package com.engro.utilityoptimizer.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.utilityoptimizer.services.UtilityOptimizerDataRetreivalService;
import com.engro.utilityoptimizer.util.AuthenticationUtility;

@RestController
public class UtilityOptimizerController {
	private static Logger log = Logger.getLogger(UtilityOptimizerController.class);



	@Autowired
	UtilityOptimizerDataRetreivalService queryService ;
	
	@Autowired
	AuthenticationUtility authenticationUtility;
	
	
	@RequestMapping("/getJSON")
	public String getJSON(){
		
		
		return "{\"number1\":5,\"number2\":10}";
	}
	
	@RequestMapping("/postJSON")
	public String postJSON(@RequestBody String json){
		
		log.info("postJSON: received json " + json);
		return json;
	}
	
	@RequestMapping("/queryWithValidation")
	public String querywithValidation(@RequestHeader("access_token") String accessToken){
		// Verify if token is present.. then validate token
		if(accessToken==null || accessToken.isEmpty() || authenticationUtility.validateToken(accessToken).equals("denied"))
			return "No/Invalid Token";
		log.info("Access Token: "+accessToken);
		
		return queryService.retreiveData();
	}
	@RequestMapping("/query")
	public String query(){
		// Verify if token is present.. then validate token
		return queryService.retreiveData();
	}
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping(){
		log.info("ping-pong ");
		return "pong";
	}
	@RequestMapping("/")
	public String empty(){
		log.info("Micro Service Template: / ");
		return "Welcome";
	}

	
	
}
