package com.ozan.sfmovies.model.geodata;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.ozan.sfmovies.model.Location;

/**
 * @author Abhishek Somani
 */
public class Geometry
{

	private Location location;

	@JsonProperty("location_type")
	private String locationType;

	@JsonIgnore
	private Object bounds;

	@JsonIgnore
	private Object viewport;

	public Location getLocation()
	{
		return this.location;
	}

	public void setLocation(final Location location)
	{
		this.location = location;
	}

	public String getLocationType()
	{
		return this.locationType;
	}

	public void setLocationType(final String locationType)
	{
		this.locationType = locationType;
	}

	public Object getBounds()
	{
		return this.bounds;
	}

	public void setBounds(final Object bounds)
	{
		this.bounds = bounds;
	}

	public Object getViewport()
	{
		return this.viewport;
	}

	public void setViewport(final Object viewport)
	{
		this.viewport = viewport;
	}

}
