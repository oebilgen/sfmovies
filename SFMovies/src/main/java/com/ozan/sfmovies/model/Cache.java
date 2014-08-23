package com.ozan.sfmovies.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ozan.sfmovies.geodata.AddressConverter;

public class Cache
{
	private static Logger logger = LoggerFactory.getLogger(Cache.class);
	private final List<Movie> movies = new ArrayList<Movie>();

	@Autowired
	private AddressConverter addressConverter;

	public List<Movie> getAllMovies()
	{
		return this.movies;
	}

	public List<Movie> getMoviesByReleaseYear(final Integer releaseYear)
	{
		if (releaseYear == null)
		{
			throw new IllegalArgumentException("Null argument");
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (releaseYear.equals(movie.getReleaseYear()))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByActor(final String actorName)
	{
		if (actorName == null)
		{
			throw new IllegalArgumentException("Null argument");
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (actorName.equals(movie.getActor1()) || actorName.equals(movie.getActor2()) || actorName.equals(movie.getActor3()))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByDirector(final String directorName)
	{
		if (directorName == null)
		{
			throw new IllegalArgumentException("Null argument");
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (directorName.equals(movie.getDirector()))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByDistributor(final String distributorName)
	{
		if (distributorName == null)
		{
			throw new IllegalArgumentException("Null argument");
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (distributorName.equals(movie.getDistributor()))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByProductionCompany(final String productionCompanyName)
	{
		if (productionCompanyName == null)
		{
			throw new IllegalArgumentException("Null argument");
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (productionCompanyName.equals(movie.getProductionCompany()))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByWriter(final String writerName)
	{
		if (writerName == null)
		{
			throw new IllegalArgumentException("Null argument");
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (writerName.equals(movie.getWriter()))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public void addMovie(final Movie newMovie, final boolean doAddressResolution)
	{
		if (this.movies.contains(newMovie))
		{
			return;
		}
		if (doAddressResolution && newMovie.getLocation() == null)
		{
			for (int i = 0; i < 3; i++)
			{
				final Location coordinates = this.addressConverter.convertAddressToLocation(newMovie.getLocations() + ", San Francisco, CA, USA");

				try
				{
					// Avoid Google Maps API rate limit
					Thread.sleep(100);
				}
				catch (final InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (coordinates == null)
				{
					logger.debug("No coordinates found...");
				}
				else
				{
					newMovie.setLocation(coordinates);
					logger.debug("Coordinates found: " + coordinates);
					break;
				}
			}
		}
		// logger.debug("New movie: " + newMovie.toString());
		this.movies.add(newMovie);
	}

	public void clear()
	{
		this.movies.clear();
	}
}
