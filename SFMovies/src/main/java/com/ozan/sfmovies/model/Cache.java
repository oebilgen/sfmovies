package com.ozan.sfmovies.model;

import java.util.ArrayList;
import java.util.List;

public class Cache
{
	private final List<Movie> movies = new ArrayList<Movie>();

	public List<Movie> getAllMovies()
	{
		return this.movies;
	}

	public List<Movie> getMoviesByReleaseYear(final Integer releaseYear)
	{
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (movie.getReleaseYear().equals(releaseYear))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByActor(final String actorName)
	{
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (movie.getActor1().equals(actorName)
					|| movie.getActor2().equals(actorName)
					|| movie.getActor3().equals(actorName))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByDirector(final String directorName)
	{
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (movie.getDirector().equals(directorName))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByDistributor(final String distributorName)
	{
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (movie.getDistributor().equals(distributorName))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByProductionCompany(
			final String productionCompanyName)
	{
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (movie.getProductionCompany().equals(productionCompanyName))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public List<Movie> getMoviesByWriter(final String writerName)
	{
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (movie.getWriter().equals(writerName))
			{
				result.add(movie);
			}
		}
		return result;
	}

	public void addMovie(final Movie newMovie)
	{
		this.movies.add(newMovie);
	}

	public void clear()
	{
		this.movies.clear();
	}

	public boolean contains(final Movie newMovie)
	{
		return this.movies.contains(newMovie);
	}

}
