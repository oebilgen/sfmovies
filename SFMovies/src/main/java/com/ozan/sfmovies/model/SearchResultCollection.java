package com.ozan.sfmovies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResultCollection implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<SearchResult> suggestions;

	public SearchResultCollection()
	{
		this.suggestions = new ArrayList<>();
	}

	/**
	 * @return the suggestions
	 */
	public List<SearchResult> getSuggestions()
	{
		return this.suggestions;
	}

	/**
	 * @param suggestions
	 *            the suggestions to set
	 */
	public void setSuggestions(final List<SearchResult> suggestions)
	{
		this.suggestions = suggestions;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public String toString()
	{
		return "SearchResultCollection [suggestions=" + this.suggestions + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.suggestions == null ? 0 : this.suggestions.hashCode());
		return result;
	}

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
		final SearchResultCollection other = (SearchResultCollection) obj;
		if (this.suggestions == null)
		{
			if (other.suggestions != null)
			{
				return false;
			}
		}
		else if (!this.suggestions.equals(other.suggestions))
		{
			return false;
		}
		return true;
	}

	public void add(final SearchResult searchResult)
	{
		this.suggestions.add(searchResult);
	}

	public void sort()
	{
		Collections.sort(this.suggestions);
	}

}
