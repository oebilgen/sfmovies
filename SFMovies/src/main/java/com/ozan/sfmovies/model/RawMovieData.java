package com.ozan.sfmovies.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RawMovieData implements Serializable
{
	private static final long serialVersionUID = 1L;
	@JsonProperty("actor_1")
	private String actor1;
	@JsonProperty("actor_2")
	private String actor2;
	@JsonProperty("actor_3")
	private String actor3;
	private String director;
	private String distributor;
	@JsonProperty("fun_facts")
	private String funFacts;
	private String locations;
	@JsonProperty("production_company")
	private String productionCompany;
	@JsonProperty("release_year")
	private Integer releaseYear;
	private String title;
	private String writer;

	/**
	 * @return the actor1
	 */
	public String getActor1()
	{
		return this.actor1;
	}

	/**
	 * @param actor1
	 *            the actor1 to set
	 */
	public void setActor1(final String actor1)
	{
		this.actor1 = actor1;
	}

	/**
	 * @return the actor2
	 */
	public String getActor2()
	{
		return this.actor2;
	}

	/**
	 * @param actor2
	 *            the actor2 to set
	 */
	public void setActor2(final String actor2)
	{
		this.actor2 = actor2;
	}

	/**
	 * @return the actor3
	 */
	public String getActor3()
	{
		return this.actor3;
	}

	/**
	 * @param actor3
	 *            the actor3 to set
	 */
	public void setActor3(final String actor3)
	{
		this.actor3 = actor3;
	}

	/**
	 * @return the director
	 */
	public String getDirector()
	{
		return this.director;
	}

	/**
	 * @param director
	 *            the director to set
	 */
	public void setDirector(final String director)
	{
		this.director = director;
	}

	/**
	 * @return the distributor
	 */
	public String getDistributor()
	{
		return this.distributor;
	}

	/**
	 * @param distributor
	 *            the distributor to set
	 */
	public void setDistributor(final String distributor)
	{
		this.distributor = distributor;
	}

	/**
	 * @return the funFacts
	 */
	public String getFunFacts()
	{
		return this.funFacts;
	}

	/**
	 * @param funFacts
	 *            the funFacts to set
	 */
	public void setFunFacts(final String funFacts)
	{
		this.funFacts = funFacts;
	}

	/**
	 * @return the locations
	 */
	public String getLocations()
	{
		return this.locations;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(final String locations)
	{
		this.locations = locations;
	}

	/**
	 * @return the productionCompany
	 */
	public String getProductionCompany()
	{
		return this.productionCompany;
	}

	/**
	 * @param productionCompany
	 *            the productionCompany to set
	 */
	public void setProductionCompany(final String productionCompany)
	{
		this.productionCompany = productionCompany;
	}

	/**
	 * @return the releaseYear
	 */
	public Integer getReleaseYear()
	{
		return this.releaseYear;
	}

	/**
	 * @param releaseYear
	 *            the releaseYear to set
	 */
	public void setReleaseYear(final Integer releaseYear)
	{
		this.releaseYear = releaseYear;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * @return the writer
	 */
	public String getWriter()
	{
		return this.writer;
	}

	/**
	 * @param writer
	 *            the writer to set
	 */
	public void setWriter(final String writer)
	{
		this.writer = writer;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.actor1 == null ? 0 : this.actor1.hashCode());
		result = prime * result + (this.actor2 == null ? 0 : this.actor2.hashCode());
		result = prime * result + (this.actor3 == null ? 0 : this.actor3.hashCode());
		result = prime * result + (this.director == null ? 0 : this.director.hashCode());
		result = prime * result + (this.distributor == null ? 0 : this.distributor.hashCode());
		result = prime * result + (this.funFacts == null ? 0 : this.funFacts.hashCode());
		result = prime * result + (this.locations == null ? 0 : this.locations.hashCode());
		result = prime * result + (this.productionCompany == null ? 0 : this.productionCompany.hashCode());
		result = prime * result + (this.releaseYear == null ? 0 : this.releaseYear.hashCode());
		result = prime * result + (this.title == null ? 0 : this.title.hashCode());
		result = prime * result + (this.writer == null ? 0 : this.writer.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final RawMovieData other = (RawMovieData) obj;
		if (this.actor1 == null)
		{
			if (other.actor1 != null)
			{
				return false;
			}
		}
		else if (!this.actor1.equals(other.actor1))
		{
			return false;
		}
		if (this.actor2 == null)
		{
			if (other.actor2 != null)
			{
				return false;
			}
		}
		else if (!this.actor2.equals(other.actor2))
		{
			return false;
		}
		if (this.actor3 == null)
		{
			if (other.actor3 != null)
			{
				return false;
			}
		}
		else if (!this.actor3.equals(other.actor3))
		{
			return false;
		}
		if (this.director == null)
		{
			if (other.director != null)
			{
				return false;
			}
		}
		else if (!this.director.equals(other.director))
		{
			return false;
		}
		if (this.distributor == null)
		{
			if (other.distributor != null)
			{
				return false;
			}
		}
		else if (!this.distributor.equals(other.distributor))
		{
			return false;
		}
		if (this.funFacts == null)
		{
			if (other.funFacts != null)
			{
				return false;
			}
		}
		else if (!this.funFacts.equals(other.funFacts))
		{
			return false;
		}
		if (this.locations == null)
		{
			if (other.locations != null)
			{
				return false;
			}
		}
		else if (!this.locations.equals(other.locations))
		{
			return false;
		}
		if (this.productionCompany == null)
		{
			if (other.productionCompany != null)
			{
				return false;
			}
		}
		else if (!this.productionCompany.equals(other.productionCompany))
		{
			return false;
		}
		if (this.releaseYear == null)
		{
			if (other.releaseYear != null)
			{
				return false;
			}
		}
		else if (!this.releaseYear.equals(other.releaseYear))
		{
			return false;
		}
		if (this.title == null)
		{
			if (other.title != null)
			{
				return false;
			}
		}
		else if (!this.title.equals(other.title))
		{
			return false;
		}
		if (this.writer == null)
		{
			if (other.writer != null)
			{
				return false;
			}
		}
		else if (!this.writer.equals(other.writer))
		{
			return false;
		}
		return true;
	}
}
