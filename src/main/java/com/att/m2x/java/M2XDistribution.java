package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&T M2X Distribution API
 * https://m2x.att.com/developer/documentation/v2/distribution
 */
public final class M2XDistribution extends M2XClass
{
	public static final String URL_PATH = "/distributions";

	public final String distributionId;

	M2XDistribution(M2XClient client, String distributionId)
	{
		super(client);
		assert distributionId != null & distributionId.length() > 0;

		this.distributionId = distributionId;
	}

	String buildPath(String path)
	{
		return concat(M2XDistribution.URL_PATH, "/", this.distributionId, path);
	}

	/**
	 * Retrieve information about an existing device distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#View-Distribution-Details
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Update an existing device distribution's information.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#Update-Distribution-Details
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Retrieve list of devices added to the specified distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#List-Devices-from-an-existing-Distribution
	 */
	public M2XResponse devices(String query) throws IOException
	{
		return makeGet(M2XDevice.URL_PATH, query);
	}

	/**
	 * Add a new device to an existing distribution
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#Add-Device-to-an-existing-Distribution
	 */
	public M2XResponse addDevice(String jsonContent) throws IOException
	{
		return makePost(M2XDevice.URL_PATH, jsonContent);
	}

	/**
	 * Delete an existing device distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#Delete-Distribution
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}

	/**
	 * Retrieve list of data streams associated with the specified distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#List-Data-Streams
	 */
	public M2XResponse streams(String query) throws IOException
	{
		return makeGet(M2XStream.URL_PATH, query);
	}

	/**
	 * Get a wrapper to access a data stream associated with the specified distribution
	 */
	public M2XStream stream(String streamName)
	{
		return new M2XStream(streamName, null, this);
	}

	/**
	 * Retrieve list of triggers associated with the specified distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#List-Triggers
	 */
	public M2XResponse triggers(String query) throws IOException
	{
		return makeGet(M2XTrigger.URL_PATH, query);
	}

	/**
	 * Create a new trigger associated with the specified distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#Create-Trigger
	 */
	public M2XResponse createTrigger(String jsonContent) throws IOException
	{
		return makePost(M2XTrigger.URL_PATH, jsonContent);
	}

	/**
	 * Get a wrapper to access a trigger associated with the specified distribution.
	 */
	public M2XTrigger trigger(String triggerId)
	{
		return new M2XTrigger(triggerId, null, this);
	}
}
