package com.att.m2x.client;

import java.io.IOException;

/**
 * Wrapper for AT&T M2X Data Streams API
 * https://m2x.att.com/developer/documentation/v2/device
 * https://m2x.att.com/developer/documentation/v2/distribution
 */
public final class M2XStream extends M2XClass
{
	public static final String URL_PATH = "/streams";

	public final String streamName;
	public final M2XDevice device;
	public final M2XDistribution distribution;

	M2XStream(String streamName, M2XDevice device, M2XDistribution distribution)
	{
		super(device == null ? distribution.client : device.client);
		assert streamName != null & streamName.length() > 0;

		this.streamName = streamName;
		this.device = device;
		this.distribution = distribution;
	}

	String buildPath(String path)
	{
		path = concat(M2XStream.URL_PATH, "/", this.streamName, path);
		return this.device == null
			? this.distribution.buildPath(path)
			: this.device.buildPath(path);
	}

	/**
	 * Update a data stream associated with the Device or specified distribution
	 * (if a stream with this name does not exist it gets created).
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Create-Update-Data-Stream
	 * https://m2x.att.com/developer/documentation/v2/distribution#Create-Update-Data-Stream
	 */
	public M2XResponse createOrUpdate(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Update the current value of the stream.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Update-Data-Stream-Value
	 */
	public M2XResponse updateValue(String jsonContent) throws IOException
	{
		return makePut("/value", jsonContent);
	}

	/**
	 * Get details of a specific data Stream associated with an existing device or distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#View-Data-Stream
	 * https://m2x.att.com/developer/documentation/v2/distribution#View-Data-Stream
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * List values from the stream, sorted in reverse chronological order
	 * (most recent values first).
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#List-Data-Stream-Values
	 */
	public M2XResponse values(String query) throws IOException
	{
		return makeGet("/values", query);
	}

	/**
	 * Sample values from the stream, sorted in reverse chronological order
	 * (most recent values first).
	 *
	 * This method only works for numeric streams
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Data-Stream-Sampling
	 */
	public M2XResponse sampling(String query) throws IOException
	{
		return makeGet("/sampling", query);
	}

	/**
	 * Return count, min, max, average and standard deviation stats for the
	 * values of the stream.
	 *
	 * This method only works for numeric streams
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Data-Stream-Stats
	 */
	public M2XResponse stats(String query) throws IOException
	{
		return makeGet("/stats", query);
	}

	/**
	 * Post multiple values to the stream
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Post-Data-Stream-Values
	 */
	public M2XResponse postValues(String jsonContent) throws IOException
	{
		return makePost("/values", jsonContent);
	}

	/**
	 * Delete values in a stream by a date range
	 *
	 * https://m2x.com/developer/documentation/v2/device#Delete-Data-Stream-Values
	 */
	public M2XResponse deleteValues(String query) throws IOException
	{
		return makeDelete("/values", query);
	}

	/**
	 * Delete an existing data stream associated with a specific device or distribution.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Delete-Data-Stream
	 * https://m2x.att.com/developer/documentation/v2/distribution#Delete-Data-Stream
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}
}
