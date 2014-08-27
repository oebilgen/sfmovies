package com.ozan.sfmovies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MovieSummaryResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Boolean success = false;
	private String errorMessage = null;
	private List<MovieSummary> data = null;

	public MovieSummaryResponse(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * Create a success message
	 *
	 * @param data
	 *            list of matching movies
	 */
	public MovieSummaryResponse(final List<MovieSummary> data)
	{
		if ((data == null) || (data.size() == 0))
		{
			this.errorMessage = "No matching movies.";
		}
		else
		{
			this.success = true;
			this.data = data;
		}
	}

	public MovieSummaryResponse(final Movie movie)
	{
		if (movie == null)
		{
			this.errorMessage = "Movie not found.";
		}
		else
		{
			this.success = true;
			this.data = new ArrayList<MovieSummary>();
			this.data.add(movie.getMovieSummary());
		}
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
	public List<MovieSummary> getData()
	{
		return this.data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(final List<MovieSummary> data)
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
		return "MovieSummaryResponse [success=" + this.success + ", errorMessage=" + this.errorMessage + ", data=" + this.data + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((this.data == null) ? 0 : this.data.hashCode());
		result = (prime * result) + ((this.errorMessage == null) ? 0 : this.errorMessage.hashCode());
		result = (prime * result) + ((this.success == null) ? 0 : this.success.hashCode());
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
		if (!super.equals(obj))
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final MovieSummaryResponse other = (MovieSummaryResponse) obj;
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
