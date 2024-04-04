package com.motel.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.motel.model.GeocodingResponse;

@Service
public class GeocodingService {
	private final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search";

	public double[] getCoordinates(String address) {
		String requestUrl = String.format("%s?q=%s&format=json", NOMINATIM_API_URL, address);
		RestTemplate restTemplate = new RestTemplate();
		GeocodingResponse[] response = restTemplate.getForObject(requestUrl, GeocodingResponse[].class);
		if (response != null && response.length > 0) {
			double latitude = Double.parseDouble(response[0].getLat());
			double longitude = Double.parseDouble(response[0].getLon());
			return new double[] { latitude, longitude };
		}
		return null;
	}
}
