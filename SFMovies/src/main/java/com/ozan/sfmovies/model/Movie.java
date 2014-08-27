package com.ozan.sfmovies.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozan.sfmovies.model.geodata.Coordinates;
import com.ozan.sfmovies.utilities.Utilities;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Movie
{
	private static Logger logger = LoggerFactory.getLogger(Movie.class);
	private static final long serialVersionUID = 1L;
	private MovieSummary movieSummary;
	private List<String> actors;
	private String director;
	private String distributor;
	private String productionCompany;
	private Integer releaseYear;
	private String writer;

	public Movie()
	{
		this.movieSummary = new MovieSummary();
		this.actors = new ArrayList<>();
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
		this.productionCompany = Utilities.trim(newRawMovie.getProductionCompany());
		this.releaseYear = newRawMovie.getReleaseYear();
		this.getMovieSummary().setTitle(Utilities.trim(newRawMovie.getTitle()));
		this.writer = Utilities.trim(newRawMovie.getWriter());

	}

	/**
	 * @return the movieSummary
	 */
	public MovieSummary getMovieSummary()
	{
		return this.movieSummary;
	}

	/**
	 * @param movieSummary
	 *            the movieSummary to set
	 */
	public void setMovieSummary(final MovieSummary movieSummary)
	{
		this.movieSummary = movieSummary;
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
	 * @param newMovieLocation
	 *            new location to add
	 */
	public void addMovieLocation(final MovieLocation newMovieLocation)
	{
		if (newMovieLocation == null)
		{
			return;
		}
		for (final MovieLocation location : this.movieSummary.getMovieLocations())
		{
			if (location.equals(newMovieLocation))
			{
				return;
			}
		}
		this.movieSummary.getMovieLocations().add(newMovieLocation);
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
		return "Movie [movieSummary=" + this.movieSummary + ", actors=" + this.actors + ", director=" + this.director + ", distributor=" + this.distributor
				+ ", productionCompany=" + this.productionCompany + ", releaseYear=" + this.releaseYear + ", writer=" + this.writer + "]";
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
		result = (prime * result) + ((this.actors == null) ? 0 : this.actors.hashCode());
		result = (prime * result) + ((this.director == null) ? 0 : this.director.hashCode());
		result = (prime * result) + ((this.distributor == null) ? 0 : this.distributor.hashCode());
		result = (prime * result) + ((this.movieSummary == null) ? 0 : this.movieSummary.hashCode());
		result = (prime * result) + ((this.productionCompany == null) ? 0 : this.productionCompany.hashCode());
		result = (prime * result) + ((this.releaseYear == null) ? 0 : this.releaseYear.hashCode());
		result = (prime * result) + ((this.writer == null) ? 0 : this.writer.hashCode());
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
		if (this.movieSummary == null)
		{
			if (other.movieSummary != null)
			{
				return false;
			}
		}
		else if (!this.movieSummary.equals(other.movieSummary))
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
		if ((this.actors == null) || (this.actors.size() == 0))
		{
			if ((rawMovieActor1 != null) || (rawMovieActor2 != null) || (rawMovieActor3 != null))
			{
				return false;
			}
		}
		else
		{
			if ((rawMovieActor1 != null) && !this.actors.contains(rawMovieActor1))
			{
				return false;
			}
			if ((rawMovieActor2 != null) && !this.actors.contains(rawMovieActor2))
			{
				return false;
			}
			if ((rawMovieActor3 != null) && !this.actors.contains(rawMovieActor3))
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
		if (this.getMovieSummary().getTitle() == null)
		{
			if (rawMovieData.getTitle() != null)
			{
				return false;
			}
		}
		else if (!this.getMovieSummary().getTitle().equals(Utilities.trim(rawMovieData.getTitle())))
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

	public List<MovieLocation> isWithinBoundries(final Coordinates southWestCorner, final Coordinates northEastCorner)
	{
		if ((southWestCorner == null) || (northEastCorner == null))
		{
			return this.movieSummary.getMovieLocations();
		}
		List<MovieLocation> matchingLocations = null;
		final Double southWestCornerLatitude = southWestCorner.getLatitude();
		final Double southWestCornerLongitude = southWestCorner.getLongitude();
		final Double northEastCornerLatitude = northEastCorner.getLatitude();
		final Double northEastCornerLongitude = northEastCorner.getLongitude();
		for (final MovieLocation movieLocation : this.getMovieSummary().getMovieLocations())
		{
			if (movieLocation == null)
			{
				continue;
			}
			final Double movieLocationLatitude = movieLocation.getLatitude();
			final Double movieLocationLongitude = movieLocation.getLongitude();
			final boolean eastBound = movieLocationLongitude < northEastCornerLongitude;
			final boolean westBound = movieLocationLongitude > southWestCornerLongitude;
			final boolean inLongitude;
			if (northEastCornerLongitude < southWestCornerLongitude)
			{
				inLongitude = eastBound || westBound;
			}
			else
			{
				inLongitude = eastBound && westBound;
			}
			final boolean inLatitude = (movieLocationLatitude > southWestCornerLatitude) && (movieLocationLatitude < northEastCornerLatitude);
			if (inLongitude && inLatitude)
			{
				logger.debug(movieLocation + " is within SW:" + southWestCorner + " NE:" + northEastCorner);
				if (matchingLocations == null)
				{
					matchingLocations = new ArrayList<>();
				}
				matchingLocations.add(movieLocation);
			}
		}
		if (matchingLocations == null)
		{
			logger.debug("No movie location within SW:" + southWestCorner + " NE:" + northEastCorner);
		}
		return matchingLocations;
	}
}
