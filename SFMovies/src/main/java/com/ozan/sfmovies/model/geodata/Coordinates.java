package com.ozan.sfmovies.model.geodata;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Coordinates implements Serializable
{
	private static Logger logger = LoggerFactory.getLogger(Coordinates.class);
	private static final long serialVersionUID = 1L;
	@JsonProperty("lat")
	private Double latitude;
	@JsonProperty("lng")
	private Double longitude;

	public Coordinates()
	{

	}

	public Coordinates(final String coordinateString) throws IllegalArgumentException
	{
		if (coordinateString == null)
		{
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		final String[] coordinates = coordinateString.split(",");
		if ((coordinates == null) || (coordinates.length != 2))
		{
			throw new IllegalArgumentException("Parameter must be in x,y format.");
		}
		final String latitudeString = coordinates[0].trim();
		if (latitudeString.isEmpty())
		{
			throw new IllegalArgumentException("Empty latitude value.");
		}
		final String longitudeString = coordinates[1].trim();
		if (longitudeString.isEmpty())
		{
			throw new IllegalArgumentException("Empty longitude value.");
		}
		try
		{
			this.latitude = Double.parseDouble(latitudeString);
		}
		catch (final NumberFormatException e)
		{
			throw new IllegalArgumentException("Cannot convert latitude string [" + latitudeString + "] to double.");
		}
		try
		{
			this.longitude = Double.parseDouble(longitudeString);
		}
		catch (final NumberFormatException e)
		{
			throw new IllegalArgumentException("Cannot convert longitude string [" + longitudeString + "] to double.");
		}
		logger.debug(this.toString());
	}

	public Double getLatitude()
	{
		return this.latitude;
	}

	public void setLatitude(final Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		return this.longitude;
	}

	public void setLongitude(final Double longitude)
	{
		this.longitude = longitude;
	}

	@Override
	public String toString()
	{
		return "Coordinates [latitude=" + this.latitude + ", longitude=" + this.longitude + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (this.latitude == null ? 0 : this.latitude.hashCode());
		result = (prime * result) + (this.longitude == null ? 0 : this.longitude.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final Coordinates other = (Coordinates) obj;
		if (this.latitude == null)
		{
			if (other.latitude != null)
			{
				return false;
			}
		}
		else if (!this.latitude.equals(other.latitude))
		{
			return false;
		}
		if (this.longitude == null)
		{
			if (other.longitude != null)
			{
				return false;
			}
		}
		else if (!this.longitude.equals(other.longitude))
		{
			return false;
		}
		return true;
	}
}
