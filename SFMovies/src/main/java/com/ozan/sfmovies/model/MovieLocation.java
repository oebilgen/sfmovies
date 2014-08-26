package com.ozan.sfmovies.model;

import java.io.Serializable;

public class MovieLocation implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Double latitude;
	private Double longitude;
	private String formattedAddress;

	public MovieLocation()
	{
	}

	public MovieLocation(final Double latitude, final Double longitude, final String formattedAddress)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.formattedAddress = formattedAddress;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude()
	{
		return this.latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude()
	{
		return this.longitude;
	}

	/**
	 * @return the formattedAddress
	 */
	public String getFormattedAddress()
	{
		return this.formattedAddress;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MovieLocation [latitude=" + this.latitude + ", longitude=" + this.longitude + ", formattedAddress=" + this.formattedAddress + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.formattedAddress == null ? 0 : this.formattedAddress.hashCode());
		result = prime * result + (this.latitude == null ? 0 : this.latitude.hashCode());
		result = prime * result + (this.longitude == null ? 0 : this.longitude.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		final MovieLocation other = (MovieLocation) obj;
		if (this.formattedAddress == null)
		{
			if (other.formattedAddress != null)
			{
				return false;
			}
		}
		else if (!this.formattedAddress.equals(other.formattedAddress))
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
