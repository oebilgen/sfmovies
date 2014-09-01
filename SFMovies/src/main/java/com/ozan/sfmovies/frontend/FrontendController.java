/**
 * (c) San Francisco Movies - Author: Ozan Eren Bilgen (oebilgen@gmail.com)
 */
package com.ozan.sfmovies.frontend;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ozan.sfmovies.data.Cache;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.MovieSummary;
import com.ozan.sfmovies.model.MovieSummaryResponse;
import com.ozan.sfmovies.model.SearchResultCollection;
import com.ozan.sfmovies.model.geodata.Coordinates;

@Controller
@RequestMapping("/")
public class FrontendController
{
	@Autowired
	private Cache cache;

	/**
	 * Takes the south west and north east corner coordinates of the view and
	 * returns a list of movie headers (ID, title, locations) in the given
	 * boundries.
	 *
	 * @param southWestCornerString
	 *            Map's south west corner coordinate in x,y format.
	 * @param northEastCornerString
	 *            Map's north east corner coordinate in x,y format.
	 * @return The list of movies in the vincinity
	 */
	@RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> allMovies(@RequestParam(value = "swc", required = false) final String southWestCornerString,
			@RequestParam(value = "nec", required = false) final String northEastCornerString)
			{
		Coordinates southWestCorner = null, northEastCorner = null;
		if ((southWestCornerString != null) && (northEastCornerString != null))
		{
			try
			{
				southWestCorner = new Coordinates(southWestCornerString);
				northEastCorner = new Coordinates(northEastCornerString);
			}
			catch (final IllegalArgumentException e)
			{
				return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
			}
		}
		final List<MovieSummary> movies = this.cache.getMovieSummaries(southWestCorner, northEastCorner);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
			}

	/**
	 * Returns the movie header for a given movie ID. Used in deep linking (e.g.
	 * http://www.sfmovies.org/?movieId=ec772a74-87d9-4d2e-b889-96ab632641d9)
	 *
	 * @param movieId
	 *            Movie ID
	 * @return movie header
	 */
	@RequestMapping(value = "movieId/{movieId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> movieId(@PathVariable("movieId") final UUID movieId)
	{
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(this.cache.getMovieById(movieId)), HttpStatus.OK);
	}

	/**
	 * Returns all the information for a given movie ID. Used to asynchronously
	 * fill the movie information window.
	 *
	 * @param movieId
	 *            Movie ID
	 * @return full movie object
	 */
	@RequestMapping(value = "movieDetail/{movieId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Movie movieDetail(@PathVariable("movieId") final UUID movieId)
	{
		return this.cache.getMovieById(movieId);
	}

	/**
	 * Takes a search string as an input and returns a list of search results,
	 * each containing a matching item and its type (e.g. actor, director, etc.)
	 *
	 * @param query
	 *            Search query
	 * @return List of matching records
	 */
	@RequestMapping(value = "search", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<SearchResultCollection> search(@RequestParam(value = "query", required = false) final String query)
	{
		final SearchResultCollection searchResultCollection = this.cache.search(query);
		return new ResponseEntity<SearchResultCollection>(searchResultCollection, HttpStatus.OK);
	}

	/**
	 * Returns all the movies where a given actor starred.
	 *
	 * @param actor
	 *            the actor's name
	 * @return List of actor's movies
	 */
	@RequestMapping(value = "actor/{actor}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> actorFilter(@PathVariable("actor") final String actor)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByActor(actor);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	/**
	 * Returns all the movies in which the parameter was the director.
	 *
	 * @param director
	 *            the name of the director
	 * @return list of director's movies
	 */
	@RequestMapping(value = "director/{director}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> directorFilter(@PathVariable("director") final String director)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByDirector(director);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	/**
	 * Returns all the movies in which the parameter was the distributor.
	 *
	 * @param distributor
	 *            the name of the distributor
	 * @return list of distributor's movies
	 */
	@RequestMapping(value = "distributor/{distributor}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> distributorFilter(@PathVariable("distributor") final String distributor)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByDistributor(distributor);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	/**
	 * Returns all the movies in which the parameter was the production company.
	 *
	 * @param productionCompany
	 *            the name of the production company
	 * @return list of production company's movies
	 */
	@RequestMapping(value = "productionCompany/{productionCompany}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> productionCompanyFilter(@PathVariable("productionCompany") final String productionCompany)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByProductionCompany(productionCompany);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	/**
	 * Returns all the movies with matching title.
	 *
	 * @param title
	 *            the name of the movie
	 * @return list of movies with this name
	 */
	@RequestMapping(value = "title/{title}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> titleFilter(@PathVariable("title") final String title)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByTitle(title);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	/**
	 * Returns all the movies in which the parameter was the writer.
	 *
	 * @param writer
	 *            the name of the writer
	 * @return list of writer's movies
	 */
	@RequestMapping(value = "writer/{writer}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> writerFilter(@PathVariable("writer") final String writer)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByWriter(writer);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	/**
	 * Returns all the movies that were filmed in the given year.
	 * 
	 * @param releaseYear
	 *            release year
	 * @return list of movies
	 */
	@RequestMapping(value = "releaseYear/{releaseYear}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> productionCompanyFilter(@PathVariable("releaseYear") final Integer releaseYear)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByReleaseYear(releaseYear);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}
}
