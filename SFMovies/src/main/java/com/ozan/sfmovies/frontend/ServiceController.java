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

import com.ozan.sfmovies.model.Cache;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.MovieSummaryResponse;
import com.ozan.sfmovies.model.RawMovieData;

@Controller
@RequestMapping("/")
public class ServiceController
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
}
