package com.ozan.sfmovies.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Response implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Boolean success;
	private String errorMessage;
	private List<Movie> data;

	/**
	 * Create an error message
	 * 
	 * @param errorMessage
	 *            error message
	 */
	public Response(final String errorMessage)
	{
		this.success = false;
		this.errorMessage = errorMessage;
		this.data = null;
	}

	/**
	 * Create a success message
	 * 
	 * @param data
	 *            list of matching movies
	 */
	public Response(final List<Movie> data)
	{
		this.success = true;
		this.errorMessage = null;
		this.data = data;
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess()
	{
		return this.success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(final Boolean success)
	{
		this.success = success;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return this.errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the data
	 */
	public List<Movie> getData()
	{
		return this.data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(final List<Movie> data)
	{
		this.data = data;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Response [success=" + this.success + ", errorMessage="
				+ this.errorMessage + ", data=" + this.data + "]";
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
		result = prime * result
				+ (this.data == null ? 0 : this.data.hashCode());
		result = prime
				* result
				+ (this.errorMessage == null ? 0 : this.errorMessage.hashCode());
		result = prime * result
				+ (this.success == null ? 0 : this.success.hashCode());
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
		final Response other = (Response) obj;
		if (this.data == null)
		{
			if (other.data != null)
			{
				return false;
			}
		}
		else if (!this.data.equals(other.data))
		{
			return false;
		}
		if (this.errorMessage == null)
		{
			if (other.errorMessage != null)
			{
				return false;
			}
		}
		else if (!this.errorMessage.equals(other.errorMessage))
		{
			return false;
		}
		if (this.success == null)
		{
			if (other.success != null)
			{
				return false;
			}
		}
		else if (!this.success.equals(other.success))
		{
			return false;
		}
		return true;
	}

}
