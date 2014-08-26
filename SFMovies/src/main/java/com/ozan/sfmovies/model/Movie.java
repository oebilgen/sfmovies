package com.ozan.sfmovies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ozan.sfmovies.utilities.Utilities;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Movie implements Serializable
{
	private static final long serialVersionUID = 1L;
	private UUID id;
	private List<String> actors;
	private String director;
	private String distributor;
	private String funFacts;
	private String productionCompany;
	private Integer releaseYear;
	private String title;
	private String writer;
	private List<MovieLocation> movieLocations;

	public Movie()
	{
		this.id = UUID.randomUUID();
		this.actors = new ArrayList<>();
		this.movieLocations = new ArrayList<>();
	}

	public Movie(final RawMovieData newRawMovie)
	{
		this();
		if (newRawMovie.getActor1() != null)
		{
			this.actors.add(Utilities.trim(newRawMovie.getActor1()));
		}
		if (newRawMovie.getActor2() != null)
		{
			this.actors.add(Utilities.trim(newRawMovie.getActor2()));
		}
		if (newRawMovie.getActor3() != null)
		{
			this.actors.add(Utilities.trim(newRawMovie.getActor3()));
		}
		this.director = Utilities.trim(newRawMovie.getDirector());
		this.distributor = Utilities.trim(newRawMovie.getDistributor());
		this.funFacts = Utilities.trim(newRawMovie.getFunFacts());
		this.productionCompany = Utilities.trim(newRawMovie.getProductionCompany());
		this.releaseYear = newRawMovie.getReleaseYear();
		this.title = Utilities.trim(newRawMovie.getTitle());
		this.writer = Utilities.trim(newRawMovie.getWriter());

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
	 * @return the actors
	 */
	public List<String> getActors()
	{
		return this.actors;
	}

	/**
	 * @param actors
	 *            the actors to set
	 */
	public void setActors(final List<String> actors)
	{
		this.actors = actors;
	}

	/**
	 * @return the director
	 */
	public String getDirector()
	{
		return this.director;
	}

	/**
	 * @param director
	 *            the director to set
	 */
	public void setDirector(final String director)
	{
		this.director = director;
	}

	/**
	 * @return the distributor
	 */
	public String getDistributor()
	{
		return this.distributor;
	}

	/**
	 * @param distributor
	 *            the distributor to set
	 */
	public void setDistributor(final String distributor)
	{
		this.distributor = distributor;
	}

	/**
	 * @return the funFacts
	 */
	public String getFunFacts()
	{
		return this.funFacts;
	}

	/**
	 * @param funFacts
	 *            the funFacts to set
	 */
	public void setFunFacts(final String funFacts)
	{
		this.funFacts = funFacts;
	}

	/**
	 * @return the productionCompany
	 */
	public String getProductionCompany()
	{
		return this.productionCompany;
	}

	/**
	 * @param productionCompany
	 *            the productionCompany to set
	 */
	public void setProductionCompany(final String productionCompany)
	{
		this.productionCompany = productionCompany;
	}

	/**
	 * @return the releaseYear
	 */
	public Integer getReleaseYear()
	{
		return this.releaseYear;
	}

	/**
	 * @param releaseYear
	 *            the releaseYear to set
	 */
	public void setReleaseYear(final Integer releaseYear)
	{
		this.releaseYear = releaseYear;
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
	 * @return the writer
	 */
	public String getWriter()
	{
		return this.writer;
	}

	/**
	 * @param writer
	 *            the writer to set
	 */
	public void setWriter(final String writer)
	{
		this.writer = writer;
	}

	/**
	 * @return the movieLocations
	 */
	public List<MovieLocation> getMovieLocations()
	{
		return this.movieLocations;
	}

	/**
	 * @param movieLocations
	 *            the movieLocations to set
	 */
	public void setMovieLocations(final List<MovieLocation> movieLocations)
	{
		this.movieLocations = movieLocations;
	}

	/**
	 * @param newMovieLocation
	 *            new location to add
	 */
	public void addMovieLocation(final MovieLocation newMovieLocation)
	{
		if (newMovieLocation == null)
		{
			return;
		}
		for (final MovieLocation location : this.movieLocations)
		{
			if (location.equals(newMovieLocation))
			{
				return;
			}
		}
		this.movieLocations.add(newMovieLocation);
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
		return "Movie [id=" + this.id + ", actors=" + this.actors + ", director=" + this.director + ", distributor=" + this.distributor + ", funFacts="
				+ this.funFacts + ", productionCompany=" + this.productionCompany + ", releaseYear=" + this.releaseYear + ", title=" + this.title + ", writer="
				+ this.writer + ", movieLocations=" + this.movieLocations + "]";
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
		result = prime * result + (this.actors == null ? 0 : this.actors.hashCode());
		result = prime * result + (this.director == null ? 0 : this.director.hashCode());
		result = prime * result + (this.distributor == null ? 0 : this.distributor.hashCode());
		result = prime * result + (this.funFacts == null ? 0 : this.funFacts.hashCode());
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		result = prime * result + (this.movieLocations == null ? 0 : this.movieLocations.hashCode());
		result = prime * result + (this.productionCompany == null ? 0 : this.productionCompany.hashCode());
		result = prime * result + (this.releaseYear == null ? 0 : this.releaseYear.hashCode());
		result = prime * result + (this.title == null ? 0 : this.title.hashCode());
		result = prime * result + (this.writer == null ? 0 : this.writer.hashCode());
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
		final Movie other = (Movie) obj;
		if (this.actors == null)
		{
			if (other.actors != null)
			{
				return false;
			}
		}
		else if (!this.actors.equals(other.actors))
		{
			return false;
		}
		if (this.director == null)
		{
			if (other.director != null)
			{
				return false;
			}
		}
		else if (!this.director.equals(other.director))
		{
			return false;
		}
		if (this.distributor == null)
		{
			if (other.distributor != null)
			{
				return false;
			}
		}
		else if (!this.distributor.equals(other.distributor))
		{
			return false;
		}
		if (this.funFacts == null)
		{
			if (other.funFacts != null)
			{
				return false;
			}
		}
		else if (!this.funFacts.equals(other.funFacts))
		{
			return false;
		}
		if (this.id == null)
		{
			if (other.id != null)
			{
				return false;
			}
		}
		else if (!this.id.equals(other.id))
		{
			return false;
		}
		if (this.movieLocations == null)
		{
			if (other.movieLocations != null)
			{
				return false;
			}
		}
		else if (!this.movieLocations.equals(other.movieLocations))
		{
			return false;
		}
		if (this.productionCompany == null)
		{
			if (other.productionCompany != null)
			{
				return false;
			}
		}
		else if (!this.productionCompany.equals(other.productionCompany))
		{
			return false;
		}
		if (this.releaseYear == null)
		{
			if (other.releaseYear != null)
			{
				return false;
			}
		}
		else if (!this.releaseYear.equals(other.releaseYear))
		{
			return false;
		}
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
		if (this.writer == null)
		{
			if (other.writer != null)
			{
				return false;
			}
		}
		else if (!this.writer.equals(other.writer))
		{
			return false;
		}
		return true;
	}

	public boolean equals(final RawMovieData rawMovieData)
	{
		final String rawMovieActor1 = Utilities.trim(rawMovieData.getActor1());
		final String rawMovieActor2 = Utilities.trim(rawMovieData.getActor2());
		final String rawMovieActor3 = Utilities.trim(rawMovieData.getActor3());
		if (this.actors == null || this.actors.size() == 0)
		{
			if (rawMovieActor1 != null || rawMovieActor2 != null || rawMovieActor3 != null)
			{
				return false;
			}
		}
		else
		{
			if (rawMovieActor1 != null && !this.actors.contains(rawMovieActor1))
			{
				return false;
			}
			if (rawMovieActor2 != null && !this.actors.contains(rawMovieActor2))
			{
				return false;
			}
			if (rawMovieActor3 != null && !this.actors.contains(rawMovieActor3))
			{
				return false;
			}
		}
		if (this.director == null)
		{
			if (rawMovieData.getDirector() != null)
			{
				return false;
			}
		}
		else if (!this.director.equals(Utilities.trim(rawMovieData.getDirector())))
		{
			return false;
		}
		if (this.distributor == null)
		{
			if (rawMovieData.getDistributor() != null)
			{
				return false;
			}
		}
		else if (!this.distributor.equals(Utilities.trim(rawMovieData.getDistributor())))
		{
			return false;
		}
		if (this.funFacts == null)
		{
			if (rawMovieData.getFunFacts() != null)
			{
				return false;
			}
		}
		else if (!this.funFacts.equals(Utilities.trim(rawMovieData.getFunFacts())))
		{
			return false;
		}
		if (this.productionCompany == null)
		{
			if (rawMovieData.getProductionCompany() != null)
			{
				return false;
			}
		}
		else if (!this.productionCompany.equals(Utilities.trim(rawMovieData.getProductionCompany())))
		{
			return false;
		}
		if (this.releaseYear == null)
		{
			if (rawMovieData.getReleaseYear() != null)
			{
				return false;
			}
		}
		else if (!this.releaseYear.equals(rawMovieData.getReleaseYear()))
		{
			return false;
		}
		if (this.title == null)
		{
			if (rawMovieData.getTitle() != null)
			{
				return false;
			}
		}
		else if (!this.title.equals(Utilities.trim(rawMovieData.getTitle())))
		{
			return false;
		}
		if (this.writer == null)
		{
			if (rawMovieData.getWriter() != null)
			{
				return false;
			}
		}
		else if (!this.writer.equals(Utilities.trim(rawMovieData.getWriter())))
		{
			return false;
		}
		return true;
	}
}
