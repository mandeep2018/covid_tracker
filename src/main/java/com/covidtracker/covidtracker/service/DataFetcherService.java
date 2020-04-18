package com.covidtracker.covidtracker.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.covidtracker.covidtracker.model.LocationStats;

@Service
public class DataFetcherService {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	private List<LocationStats> allStats = new ArrayList<>();
	
	private static String VIRUS_DB_URL  =  "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv";

	/*
	 * 
	 * This call is without using RestTemplate. It uses the more granular HttpClient Apache APIs
	 * 
	 */
	@PostConstruct
	@Scheduled(cron="* * 1 * * *")
	public void fetchDailyData() {
		List<LocationStats> tempStats = new ArrayList<>();
		HttpUriRequest httpUriRequest = null;
		URI target = createURI(VIRUS_DB_URL);
		try {
			target = buildGetRequest(target);
			httpUriRequest = RequestBuilder.get().setUri(target).build();
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(httpUriRequest);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || 
					response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			//	LOG.info("Register message sent to [{}] for [{}].", url, model.getObject().getDisplay());
				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(response.getEntity());
				//System.out.println(EntityUtils.toString(bufferedHttpEntity));
			StringReader stringReader = new StringReader(EntityUtils.toString(bufferedHttpEntity));
				Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
				for (CSVRecord record : records) {
					LocationStats locationStat = new LocationStats();
					locationStat.setState(record.get("Province_State"));
					locationStat.setCountry(record.get("Country_Region"));
					locationStat.setActiveCases(Integer.parseInt(record.get(record.size() - 1)));
					locationStat.setDiffFromPrevDay(Integer.parseInt(record.get(record.size() - 1)) - Integer.parseInt(record.get(record.size() - 2)));
					//System.out.println(locationStat.toString());
					tempStats.add(locationStat);
				    // String customerNo = record.get("CustomerNo");
				    //String name = record.get("Name");
				}
				
				this.setAllStats(tempStats);
	       } else {
	    	   	LOG.warn("Register message sent failed. Verify below information.");
	       }
			
		} catch (URISyntaxException e) {
			LOG.error(e.getMessage(), "URI ceation exception");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	private URI buildGetRequest(URI target) throws URISyntaxException {
		return new URIBuilder(target).build();		
	}


	private URI createURI(String vIRUS_DB_URL2) {
		return URI.create(vIRUS_DB_URL2);
	}


	public List<LocationStats> getAllStats() {
		return allStats;
	}


	public void setAllStats(List<LocationStats> allStats) {
		this.allStats = allStats;
	}
}
