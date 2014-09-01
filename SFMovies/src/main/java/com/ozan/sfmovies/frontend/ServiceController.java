/**
 * (c) San Francisco Movies - Author: Ozan Eren Bilgen (oebilgen@gmail.com)
 */
package com.ozan.sfmovies.frontend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ozan.sfmovies.data.Cache;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.MovieSummaryResponse;
import com.ozan.sfmovies.model.RawMovieData;

@Controller
@RequestMapping("/")
public class ServiceController
{
	@Autowired
	private Cache cache;

	/**
	 * Adds a formatted movie to the cache. If the movie does not exist, it
	 * creates a new entry, otherwise it adds the movie locations that were
	 * previously unknown.
	 *
	 * @param newMovie
	 *            formatted movie
	 */
	@RequestMapping(value = "newMovie", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newMovie(@RequestBody final Movie newMovie)
	{
		this.cache.addMovie(newMovie);
	}

	/**
	 * Performs the same operation of /v1/movies/newMovie for a list of movies.
	 *
	 * @param newMovies
	 *            list of formatted movies
	 */
	@RequestMapping(value = "newMovies", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newMovies(@RequestBody final ArrayList<Movie> newMovies)
	{
		for (final Movie newMovie : newMovies)
		{
			this.cache.addMovie(newMovie);
		}
	}

	/**
	 * Adds an unformatted movie (from sfdata.org) to the cache. It converts the
	 * movie format by organizing actors and calculating the movie location
	 * coordinates. If the movie does not exist, it creates a new entry,
	 * otherwise it adds the movie locations that were previously unknown.
	 *
	 * @param newRawMovie
	 *            a SfData.org movie
	 */
	@RequestMapping(value = "newRawMovie", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newRawMovie(@RequestBody final RawMovieData newRawMovie)
	{
		this.cache.addRawMovieData(newRawMovie);
	}

	/**
	 * Performs the same operation of /v1/movies/newRawMovies for a list of
	 * movies.
	 *
	 * @param newRawMovies
	 *            a list of SfData.org movie
	 */
	@RequestMapping(value = "newRawMovies", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newRawMovies(@RequestBody final ArrayList<RawMovieData> newRawMovies)
	{
		for (final RawMovieData newRawMovie : newRawMovies)
		{
			this.cache.addRawMovieData(newRawMovie);
		}
	}

	/**
	 * Clears the cache
	 */
	@RequestMapping(value = "clear", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void clear()
	{
		this.cache.clear();
	}

	/**
	 * Clears the cache and reloads the data from the data source.
	 *
	 * @return list of movies
	 */
	@RequestMapping(value = "reloadCdnData", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> reloadCdnData()
	{
		this.cache.clear();
		this.cache.loadFormattedCdnData();
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(this.cache.getMovieSummaries(null, null)), HttpStatus.OK);
	}

	/**
	 * Lists all the movies.
	 *
	 * @return list of movies
	 */
	@RequestMapping(value = "printMovies", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Movie> loadCdnData()
	{
		return this.cache.getMovies();
	}
}
