package com.ozan.sfmovies.model;

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

import com.ozan.sfmovies.model.geodata.AddressConverter;

public class Cache
{
	private static Logger logger = LoggerFactory.getLogger(Cache.class);
	private final List<Movie> movies = new ArrayList<Movie>();
	@Autowired
	private AddressConverter addressConverter;
	private URL cdnFormattedDataFile;
	private URL cdnRawDataFile;

	@PostConstruct
	public void initializeCache()
	{
		this.loadFormattedCdnData();
	}

	public void loadFormattedCdnData()
	{
		final ArrayList<Movie> movies;
		try
		{
			final BufferedReader reader = this.getCdnInputStream(this.cdnFormattedDataFile);
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
			newMovie.getMovieLocations().remove(null);
			this.addMovie(newMovie);
		}
	}

	public void loadRawData()
	{
		final ArrayList<RawMovieData> newRawMovies;
		try
		{
			final BufferedReader reader = this.getCdnInputStream(this.cdnRawDataFile);
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

	private BufferedReader getCdnInputStream(final URL cdnDataFile) throws IOException
	{
		logger.debug("Opening connection to CDN data file [" + cdnDataFile + "]...");
		final URLConnection urlConnection = cdnDataFile.openConnection();
		logger.debug("Connection established.");
		final InputStream inputStream = urlConnection.getInputStream();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		return reader;
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
			for (final String knownActor : movie.getActors())
			{
				if (this.stringMatch(knownActor, actor))
				{
					result.add(movie);
				}
			}
		}
		return result;
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

	public List<Movie> getMoviesById(final UUID movieId)
	{
		if (movieId == null)
		{
			return null;
		}
		final List<Movie> result = new ArrayList<Movie>();
		for (final Movie movie : this.movies)
		{
			if (movie.getId().equals(movieId))
			{
				result.add(movie);
			}
		}
		return result;
	}

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
				Thread.sleep(100);
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

	public void addMovie(final Movie newMovie)
	{
		// Rather use loop than contains method to add a new location
		for (final Movie existingMovie : this.movies)
		{
			if (existingMovie.equals(newMovie))
			{
				for (final MovieLocation newMovieLocation : newMovie.getMovieLocations())
				{
					if (newMovieLocation != null && !existingMovie.getMovieLocations().contains(newMovieLocation))
					{
						existingMovie.getMovieLocations().add(newMovieLocation);
					}
				}
				return;
			}
		}
		this.movies.add(newMovie);
	}

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
				// We have an existing movie. We should check if that object has
				// the location, otherwise add it to the array.
				if (existingMovie.getMovieLocations().contains(newMovieLocation))
				{
					logger.debug("Location [" + newMovieLocation + "] exists in Movie [" + existingMovie.getTitle() + "] does contain this location.");
					return;
				}
				else
				{
					logger.debug("Movie [" + existingMovie.getTitle() + "] does not contain the location [" + newMovieLocation + "], adding...");
					existingMovie.getMovieLocations().add(newMovieLocation);
					return;
				}
			}
		}
		logger.debug("Movie [" + newRawMovie.getTitle() + "] is new.");
		final Movie newMovie = new Movie(newRawMovie);
		final MovieLocation newMovieLocation = this.queryLocation(newRawMovie.getLocations());
		if (newMovieLocation != null)
		{
			newMovie.addMovieLocation(newMovieLocation);
		}
		logger.debug("New movie: " + newMovie.toString());
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
				final String title = movie.getTitle();
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
		if (haystack == null || needle == null)
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
}
