package com.ozan.sfmovies.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Location
{
	@JsonProperty("lat")
	private Double latitude;
	@JsonProperty("lng")
	private Double longitude;
	private String fullAddress;

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

	public String getFullAddress()
	{
		return this.fullAddress;
	}

	public void setFullAddress(final String fullAddress)
	{
		this.fullAddress = fullAddress;
	}

	@Override
	public String toString()
	{
		return "Coordinates [latitude=" + this.latitude + ", longitude=" + this.longitude + ", fullAddress=" + this.fullAddress + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.fullAddress == null ? 0 : this.fullAddress.hashCode());
		result = prime * result + (this.latitude == null ? 0 : this.latitude.hashCode());
		result = prime * result + (this.longitude == null ? 0 : this.longitude.hashCode());
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
		final Location other = (Location) obj;
		if (this.fullAddress == null)
		{
			if (other.fullAddress != null)
			{
				return false;
			}
		}
		else if (!this.fullAddress.equals(other.fullAddress))
		{
			return false;
		}
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
