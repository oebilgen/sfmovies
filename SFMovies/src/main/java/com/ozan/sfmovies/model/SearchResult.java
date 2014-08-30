package com.ozan.sfmovies.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchResult implements Serializable, Comparable<SearchResult>
{
	private static final long serialVersionUID = 1L;
	private String value;
	@JsonProperty("data")
	private DataType dataType;

	public SearchResult()
	{

	}

	public SearchResult(final String value, final DataType dataType)
	{
		if (value == null)
		{
			throw new IllegalArgumentException("Null value");
		}
		if (dataType == null)
		{
			throw new IllegalArgumentException("Null dataType");
		}
		this.value = value;
		this.dataType = dataType;
	}

	public String getValue()
	{
		return this.value;
	}

	public void setValue(final String value)
	{
		this.value = value;
	}

	public DataType getDataType()
	{
		return this.dataType;
	}

	public void setData(final DataType dataType)
	{
		this.dataType = dataType;
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
		result = (prime * result) + (this.dataType == null ? 0 : this.dataType.hashCode());
		result = (prime * result) + (this.value == null ? 0 : this.value.hashCode());
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
		final SearchResult other = (SearchResult) obj;
		if (this.dataType != other.dataType)
		{
			return false;
		}
		if (this.value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		}
		else if (!this.value.equals(other.value))
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "SearchResult [value=" + this.value + ", dataType=" + this.dataType + "]";
	}

	@Override
	public int compareTo(final SearchResult o)
	{
		try
		{
			if (o == this)
			{
				return 0;
			}
			final int valueComparison = this.value.compareTo(o.value);
			if (valueComparison == 0)
			{
				return this.dataType.compareTo(o.dataType);
			}
			else
			{
				return valueComparison;
			}
		}
		catch (final Exception e)
		{
			System.err.println("this:" + this);
			System.err.println("o:" + o);
			e.printStackTrace();
			return 0;
		}
	}

}
