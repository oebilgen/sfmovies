package com.ozan.sfmovies.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ozan.sfmovies.model.Cache;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.Response;

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
		this.addMovie(newMovie);
	}

	private void addMovie(final Movie newMovie)
	{
		if (this.cache.contains(newMovie))
		{
			return;
		}
		this.cache.addMovie(newMovie);
	}

	@RequestMapping(value = "newMovies", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void newMovies(@RequestBody final ArrayList<Movie> newMovies)
	{
		for (final Movie newMovie : newMovies)
		{
			this.addMovie(newMovie);
		}
	}

	@RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> noFilter()
	{
		final List<Movie> movies = this.cache.getAllMovies();
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "clear", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> clear()
	{
		final List<Movie> movies = this.cache.getAllMovies();
		this.cache.clear();
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "actor/{actorName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> actorFilter(
			@PathVariable("actorName") final String actorName)
	{
		if (actorName == null)
		{
			return new ResponseEntity<Response>(new Response(
					"Missing parameter: actorName"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByActor(actorName);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "director/{directorName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> directorFilter(
			@PathVariable("directorName") final String directorName)
	{
		if (directorName == null)
		{
			return new ResponseEntity<Response>(new Response(
					"Missing parameter: directorName"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByDirector(directorName);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "distributor/{distributorName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> distributorFilter(
			@PathVariable("distributorName") final String distributorName)
	{
		if (distributorName == null)
		{
			return new ResponseEntity<Response>(new Response(
					"Missing parameter: distributorName"),
					HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache
				.getMoviesByDistributor(distributorName);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "productionCompany/{productionCompanyName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> productionCompanyFilter(
			@PathVariable("productionCompanyName") final String productionCompanyName)
	{
		if (productionCompanyName == null)
		{
			return new ResponseEntity<Response>(new Response(
					"Missing parameter: productionCompanyName"),
					HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache
				.getMoviesByProductionCompany(productionCompanyName);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "writer/{writerName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> writerFilter(
			@PathVariable("writerName") final String writerName)
	{
		if (writerName == null)
		{
			return new ResponseEntity<Response>(new Response(
					"Missing parameter: writerName"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache.getMoviesByWriter(writerName);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}

	@RequestMapping(value = "releaseYear/{releaseYear}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Response> productionCompanyFilter(
			@PathVariable("releaseYear") final Integer releaseYear)
	{
		if (releaseYear == null)
		{
			return new ResponseEntity<Response>(new Response(
					"Missing parameter: releaseYear"), HttpStatus.BAD_REQUEST);
		}
		final List<Movie> movies = this.cache
				.getMoviesByReleaseYear(releaseYear);
		return new ResponseEntity<Response>(new Response(movies), HttpStatus.OK);
	}
}
