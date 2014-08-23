package com.ozan.sfmovies.geodata;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Result implements Serializable
{
	private static final long serialVersionUID = 1L;
	@JsonProperty("formatted_address")
	private String formattedAddress;
	@JsonProperty("partial_match")
	private boolean partialMatch;
	private Geometry geometry;
	@JsonIgnore
	private Object address_components;
	@JsonIgnore
	private Object types;

	public String getFormattedAddress()
	{
		return this.formattedAddress;
	}

	public void setFormattedAddress(final String formattedAddress)
	{
		this.formattedAddress = formattedAddress;
	}

	public boolean isPartialMatch()
	{
		return this.partialMatch;
	}

	public void setPartialMatch(final boolean partialMatch)
	{
		this.partialMatch = partialMatch;
	}

	public Geometry getGeometry()
	{
		return this.geometry;
	}

	public void setGeometry(final Geometry geometry)
	{
		this.geometry = geometry;
	}

	public Object getAddress_components()
	{
		return this.address_components;
	}

	public void setAddress_components(final Object address_components)
	{
		this.address_components = address_components;
	}

	public Object getTypes()
	{
		return this.types;
	}

	public void setTypes(final Object types)
	{
		this.types = types;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.address_components == null ? 0 : this.address_components.hashCode());
		result = prime * result + (this.formattedAddress == null ? 0 : this.formattedAddress.hashCode());
		result = prime * result + (this.geometry == null ? 0 : this.geometry.hashCode());
		result = prime * result + (this.partialMatch ? 1231 : 1237);
		result = prime * result + (this.types == null ? 0 : this.types.hashCode());
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
		final Result other = (Result) obj;
		if (this.address_components == null)
		{
			if (other.address_components != null)
			{
				return false;
			}
		}
		else if (!this.address_components.equals(other.address_components))
		{
			return false;
		}
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
		if (this.geometry == null)
		{
			if (other.geometry != null)
			{
				return false;
			}
		}
		else if (!this.geometry.equals(other.geometry))
		{
			return false;
		}
		if (this.partialMatch != other.partialMatch)
		{
			return false;
		}
		if (this.types == null)
		{
			if (other.types != null)
			{
				return false;
			}
		}
		else if (!this.types.equals(other.types))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Result [formattedAddress=" + this.formattedAddress + ", partialMatch=" + this.partialMatch + ", geometry=" + this.geometry
				+ ", address_components=" + this.address_components + ", types=" + this.types + "]";
	}
}
