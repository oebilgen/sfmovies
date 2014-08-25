/**
 * Based on the work of @author Abhishek Somani
 */
package com.ozan.sfmovies.model.geodata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozan.sfmovies.model.Location;

public class AddressConverter
{
	private static Logger logger = LoggerFactory.getLogger(AddressConverter.class);
	private static final String googleMapsApiEndpoint = "http://maps.googleapis.com/maps/api/geocode/json?address=";
	private static final String sensorSetting = "&sensor=false";
	private static final String encoding = "UTF-8";

	public Location convertAddressToLocation(final String address)
	{
		final GoogleResponse googleResponse;

		if (address == null)
		{
			logger.error("Address is null.");
			return null;
		}

		try
		{
			final URL url = new URL(googleMapsApiEndpoint + URLEncoder.encode(address, encoding) + AddressConverter.sensorSetting);
			logger.debug("Querying the location for: " + url.toString());
			final URLConnection conn = url.openConnection();

			final InputStream in = conn.getInputStream();
			final ObjectMapper mapper = new ObjectMapper();
			googleResponse = mapper.readValue(in, GoogleResponse.class);
			in.close();
		}
		catch (final IOException e)
		{
			logger.error("Unable to read data from Google Servers for address [" + address + "]", e);
			return null;
		}
		final String errorMessage = googleResponse.getErrorMessage();
		if (errorMessage != null)
		{
			logger.error("Error: " + errorMessage);
			return null;
		}
		final Result[] results = googleResponse.getResults();
		if (results == null || results.length == 0)
		{
			logger.error("No results for address [" + address + "]");
			return null;
		}
		final Geometry geometry = results[0].getGeometry();
		if (geometry == null)
		{
			logger.error("Cannot determine the geometry for address [" + address + "]");
			return null;
		}
		final Location location = geometry.getLocation();
		if (location == null)
		{
			logger.error("Cannot determine the location for address [" + address + "]");
			return null;
		}
		final String formattedAddress = results[0].getFormattedAddress();
		if (formattedAddress == null)
		{
			location.setFullAddress(address);
		}
		else
		{
			location.setFullAddress(formattedAddress);
		}
		return location;
	}

	public GoogleResponse convertLocationToAddress(final String latlongString) throws IOException
	{
		final URL url = new URL(googleMapsApiEndpoint + "?latlng=" + URLEncoder.encode(latlongString, encoding) + AddressConverter.sensorSetting);
		final URLConnection connection = url.openConnection();

		final InputStream in = connection.getInputStream();
		final ObjectMapper mapper = new ObjectMapper();
		final GoogleResponse response = mapper.readValue(in, GoogleResponse.class);
		in.close();
		return response;
	}
}
