/**
 * (c) San Francisco Movies - Author: Ozan Eren Bilgen (oebilgen@gmail.com)
 */
package com.ozan.sfmovies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MovieSummary implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UUID id;
	private String title;
	private final List<MovieLocation> movieLocations;

	public MovieSummary()
	{
		this.id = UUID.randomUUID();
		this.movieLocations = new ArrayList<>();
	}

	public MovieSummary(final Movie movie, final List<MovieLocation> matchingLocations)
	{
		this.id = movie.getMovieSummary().getId();
		this.title = movie.getMovieSummary().getTitle();
		this.movieLocations = matchingLocations;
	}

	/**
	 * @return the id
	 */
	public UUID getId()
	{
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final UUID id)
	{
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * @return the movieLocations
	 */
	public List<MovieLocation> getMovieLocations()
	{
		return this.movieLocations;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MovieSummary [id=" + this.id + ", title=" + this.title + ", movieLocations=" + this.movieLocations + "]";
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
		result = (prime * result) + ((this.title == null) ? 0 : this.title.hashCode());
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
		final MovieSummary other = (MovieSummary) obj;
		if (this.title == null)
		{
			if (other.title != null)
			{
				return false;
			}
		}
		else if (!this.title.equals(other.title))
		{
			return false;
		}
		return true;
	}
}
