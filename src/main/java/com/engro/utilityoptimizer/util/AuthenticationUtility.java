package com.engro.utilityoptimizer.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ge.predix.solsvc.restclient.impl.RestClient;




/**
 * Utility class to do authorization related functionalities.
 * 
 * @author 502688286
 *
 */
@Component
@PropertySource("classpath:predix-timeseries.properties")
public class AuthenticationUtility {

	private static Logger log = Logger.getLogger(AuthenticationUtility.class);

	@Autowired
	@Qualifier("restClient")
	public RestClient rest;

	@Autowired
	private HttpServletRequest context;

	@Value("${predix_oauthRestHost}")
	String predix_oauthRestHost;

	@Value("${predix_oauthClientId}")
	String predix_oauthClientId;

	@Value("${predix_oauthGrantType}")
	String predix_oauthGrantType;

	@Value("${accessTokenEndpointUrl}")
	String accessTokenEndpointUrl;

	@Value("${clientId}")
	String clientId;

	@Value("${clientSecret}")
	String clientSecret;
	
	@Value("${predix_oauthCredentials}")
	String oauthCredentials;
	
	@Value("${predix_oauthZoneId}")
	String uaaZoneId;

	//@Value("${timeseriesZone}")
	String timeseriesZone;

	@Value("${network.proxy.enabled}")
	String proxyEnabled;

	@Value("${network.host.name}")
	String hostName;

	@Value("${network.host.port}")
	String hostPort;

	// @Autowired
	// private Environment env;

	/**
	 * 
	 * @return
	 */
	public String getAuthToken() {

		String authToken = "";
		try {

			boolean oauthClientIdEncode = true;
			String oauthPort = "80";
			String oauthResource = "/oauth/token";

			String proxyHost = null; //
			String proxyPort = null;//

			if (proxyEnabled != null && "true".equalsIgnoreCase(proxyEnabled)) {
				proxyHost = this.hostName;
				proxyPort = this.hostPort;
			}

			List<Header> headers = this.rest.getOauthHttpHeaders(this.predix_oauthClientId, oauthClientIdEncode);
			// Request Token from UAA
			authToken = this.rest.requestToken(headers, oauthResource, this.predix_oauthRestHost, oauthPort, this.predix_oauthGrantType, proxyHost, proxyPort);

			// Token is stored as a JSON object
			JSONObject token = new JSONObject(authToken);

			// Store Access_token part of the token
			return "Bearer " + token.getString("access_token");

		} catch (Exception e) {
			log.error("Exception :[" + e.getMessage() + "]");
		}
		return "";

	}
	
	public String getAuthenticated(String username, String password) {

		String authToken = "";
		String condition = "Access Denied";
		try {
			boolean oauthClientIdEncode = true;
			String oauthPort = "80";
			String oauthResource = "/oauth/token";

			String proxyHost = null; //
			String proxyPort = null;//

			if (proxyEnabled != null && "true".equalsIgnoreCase(proxyEnabled)) {
				proxyHost = this.hostName;
				proxyPort = this.hostPort;
			}
			String clientIdPassword = username+":"+password;
			List<Header> headers = this.rest.getOauthHttpHeaders(clientIdPassword, oauthClientIdEncode);
			// Request Token from UAA
			authToken = this.rest.requestToken(headers, oauthResource, this.predix_oauthRestHost, oauthPort, this.predix_oauthGrantType, proxyHost, proxyPort);
			// Token is stored as a JSON object
			JSONObject token = new JSONObject(authToken);
			String authentication = token.getString("access_token");
			if(authentication.equals(null)||authentication.equals(""))
			{
				condition = "Access Denied";
				log.info("Access Denied");
			}else{
				condition = "Access Granted";
				log.info("Access Granted");
			}
			// Store Access_token part of the token
			return condition;

		} catch (Exception e) {
			log.error("Exception :[" + e.getMessage() + "]");
		}
		return condition;

	}
	
	public String validateToken(String oAuth2Token) {
		
		String responseString = "denied";

		String url = "https://"+predix_oauthRestHost+"/check_token";
		 RestTemplate restTemplate = new RestTemplate();
		 HttpHeaders headers = new HttpHeaders();
		 MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
		 HttpEntity<MultiValueMap<String, String>> requestEntity = null;
		 
		 ResponseEntity<String> validationResponse = null;
		 
		 String authorization = "Basic "+oauthCredentials;
		 
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Authorization", authorization);
		headers.add("x-tenant", uaaZoneId);
		
		postParameters.add("token", oAuth2Token);
		postParameters.add("grant_type", "password");
		
		requestEntity = new HttpEntity<MultiValueMap<String, String>>(postParameters,headers);
		
		//HttpEntity<String> analyticsResponse = oAuth2RestTemplate.exchange(url, HttpMethod.POST, this.requestEntity,String.class);
		try{
			validationResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
			log.info(validationResponse.getStatusCode()+" -- "+HttpStatus.OK);
			if(validationResponse.getStatusCode() == HttpStatus.OK)
			{
				log.info("Response is 200 ok - token validated - ");
				responseString = "granted";
			}
		}catch (Exception e) {
			// TODO: handle exception
			log.info(url);
			e.printStackTrace();
		}
		
		
		return responseString; 

	}

	/**
	 * 
	 * @param authorization
	 * @return
	 */
	public HttpHeaders getHttpHeaders(String authorization) {

		HttpHeaders httpHeaders = new HttpHeaders();
		try {

			httpHeaders.add("Predix-Zone-Id", this.timeseriesZone);
			httpHeaders.add("Authorization", authorization);
			httpHeaders.add("Content-Type", "application/json");

		} catch (Exception e) {
			log.error("Exception :[" + e.getMessage() + "]");
		}

		return httpHeaders;
	}

	/**
	 * 
	 */
	public RestTemplate getRestTemplate() {

		try {

			if (proxyEnabled != null && "true".equalsIgnoreCase(proxyEnabled)) {

				SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.hostName, Integer.parseInt(this.hostPort)));
				clientHttpRequestFactory.setProxy(proxy);
				return new RestTemplate(clientHttpRequestFactory);

			} else {
				return new RestTemplate();
			}

		} catch (Exception e) {
			log.error("Exception :[" + e.getMessage() + "]");
		}

		return null;
	}
}
