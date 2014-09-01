/**
 * (c) San Francisco Movies - Author: Ozan Eren Bilgen (oebilgen@gmail.com)
 */
package com.ozan.sfmovies.model.geodata;

 import org.codehaus.jackson.annotate.JsonIgnore;
 import org.codehaus.jackson.annotate.JsonProperty;

 /**
  * @author Abhishek Somani
  */
 public class Geometry
 {

	 private Coordinates location;

	 @JsonProperty("location_type")
	 private String locationType;

	 @JsonIgnore
	 private Object bounds;

	 @JsonIgnore
	 private Object viewport;

	 public Coordinates getLocation()
	 {
		 return this.location;
	 }

	 public void setLocation(final Coordinates location)
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
