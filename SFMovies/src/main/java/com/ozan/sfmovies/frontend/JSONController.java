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
import com.ozan.sfmovies.model.RawMovieData;
import com.ozan.sfmovies.model.Response;
import com.ozan.sfmovies.model.SearchResultCollection;

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

	@RequestMapping(value = "cdnData", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> loadCdnData()
	{
		this.cache.clear();
		this.cache.loadFormattedCdnData();
		return new ResponseEntity<Response>(new Response(this.cache.getAllMovies()), HttpStatus.OK);
	}

	@RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> noFilter()
	{
		final List<Movie> movies = this.cache.getAllMovies();
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "movieId/{movieId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> movieId(@PathVariable("movieId") final UUID movieId)
	{
		if (movieId == null)
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: movieId"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesById(movieId);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
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
	public ResponseEntity<Response> actorFilter(@PathVariable("actor") final String actor)
	{
		if (actor == null || actor.isEmpty())
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: actor"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByActor(actor);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "director/{director}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> directorFilter(@PathVariable("director") final String director)
	{
		if (director == null || director.isEmpty())
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: director"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByDirector(director);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "distributor/{distributor}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> distributorFilter(@PathVariable("distributor") final String distributor)
	{
		if (distributor == null || distributor.isEmpty())
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: distributor"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByDistributor(distributor);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "productionCompany/{productionCompany}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> productionCompanyFilter(@PathVariable("productionCompany") final String productionCompany)
	{
		if (productionCompany == null || productionCompany.isEmpty())
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: productionCompany"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByProductionCompany(productionCompany);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "title/{title}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> titleFilter(@PathVariable("title") final String title)
	{
		if (title == null || title.isEmpty())
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: title"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByTitle(title);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "writer/{writer}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> writerFilter(@PathVariable("writer") final String writer)
	{
		if (writer == null || writer.isEmpty())
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: writer"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByWriter(writer);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "releaseYear/{releaseYear}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> productionCompanyFilter(@PathVariable("releaseYear") final Integer releaseYear)
	{
		if (releaseYear == null)
		{
			return new ResponseEntity<Response>(new Response("Missing parameter: releaseYear"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByReleaseYear(releaseYear);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}
}
