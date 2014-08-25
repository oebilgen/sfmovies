package com.ozan.sfmovies.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ozan.sfmovies.model.DataType;
import com.ozan.sfmovies.model.Location;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.SearchResult;
import com.ozan.sfmovies.model.SearchResultCollection;
import com.ozan.sfmovies.model.geodata.AddressConverter;

public class Cache
{
	private static Logger logger = LoggerFactory.getLogger(Cache.class);
	private final List<Movie> movies = new ArrayList<Movie>();
	@Autowired
	private AddressConverter addressConverter;
	private URL cdnDataFile;

	@PostConstruct
	public void init()
	{
		this.loadCdnData();
	}

	public void loadCdnData()
	{
		final ArrayList<Movie> movies;
		try
		{
			logger.debug("Opening connection to CDN data file [" + this.cdnDataFile + "]...");
			final URLConnection urlConnection = this.cdnDataFile.openConnection();
			logger.debug("Connection established.");
			final InputStream inputStream = urlConnection.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			final ObjectMapper mapper = new ObjectMapper();
			logger.debug("Reading data...");
			movies = mapper.readValue(reader, new TypeReference<List<Movie>>()
			{
			});
			logger.debug("CDN data read.");
		}
		catch (final IOException e)
		{
			logger.error("Unable to read movie data.", e);
			return;
		}
		for (final Movie newMovie : movies)
		{
			this.addMovie(newMovie, false);
		}
	}

	public List<Movie> getAllMovies()
	{
		return this.movies;
	}

	public List<Movie> getMoviesByReleaseYear(final Integer releaseYear)
	{
		if (releaseYear == null)
		{
			return null;
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

	public List<Movie> getMoviesByActor(final String actor)
	{
		if (actor == null || actor.isEmpty())
		{
			return null;
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (this.actor1Match(actor, movie) || this.actor2Match(actor, movie) || this.actor3Match(actor, movie))
			{
				result.add(movie);
			}
		}
		return result;
	}

	private boolean actor1Match(final String actor, final Movie movie)
	{
		return this.stringMatch(movie.getActor1(), actor);
	}

	private boolean actor2Match(final String actor, final Movie movie)
	{
		return this.stringMatch(movie.getActor2(), actor);
	}

	private boolean actor3Match(final String actor, final Movie movie)
	{
		return this.stringMatch(movie.getActor3(), actor);
	}

	public List<Movie> getMoviesByDirector(final String director)
	{
		if (director == null || director.isEmpty())
		{
			return null;
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (this.directorMatch(director, movie))
			{
				result.add(movie);
			}
		}
		return result;
	}

	private boolean directorMatch(final String director, final Movie movie)
	{
		return this.stringMatch(movie.getDirector(), director);
	}

	public List<Movie> getMoviesByDistributor(final String distributor)
	{
		if (distributor == null || distributor.isEmpty())
		{
			return null;
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (this.distributorMatch(distributor, movie))
			{
				result.add(movie);
			}
		}
		return result;
	}

	private boolean distributorMatch(final String distributor, final Movie movie)
	{
		return this.stringMatch(movie.getDistributor(), distributor);
	}

	public List<Movie> getMoviesByProductionCompany(final String productionCompany)
	{
		if (productionCompany == null || productionCompany.isEmpty())
		{
			return null;
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (this.productionCompanyMatch(productionCompany, movie))
			{
				result.add(movie);
			}
		}
		return result;
	}

	private boolean productionCompanyMatch(final String productionCompany, final Movie movie)
	{
		return this.stringMatch(movie.getProductionCompany(), productionCompany);
	}

	public List<Movie> getMoviesByTitle(final String title)
	{
		if (title == null || title.isEmpty())
		{
			return null;
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (this.titleMatch(title, movie))
			{
				result.add(movie);
			}
		}
		return result;
	}

	private boolean titleMatch(final String title, final Movie movie)
	{
		return this.stringMatch(movie.getTitle(), title);
	}

	public List<Movie> getMoviesByWriter(final String writer)
	{
		if (writer == null || writer.isEmpty())
		{
			return null;
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (this.writerMatch(writer, movie))
			{
				result.add(movie);
			}
		}
		return result;
	}

	private boolean writerMatch(final String writer, final Movie movie)
	{
		return this.stringMatch(movie.getWriter(), writer);
	}

	public void addMovie(final Movie newMovie, final boolean doAddressResolution)
	{
		if (newMovie == null)
		{
			return;
		}
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

	public SearchResultCollection search(final String originalQuery)
	{
		if (originalQuery == null || originalQuery.isEmpty())
		{
			return null;
		}
		final String query = "(?i:.*" + originalQuery + ".*)";
		final Set<SearchResult> searchResult = new HashSet<>();
		for (final Movie movie : this.movies)
		{
			if (this.actor1Match(query, movie))
			{
				final String actor1 = movie.getActor1();
				searchResult.add(new SearchResult(actor1, DataType.ACTOR));
			}
			else if (this.actor2Match(query, movie))
			{
				final String actor2 = movie.getActor2();
				searchResult.add(new SearchResult(actor2, DataType.ACTOR));
			}
			else if (this.actor3Match(query, movie))
			{
				final String actor3 = movie.getActor3();
				searchResult.add(new SearchResult(actor3, DataType.ACTOR));
			}
			else if (this.directorMatch(query, movie))
			{
				final String director = movie.getDirector();
				searchResult.add(new SearchResult(director, DataType.DIRECTOR));
			}
			else if (this.distributorMatch(query, movie))
			{
				final String distributor = movie.getDistributor();
				searchResult.add(new SearchResult(distributor, DataType.DISTRIBUTOR));
			}
			else if (this.productionCompanyMatch(query, movie))
			{
				final String productionCompany = movie.getProductionCompany();
				searchResult.add(new SearchResult(productionCompany, DataType.PRODUCTION_COMPANY));
			}
			else if (this.titleMatch(query, movie))
			{
				final String title = movie.getTitle();
				searchResult.add(new SearchResult(title, DataType.TITLE));
			}
			else if (this.writerMatch(query, movie))
			{
				final String writer = movie.getWriter();
				searchResult.add(new SearchResult(writer, DataType.WRITER));
			}
		}
		final SearchResultCollection searchResultCollection = new SearchResultCollection();
		final Iterator<SearchResult> iterator = searchResult.iterator();
		while (iterator.hasNext())
		{
			searchResultCollection.add(iterator.next());
		}
		searchResultCollection.sort();
		return searchResultCollection;
	}

	private boolean stringMatch(final String haystack, final String needle)
	{
		if (haystack == null || needle == null)
		{
			return false;
		}
		return haystack.matches(needle);
	}

	/**
	 * @param cdnDataFile
	 *            the cdnDataFile to set
	 */
	public void setCdnDataFile(final URL cdnDataFile)
	{
		this.cdnDataFile = cdnDataFile;
	}
}
