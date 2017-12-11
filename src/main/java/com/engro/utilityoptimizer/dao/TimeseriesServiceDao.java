package com.engro.utilityoptimizer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.engro.utilityoptimizer.util.AppConstants;
import com.engro.utilityoptimizer.util.TimeseriesServiceUtil;

@Component
public class TimeseriesServiceDao implements EnvironmentAware {

	private static Logger log = Logger.getLogger(TimeseriesServiceDao.class);

	@Autowired
	TimeseriesServiceUtil timeseriesServiceUtil;


	public String retrieveTimeseriesData() {

		log.info("retrieveTimeseriesData");

		String responseFromQuery = "";
		try {
			List<String> tagList = new ArrayList<>();
			for (String tag : AppConstants.tags) {
				tagList.add(tag);
			}

			responseFromQuery = timeseriesServiceUtil.retrieveDataPoints(tagList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseFromQuery;
	}

	@Override
	public void setEnvironment(Environment env) {

	}

}
