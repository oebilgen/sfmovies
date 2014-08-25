package com.ozan.sfmovies.model.geodata;

import java.io.Serializable;
import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonProperty;

public class GoogleResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Result[] results;
	private String status;
	@JsonProperty("error_message")
	private String errorMessage;

	public Result[] getResults()
	{
		return this.results;
	}

	public void setResults(final Result[] results)
	{
		this.results = results;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getErrorMessage()
	{
		return this.errorMessage;
	}

	public void setErrorMessage(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public String toString()
	{
		return "GoogleResponse [results=" + Arrays.toString(this.results) + ", status=" + this.status + ", errorMessage=" + this.errorMessage + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.errorMessage == null ? 0 : this.errorMessage.hashCode());
		result = prime * result + Arrays.hashCode(this.results);
		result = prime * result + (this.status == null ? 0 : this.status.hashCode());
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
		final GoogleResponse other = (GoogleResponse) obj;
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
		if (!Arrays.equals(this.results, other.results))
		{
			return false;
		}
		if (this.status == null)
		{
			if (other.status != null)
			{
				return false;
			}
		}
		else if (!this.status.equals(other.status))
		{
			return false;
		}
		return true;
	}
}
