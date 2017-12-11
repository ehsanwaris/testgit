package com.engro.utilityoptimizer.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ge.predix.timeseries.client.ClientFactory;
import com.ge.predix.timeseries.client.TenantContext;
import com.ge.predix.timeseries.client.TenantContextFactory;
import com.ge.predix.timeseries.exceptions.PredixTimeSeriesException;
import com.ge.predix.timeseries.model.builder.IngestionRequestBuilder;
import com.ge.predix.timeseries.model.builder.IngestionTag;
import com.ge.predix.timeseries.model.builder.QueryBuilder;
import com.ge.predix.timeseries.model.builder.QueryTag;
import com.ge.predix.timeseries.model.datapoints.DataPoint;
import com.ge.predix.timeseries.model.datapoints.Quality;
import com.ge.predix.timeseries.model.response.IngestionResponse;
import com.ge.predix.timeseries.model.response.QueryResponse;
import com.ge.predix.timeseries.model.response.Result;
import com.ge.predix.timeseries.model.response.TagResponse;


@Component
public class TimeseriesServiceUtil implements EnvironmentAware {

	private static Logger log = Logger.getLogger(TimeseriesServiceUtil.class);
	int ingestionSize = 511000;
	

		public String retrieveDataPoints(List<String> list) throws JSONException {
		try {
			


			TenantContext tenant = TenantContextFactory	.createTenantContextFromPropertiesFile("predix-timeseries.properties");
		
			
			//TODO: to query in range :: set start time and end times
//			long startTime = 0L;
//			long endTime = 0L;
//			QueryBuilder builder = QueryBuilder.createQuery().withStartAbs(startTime).withEndAbs(endTime)
//					.addTags(QueryTag.Builder.createQueryTag().withTagNames(list).build());
			QueryBuilder builder = QueryBuilder.createQuery()
					.addTags(QueryTag.Builder.createQueryTag().withTagNames(list).build());
			QueryResponse qResponse = ClientFactory.queryClientForTenant(tenant).queryAll(builder.build());
			String tagName = "";
			JSONObject responseJSONObject = new JSONObject();
			String timeStamp = "0";
		
			for (TagResponse resp : qResponse.getTags()) {
				tagName = resp.getName();
				for (Result res : resp.getResults()) {
					
					Double value = 0.0d;
					for (DataPoint dp : res.getDataPoints()) {
						
						//ts = dp.getTimestamp();
						timeStamp = String.format("%d", dp.getTimestamp());
						value = Double.parseDouble(dp.getValue().toString());
						
						responseJSONObject.put("timestamp", timeStamp);

						responseJSONObject.put(tagName, value);
						

					}

				}
			}
			return responseJSONObject.toString();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (PredixTimeSeriesException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

		public String ingestTimeseriesData(JSONObject TimeSeriesDataObj, String[] outputParams, JSONObject TimeSeriesTag)
				throws IOException, UnsupportedOperationException, PredixTimeSeriesException, JSONException {

			JSONObject TimeSeriesCacheObj = new JSONObject();
			TenantContext tenant = null;
			try {
				tenant = TenantContextFactory.createTenantContextFromPropertiesFile("predix-timeseries.properties");

			} catch (URISyntaxException | PredixTimeSeriesException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Long messageId = System.currentTimeMillis();
			int ingSize = 0;
			IngestionRequestBuilder ingestionBuilder = IngestionRequestBuilder.createIngestionRequest()
					.withMessageId(messageId.toString());

			/*********************************************
			 * Looping for Each Entity ----- MAIN LOOP ------
			 ********************************************/
			for (int count = 1; count <= TimeSeriesTag.length(); count++) {
				String prefix = TimeSeriesTag.get("Asset" + count).toString();
				JSONObject nodeJsonDataObj = TimeSeriesDataObj.getJSONObject(prefix);

				JSONObject nodeJsonCacheObj = new JSONObject();

				// Just to make upper case and Transmit it to another service for
				// in-memory cache
				for (int attributeId = 0; attributeId < outputParams.length; attributeId++) {
					nodeJsonCacheObj.put(outputParams[attributeId].toUpperCase(),
							nodeJsonDataObj.get(outputParams[attributeId]));
				}
				// ToDo - Follow camel case naming convention the only difference is
				// Upper case
				TimeSeriesCacheObj.put(prefix, nodeJsonCacheObj);

				for (int i = 0; i < nodeJsonDataObj.length(); i++) {
					// ToDo - Follow camel case naming convention and Append OP at
					// the end of the tag for output
					ingestionBuilder.addIngestionTag(IngestionTag.Builder.createIngestionTag()
							.withTagName("<<Name>>")
							.addDataPoints(Arrays.asList(new DataPoint(System.currentTimeMillis(),
									nodeJsonDataObj.get(outputParams[i]), Quality.GOOD)))
							.addAttribute("datatype", "DOUBLE").build());
				}
				ingSize = ingestionBuilder.estimateMessageSize();
				if (ingSize >= ingestionSize) //for 511KB Data
				{
					String IngestionQuery = ingestionBuilder.build().get(0);
					IngestionResponse ingestionResponses = ClientFactory.ingestionClientForTenant(tenant)
							.ingest(IngestionQuery);
					Long messageIds = System.currentTimeMillis();
					ingestionBuilder = IngestionRequestBuilder.createIngestionRequest()
							.withMessageId(messageIds.toString());
				}
			}
			if (ingestionBuilder.estimateMessageSize() > 0) {
			String IngestionQuery = ingestionBuilder.build().get(0);
			IngestionResponse ingestionResponse = ClientFactory.ingestionClientForTenant(tenant).ingest(IngestionQuery);
			}
			return TimeSeriesCacheObj.toString();
		}
	/**
	 * 
	 * @param str
	 */

	@Override
	public void setEnvironment(Environment env) {
		// this.ingestionSize =
		// Integer.parseInt(env.getProperty("predix.timeseries.maxIngestionMessageSize"));

	}

}
