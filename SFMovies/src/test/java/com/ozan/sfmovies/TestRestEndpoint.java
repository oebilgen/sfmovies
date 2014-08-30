package com.ozan.sfmovies;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Assert;
import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import com.ozan.sfmovies.model.DataType;
import com.ozan.sfmovies.model.Movie;
import com.ozan.sfmovies.model.MovieLocation;
import com.ozan.sfmovies.model.MovieSummary;
import com.ozan.sfmovies.model.MovieSummaryResponse;
import com.ozan.sfmovies.model.SearchResult;
import com.ozan.sfmovies.model.SearchResultCollection;

@RunWith(HttpJUnitRunner.class)
public class TestRestEndpoint
{
	@Rule
	public Destination destination = new Destination(this, "http://localhost:8080");

	@Context
	private Response response; // will be injected after every request

	@HttpTest(method = Method.GET, path = "/v1/movies/all")
	public void testEndpointAll() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/printMovies")
	public void testEndpointPrintMovies() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final List<Movie> restResult = mapper.readValue(body, new TypeReference<List<Movie>>()
		{
		});
		org.junit.Assert.assertTrue(restResult.size() > 0);
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/search?query=lint")
	public void testEndpointSearch() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final SearchResultCollection restResult = mapper.readValue(body, new TypeReference<SearchResultCollection>()
		{
		});
		org.junit.Assert.assertTrue(restResult.getSuggestions().size() > 0);
		for (final SearchResult searchResult : restResult.getSuggestions())
		{
			if (searchResult.getValue().equals("Clint Eastwood") && (searchResult.getDataType() == DataType.ACTOR))
			{
				return;
			}
		}
		org.junit.Assert.fail("/v1/seach?query=lint did not list \"Clint Eastwood\" as actor.");
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/movieId/ea5c7e8a-96ec-4ca7-b7ee-db704b29856b")
	public void testEndpointMovieId() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		org.junit.Assert.assertTrue(restResult.getData().size() == 1);
		org.junit.Assert.assertTrue(restResult.getData().get(0).getTitle().equals("The Maltese Falcon"));
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/movieDetail/ea5c7e8a-96ec-4ca7-b7ee-db704b29856b")
	public void testEndpointMovieDetail() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final Movie restResult = mapper.readValue(body, new TypeReference<Movie>()
		{
		});
		org.junit.Assert.assertTrue(restResult.getMovieSummary().getTitle().equals("The Maltese Falcon"));
		org.junit.Assert.assertTrue(restResult.getActors().contains("Humphrey Bogart"));
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/actor/Clint%20Eastwood")
	public void testEndpointActor() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		for (final MovieSummary movieSummary : restResult.getData())
		{
			if (movieSummary.getTitle().equals("Escape From Alcatraz"))
			{
				return;
			}
		}
		org.junit.Assert.fail("/v1/movies/actor/Clint%20Eastwood did not list \"Escape From Alcatraz\" in movies");
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/director/Woody%20Allen")
	public void testEndpointDirector() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		for (final MovieSummary movieSummary : restResult.getData())
		{
			if (movieSummary.getTitle().equals("Blue Jasmine"))
			{
				return;
			}
		}
		org.junit.Assert.fail("/v1/movies/director/Woody%20Allen did not list \"Blue Jasmine\" in movies");
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/distributor/Columbia%20Pictures")
	public void testEndpointDistributor() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		for (final MovieSummary movieSummary : restResult.getData())
		{
			if (movieSummary.getTitle().equals("The Lineup"))
			{
				return;
			}
		}
		org.junit.Assert.fail("/v1/movies/distributor/Columbia%20Pictures did not list \"The Lineup\" in movies");
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/productionCompany/Universal%20Pictures")
	public void testEndpointProductionCompany() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		for (final MovieSummary movieSummary : restResult.getData())
		{
			if (movieSummary.getTitle().equals("Hulk"))
			{
				return;
			}
		}
		org.junit.Assert.fail("/v1/movies/distributor/Universal%20Pictures did not list \"Hulk\" in movies");
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/title/Sneakers")
	public void testEndpointTitle() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		for (final MovieSummary movieSummary : restResult.getData())
		{
			final List<MovieLocation> movieLocations = movieSummary.getMovieLocations();
			for (final MovieLocation movieLocation : movieLocations)
			{
				if (movieLocation.getFormattedAddress().equals("Embarcadero, San Francisco, CA, USA"))
				{
					return;
				}
			}
		}
		org.junit.Assert.fail("/v1/movies/title/Sneakers did not list \"Embarcadero, San Francisco, CA, USA\" in movie locations");
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/writer/Dan%20Gordon")
	public void testEndpointWriter() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		for (final MovieSummary movieSummary : restResult.getData())
		{
			if (movieSummary.getTitle().equals("Murder in the First"))
			{
				return;
			}
		}
		org.junit.Assert.fail("/v1/movies/writer/Dan%20Gordon did not list \"Murder in the First\" in movies");
	}

	@HttpTest(method = Method.GET, path = "/v1/movies/releaseYear/1998")
	public void testEndpointReleaseYear() throws IOException
	{
		Assert.assertOk(this.response);
		final String body = this.response.getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final MovieSummaryResponse restResult = mapper.readValue(body, new TypeReference<MovieSummaryResponse>()
		{
		});
		this.assertSuccessfulMovieSummaryResponse(restResult);
		for (final MovieSummary movieSummary : restResult.getData())
		{
			if (movieSummary.getTitle().equals("Doctor Doolittle"))
			{
				return;
			}
		}
		org.junit.Assert.fail("/v1/movies/releaseYear/1998 did not list \"Doctor Doolittle\" in movies");
	}

	private void assertSuccessfulMovieSummaryResponse(final MovieSummaryResponse restResult)
	{
		org.junit.Assert.assertTrue(restResult.getSuccess());
		org.junit.Assert.assertNull(restResult.getErrorMessage());
		org.junit.Assert.assertTrue(restResult.getData().size() > 0);
	}
}
