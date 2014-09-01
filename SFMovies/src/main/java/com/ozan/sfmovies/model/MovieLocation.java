/**
 * (c) San Francisco Movies - Author: Ozan Eren Bilgen (oebilgen@gmail.com)
 */
package com.ozan.sfmovies.model;

 import java.io.Serializable;

 public class MovieLocation implements Serializable
 {
	 private static final long serialVersionUID = 1L;
	 private Double latitude;
	 private Double longitude;
	 private String formattedAddress;
	 private String funFacts;

	 public MovieLocation()
	 {
	 }

	 public MovieLocation(final Double latitude, final Double longitude, final String formattedAddress, final String funFacts)
	 {
		 this.latitude = latitude;
		 this.longitude = longitude;
		 this.formattedAddress = formattedAddress;
		 this.funFacts = funFacts;
	 }

	 /**
	  * @return the latitude
	  */
	 public Double getLatitude()
	 {
		 return this.latitude;
	 }

	 /**
	  * @param latitude
	  *            the latitude to set
	  */
	 public void setLatitude(final Double latitude)
	 {
		 this.latitude = latitude;
	 }

	 /**
	  * @return the longitude
	  */
	 public Double getLongitude()
	 {
		 return this.longitude;
	 }

	 /**
	  * @param longitude
	  *            the longitude to set
	  */
	 public void setLongitude(final Double longitude)
	 {
		 this.longitude = longitude;
	 }

	 /**
	  * @return the formattedAddress
	  */
	 public String getFormattedAddress()
	 {
		 return this.formattedAddress;
	 }

	 /**
	  * @param formattedAddress
	  *            the formattedAddress to set
	  */
	 public void setFormattedAddress(final String formattedAddress)
	 {
		 this.formattedAddress = formattedAddress;
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
	  * @return the serialversionuid
	  */
	 public static long getSerialversionuid()
	 {
		 return serialVersionUID;
	 }

	 /*
	  * (non-Javadoc)
	  * @see java.lang.Object#toString()
	  */
	 @Override
	 public String toString()
	 {
		 return "MovieLocation [latitude=" + this.latitude + ", longitude=" + this.longitude + ", formattedAddress=" + this.formattedAddress + ", funFacts="
				 + this.funFacts + "]";
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
		 result = (prime * result) + ((this.formattedAddress == null) ? 0 : this.formattedAddress.hashCode());
		 result = (prime * result) + ((this.funFacts == null) ? 0 : this.funFacts.hashCode());
		 result = (prime * result) + ((this.latitude == null) ? 0 : this.latitude.hashCode());
		 result = (prime * result) + ((this.longitude == null) ? 0 : this.longitude.hashCode());
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
		 final MovieLocation other = (MovieLocation) obj;
		 if (this.formattedAddress == null)
		 {
			 if (other.formattedAddress != null)
			 {
				 return false;
			 }
		 }
		 else if (!this.formattedAddress.equals(other.formattedAddress))
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
		 if (this.latitude == null)
		 {
			 if (other.latitude != null)
			 {
				 return false;
			 }
		 }
		 else if (!this.latitude.equals(other.latitude))
		 {
			 return false;
		 }
		 if (this.longitude == null)
		 {
			 if (other.longitude != null)
			 {
				 return false;
			 }
		 }
		 else if (!this.longitude.equals(other.longitude))
		 {
			 return false;
		 }
		 return true;
	 }
 }
