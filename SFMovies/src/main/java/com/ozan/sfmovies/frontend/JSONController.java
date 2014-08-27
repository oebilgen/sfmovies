package com.ozan.sfmovies.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ozan.sfmovies.model.Cache;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.MovieSummary;
import com.ozan.sfmovies.model.MovieSummaryResponse;
import com.ozan.sfmovies.model.RawMovieData;
import com.ozan.sfmovies.model.SearchResultCollection;
import com.ozan.sfmovies.model.geodata.Coordinates;

@Controller
@RequestMapping("/")
public class JSONController
{
	@Autowired
	private Cache cache;

	@RequestMapping(value = "newMovie", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newMovie(@RequestBody final Movie newMovie)
	{
		this.cache.addMovie(newMovie);
	}

	@RequestMapping(value = "newMovies", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newMovies(@RequestBody final ArrayList<Movie> newMovies)
	{
		for (final Movie newMovie : newMovies)
		{
			this.cache.addMovie(newMovie);
		}
	}

	@RequestMapping(value = "newRawMovie", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newRawMovie(@RequestBody final RawMovieData newRawMovie)
	{
		this.cache.addRawMovieData(newRawMovie);
	}

	@RequestMapping(value = "newRawMovies", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newRawMovies(@RequestBody final ArrayList<RawMovieData> newRawMovies)
	{
		for (final RawMovieData newRawMovie : newRawMovies)
		{
			this.cache.addRawMovieData(newRawMovie);
		}
	}

	@RequestMapping(value = "clear", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void clear()
	{
		this.cache.clear();
	}

	@RequestMapping(value = "reloadCdnData", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> reloadCdnData()
	{
		this.cache.clear();
		this.cache.loadFormattedCdnData();
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(this.cache.getMovieSummaries(null, null)), HttpStatus.OK);
	}

	@RequestMapping(value = "printMovies", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Movie> loadCdnData()
	{
		return this.cache.getMovies();
	}

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

	@RequestMapping(value = "movieId/{movieId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> movieId(@PathVariable("movieId") final UUID movieId)
	{
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(this.cache.getMovieById(movieId)), HttpStatus.OK);
	}

	@RequestMapping(value = "movieDetail/{movieId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Movie movieDetail(@PathVariable("movieId") final UUID movieId)
	{
		return this.cache.getMovieById(movieId);
	}

	@RequestMapping(value = "search", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<SearchResultCollection> search(@RequestParam(value = "query", required = false) final String query)
	{
		final SearchResultCollection searchResultCollection = this.cache.search(query);
		return new ResponseEntity<SearchResultCollection>(searchResultCollection, HttpStatus.OK);
	}

	@RequestMapping(value = "actor/{actor}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> actorFilter(@PathVariable("actor") final String actor)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByActor(actor);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "director/{director}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> directorFilter(@PathVariable("director") final String director)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByDirector(director);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "distributor/{distributor}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> distributorFilter(@PathVariable("distributor") final String distributor)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByDistributor(distributor);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "productionCompany/{productionCompany}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> productionCompanyFilter(@PathVariable("productionCompany") final String productionCompany)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByProductionCompany(productionCompany);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "title/{title}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> titleFilter(@PathVariable("title") final String title)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByTitle(title);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "writer/{writer}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> writerFilter(@PathVariable("writer") final String writer)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByWriter(writer);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "releaseYear/{releaseYear}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<MovieSummaryResponse> productionCompanyFilter(@PathVariable("releaseYear") final Integer releaseYear)
	{
		final List<MovieSummary> movies = this.cache.getMoviesByReleaseYear(releaseYear);
		return new ResponseEntity<MovieSummaryResponse>(new MovieSummaryResponse(movies), HttpStatus.OK);
	}
}
