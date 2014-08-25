package com.ozan.sfmovies.model;

public enum DataType
{
	ACTOR("ACTOR"),
	DIRECTOR("DIRECTOR"),
	DISTRIBUTOR("DISTRIBUTOR"),
	PRODUCTION_COMPANY("PRODUCTION_COMPANY"),
	RELEASE_YEAR("RELEASE_YEAR"),
	TITLE("TITLE"),
	WRITER("WRITER");

	private String dataType;

	private DataType(final String dataType)
	{
		this.dataType = dataType;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType()
	{
		return this.dataType;
	}

	/**
	 * @param dataType
	 *            the dataType to set
	 */
	public void setDataType(final String dataType)
	{
		this.dataType = dataType;
	}

}
