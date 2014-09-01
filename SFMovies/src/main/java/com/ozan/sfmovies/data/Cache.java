/**
 * (c) San Francisco Movies - Author: Ozan Eren Bilgen (oebilgen@gmail.com)
 */
package com.ozan.sfmovies.data;

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
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ozan.sfmovies.model.DataType;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.MovieLocation;
import com.ozan.sfmovies.model.MovieSummary;
import com.ozan.sfmovies.model.RawMovieData;
import com.ozan.sfmovies.model.SearchResult;
import com.ozan.sfmovies.model.SearchResultCollection;
import com.ozan.sfmovies.model.geodata.AddressConverter;
import com.ozan.sfmovies.model.geodata.Coordinates;
import com.ozan.sfmovies.utilities.Utilities;

public class Cache
{
	private static Logger logger = LoggerFactory.getLogger(Cache.class);
	private final List<Movie> movies = new ArrayList<>();
	@Autowired
	private AddressConverter addressConverter;
	private URL cdnFormattedDataFile;
	private URL cdnRawDataFile;

	@PostConstruct
	public void initializeCache()
	{
		this.loadFormattedCdnData();
	}

	/**
	 * Loads formatted data from CDN to in-memory cache.
	 */
	public void loadFormattedCdnData()
	{
		final ArrayList<Movie> movies;
		try
		{
			final BufferedReader reader = this.createReaderFromUrl(this.cdnFormattedDataFile);
			final ObjectMapper mapper = new ObjectMapper();
			logger.debug("Reading data...");
			movies = mapper.readValue(reader, new TypeReference<List<Movie>>()
			{
			});
			logger.debug("CDN formatted data read.");
		}
		catch (final IOException e)
		{
			logger.error("Unable to read formatted movie data.", e);
			return;
		}
		for (final Movie newMovie : movies)
		{
			newMovie.getMovieSummary().getMovieLocations().remove(null);
			this.addMovie(newMovie);
		}
	}

	/**
	 * Loads raw movie data to in-memory cache.
	 */
	public void loadRawData()
	{
		final ArrayList<RawMovieData> newRawMovies;
		try
		{
			final BufferedReader reader = this.createReaderFromUrl(this.cdnRawDataFile);
			final ObjectMapper mapper = new ObjectMapper();
			logger.debug("Reading raw data...");
			newRawMovies = mapper.readValue(reader, new TypeReference<List<RawMovieData>>()
			{
			});
			logger.debug("CDN raw data read.");
		}
		catch (final IOException e)
		{
			logger.error("Unable to read raw movie data.", e);
			return;
		}
		for (final RawMovieData newRawMovie : newRawMovies)
		{
			this.addRawMovieData(newRawMovie);
		}
	}

	/**
	 * Creates a stream reader for a given URL
	 *
	 * @param url
	 *            the URL to read from
	 * @return a stream reader
	 * @throws IOException
	 *             if connection fails
	 */
	private BufferedReader createReaderFromUrl(final URL url) throws IOException
	{
		logger.debug("Opening connection to [" + url + "]...");
		final URLConnection urlConnection = url.openConnection();
		logger.debug("Connection established.");
		final InputStream inputStream = urlConnection.getInputStream();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		return reader;
	}

	/**
	 * the list of movies in the vincinity
	 *
	 * @param southWestCorner
	 *            South West corner of the map
	 * @param northEastCorner
	 *            North East corner of the map
	 * @return list of movies
	 */
	public List<MovieSummary> getMovieSummaries(final Coordinates southWestCorner, final Coordinates northEastCorner)
	{
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			final List<MovieLocation> matchingLocations = movie.isWithinBoundries(southWestCorner, northEastCorner);
			if (matchingLocations != null)
			{
				result.add(new MovieSummary(movie, matchingLocations));
			}
		}
		return result;
	}

	/**
	 * List of movies in a given year
	 *
	 * @param releaseYear
	 *            The 4 digit year
	 * @return list of movies
	 */
	public List<MovieSummary> getMoviesByReleaseYear(final Integer releaseYear)
	{
		if (releaseYear == null)
		{
			return null;
		}
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			if (releaseYear.equals(movie.getReleaseYear()))
			{
				result.add(movie.getMovieSummary());
			}
		}
		return result;
	}

	/**
	 * List of movies by actor
	 *
	 * @param actor
	 *            the actor's name
	 * @return list of movies
	 */
	public List<MovieSummary> getMoviesByActor(final String actor)
	{
		if ((actor == null) || actor.isEmpty())
		{
			return null;
		}
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			for (final String knownActor : movie.getActors())
			{
				if (this.stringMatch(knownActor, actor))
				{
					result.add(movie.getMovieSummary());
					break;
				}
			}
		}
		return result;
	}

	/**
	 * List of movies by director
	 *
	 * @param director
	 *            director's name
	 * @return list of movies
	 */
	public List<MovieSummary> getMoviesByDirector(final String director)
	{
		if ((director == null) || director.isEmpty())
		{
			return null;
		}
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			if (this.directorMatch(director, movie))
			{
				result.add(movie.getMovieSummary());
			}
		}
		return result;
	}

	private boolean directorMatch(final String director, final Movie movie)
	{
		return this.stringMatch(movie.getDirector(), director);
	}

	/**
	 * List of movies by distributor
	 *
	 * @param distributor
	 *            distributor's name
	 * @return list of movies
	 */
	public List<MovieSummary> getMoviesByDistributor(final String distributor)
	{
		if ((distributor == null) || distributor.isEmpty())
		{
			return null;
		}
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			if (this.distributorMatch(distributor, movie))
			{
				result.add(movie.getMovieSummary());
			}
		}
		return result;
	}

	private boolean distributorMatch(final String distributor, final Movie movie)
	{
		return this.stringMatch(movie.getDistributor(), distributor);
	}

	/**
	 * List of movies by production company
	 *
	 * @param productionCompany
	 *            production company's name
	 * @return list of movies
	 */
	public List<MovieSummary> getMoviesByProductionCompany(final String productionCompany)
	{
		if ((productionCompany == null) || productionCompany.isEmpty())
		{
			return null;
		}
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			if (this.productionCompanyMatch(productionCompany, movie))
			{
				result.add(movie.getMovieSummary());
			}
		}
		return result;
	}

	private boolean productionCompanyMatch(final String productionCompany, final Movie movie)
	{
		return this.stringMatch(movie.getProductionCompany(), productionCompany);
	}

	/**
	 * List of movies by title
	 *
	 * @param title
	 *            movie title
	 * @return list of movies
	 */
	public List<MovieSummary> getMoviesByTitle(final String title)
	{
		if ((title == null) || title.isEmpty())
		{
			return null;
		}
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			if (this.titleMatch(title, movie))
			{
				result.add(movie.getMovieSummary());
			}
		}
		return result;
	}

	private boolean titleMatch(final String title, final Movie movie)
	{
		return this.stringMatch(movie.getMovieSummary().getTitle(), title);
	}

	/**
	 * List of movies by writer
	 *
	 * @param writer
	 *            writer's name
	 * @return list of movies
	 */
	public List<MovieSummary> getMoviesByWriter(final String writer)
	{
		if ((writer == null) || writer.isEmpty())
		{
			return null;
		}
		final List<MovieSummary> result = new ArrayList<>();
		for (final Movie movie : this.movies)
		{
			if (this.writerMatch(writer, movie))
			{
				result.add(movie.getMovieSummary());
			}
		}
		return result;
	}

	private boolean writerMatch(final String writer, final Movie movie)
	{
		return this.stringMatch(movie.getWriter(), writer);
	}

	/**
	 * Returns a given movie
	 *
	 * @param movieId
	 *            ID of the movie
	 * @return the movie
	 */
	public Movie getMovieById(final UUID movieId)
	{
		if (movieId == null)
		{
			return null;
		}
		for (final Movie movie : this.movies)
		{
			if (movie.getMovieSummary().getId().equals(movieId))
			{
				return movie;
			}
		}
		return null;
	}

	/**
	 * Converts an address into a movie location via Google Maps service.
	 *
	 * @param rawAddress
	 *            the unformatted address
	 * @return the coordinates and formatted address
	 */
	private MovieLocation queryLocation(final String rawAddress)
	{
		if (rawAddress == null)
		{
			return null;
		}
		for (int i = 0; i < 3; i++)
		{
			final MovieLocation movieLocation = this.addressConverter.convertAddressToMovieLocation(rawAddress + ", San Francisco, CA, USA");

			try
			{
				// Avoid Google Maps API rate limit
				Thread.sleep(200);
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}

			if (movieLocation == null)
			{
				logger.debug("No movie location found...");
			}
			else
			{
				logger.debug("Movie location found: " + movieLocation);
				return movieLocation;
			}
		}

		return null;
	}

	/**
	 * Adds the movie if it isn't known or adds the movie location to the
	 * current movie
	 *
	 * @param newMovie
	 *            a new movie
	 */
	public void addMovie(final Movie newMovie)
	{
		if (newMovie.getMovieSummary().getTitle().equals("The Presidio"))
		{
			logger.debug("The Presidio!!");
		}
		// Rather use loop than contains method to add a new location
		for (final Movie existingMovie : this.movies)
		{
			if (existingMovie.getMovieSummary().getTitle().equals("The Presidio"))
			{
				logger.debug("The Presidio 2!!");
			}
			if (existingMovie.equals(newMovie))
			{
				logger.debug(existingMovie.getMovieSummary().getTitle() + " exists.");
				for (final MovieLocation newMovieLocation : newMovie.getMovieSummary().getMovieLocations())
				{
					if ((newMovieLocation != null) && !existingMovie.getMovieSummary().getMovieLocations().contains(newMovieLocation))
					{
						existingMovie.getMovieSummary().getMovieLocations().add(newMovieLocation);
					}
				}
				return;
			}
		}
		if (newMovie.getMovieSummary().getTitle().equals("The Presidio"))
		{
			logger.debug(newMovie.getMovieSummary().getTitle() + " is new, adding.");
		}
		this.movies.add(newMovie);
	}

	/**
	 * Converts a sfdata.org movie into sfmovies.org format, adds the movie if
	 * it isn't known or adds the movie location to the current movie
	 *
	 * @param newRawMovie
	 *            a sfdata.org movie
	 */
	public void addRawMovieData(final RawMovieData newRawMovie)
	{
		if (newRawMovie == null)
		{
			return;
		}
		for (final Movie existingMovie : this.movies)
		{
			if (existingMovie.equals(newRawMovie))
			{
				final MovieLocation newMovieLocation = this.queryLocation(newRawMovie.getLocations());
				if (newMovieLocation == null)
				{
					return;
				}
				newMovieLocation.setFunFacts(Utilities.trim(newRawMovie.getFunFacts()));
				// We have an existing movie. We should check if that object has
				// the location, otherwise add it to the array.
				if (existingMovie.getMovieSummary().getMovieLocations().contains(newMovieLocation))
				{
					logger.debug("Location [" + newMovieLocation + "] exists in Movie [" + existingMovie.getMovieSummary().getTitle()
							+ "] does contain this location.");
				}
				else
				{
					logger.debug("Movie [" + existingMovie.getMovieSummary().getTitle() + "] does not contain the location [" + newMovieLocation
							+ "], adding...");
					existingMovie.getMovieSummary().getMovieLocations().add(newMovieLocation);
				}
				return;
			}
		}
		logger.debug("Movie [" + newRawMovie.getTitle() + "] is new.");
		final Movie newMovie = new Movie(newRawMovie);
		final MovieLocation newMovieLocation = this.queryLocation(newRawMovie.getLocations());
		if (newMovieLocation != null)
		{
			newMovieLocation.setFunFacts(Utilities.trim(newRawMovie.getFunFacts()));
			newMovie.addMovieLocation(newMovieLocation);
		}
		logger.debug("New movie: " + newMovie.toString());
		this.movies.add(newMovie);
	}

	/**
	 * Clears the cache.
	 */
	public void clear()
	{
		this.movies.clear();
	}

	/**
	 * Performs a free text search in the movie cache.
	 *
	 * @param originalQuery
	 *            the query submitted by REST
	 * @return list of matching parameter and its type
	 */
	public SearchResultCollection search(final String originalQuery)
	{
		if ((originalQuery == null) || originalQuery.isEmpty())
		{
			return null;
		}
		final String query = "(?i:.*" + originalQuery + ".*)";
		final Set<SearchResult> searchResult = new HashSet<>();
		for (final Movie movie : this.movies)
		{
			for (final String knownActor : movie.getActors())
			{
				if (this.stringMatch(knownActor, query))
				{
					searchResult.add(new SearchResult(knownActor, DataType.ACTOR));
				}
			}
			if (this.directorMatch(query, movie))
			{
				final String director = movie.getDirector();
				searchResult.add(new SearchResult(director, DataType.DIRECTOR));
			}
			if (this.distributorMatch(query, movie))
			{
				final String distributor = movie.getDistributor();
				searchResult.add(new SearchResult(distributor, DataType.DISTRIBUTOR));
			}
			if (this.productionCompanyMatch(query, movie))
			{
				final String productionCompany = movie.getProductionCompany();
				searchResult.add(new SearchResult(productionCompany, DataType.PRODUCTION_COMPANY));
			}
			if (this.titleMatch(query, movie))
			{
				final String title = movie.getMovieSummary().getTitle();
				searchResult.add(new SearchResult(title, DataType.TITLE));
			}
			if (this.writerMatch(query, movie))
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
		if ((haystack == null) || (needle == null))
		{
			return false;
		}
		return haystack.matches(needle);
	}

	/**
	 * @param cdnFormattedDataFile
	 *            the cdnFormattedDataFile to set
	 */
	public void setCdnFormattedDataFile(final URL cdnFormattedDataFile)
	{
		this.cdnFormattedDataFile = cdnFormattedDataFile;
	}

	/**
	 * @param cdnRawDataFile
	 *            the cdnRawDataFile to set
	 */
	public void setCdnRawDataFile(final URL cdnRawDataFile)
	{
		this.cdnRawDataFile = cdnRawDataFile;
	}

	/**
	 * @return the movies
	 */
	public List<Movie> getMovies()
	{
		return this.movies;
	}

}
