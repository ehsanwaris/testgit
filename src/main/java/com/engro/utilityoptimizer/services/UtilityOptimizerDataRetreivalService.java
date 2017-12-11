package com.engro.utilityoptimizer.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.engro.utilityoptimizer.dao.TimeseriesServiceDao;

@Component
@PropertySource("classpath:predix-timeseries.properties")
public class UtilityOptimizerDataRetreivalService implements EnvironmentAware {

	private static Logger log = Logger.getLogger(UtilityOptimizerDataRetreivalService.class);

	@PostConstruct
	private void init() {
		System.out.println("Test");
	}

	@PreDestroy
	private void destroy() {
		System.out.println("Test");
	}

	
	@Autowired
	public TimeseriesServiceDao timeseriesServiceDao;


	
	public String retreiveData() {
		log.info("Retreive Data ---------------------> "+System.currentTimeMillis());
		return timeseriesServiceDao.retrieveTimeseriesData();

	}
	
//	public String retreiveChartData(String tag, long start, long end) {
//		log.info("Retreive Chart Data ---------------------> "+System.currentTimeMillis());
//		return timeseriesServiceDao.retrieveTimeseriesChartData(tag, start, end);
//
//	}
	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		
	}

}
