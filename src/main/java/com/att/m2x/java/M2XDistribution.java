package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&amp;T M2X Distribution API
 *
 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution">M2X Distribution API Documentation</a>
 */
public final class M2XDistribution extends M2XClassWithMetadata
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
	 * Retrieve list of devices added to the specified distribution.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#List-Devices-from-an-existing-Distribution">https://m2x.att.com/developer/documentation/v2/distribution#List-Devices-from-an-existing-Distribution</a>
	 */
	public M2XResponse devices() throws IOException
	{
		return makeGet(M2XDevice.URL_PATH, null);
	}

	/**
	 * Add a new device to an existing distribution
	 *
	 * @param jsonContent parameters for the distribution to be updated as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Add-Device-to-an-existing-Distribution">https://m2x.att.com/developer/documentation/v2/distribution#Add-Device-to-an-existing-Distribution</a>
	 */
	public M2XResponse addDevice(String jsonContent) throws IOException
	{
		return makePost(M2XDevice.URL_PATH, jsonContent);
	}

	/**
	 * Retrieve list of data streams associated with the specified distribution.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#List-Data-Streams">https://m2x.att.com/developer/documentation/v2/distribution#List-Data-Streams</a>
	 */
	public M2XResponse streams() throws IOException
	{
		return makeGet(M2XStream.URL_PATH, null);
	}

	/**
	 * Get a wrapper to access a data stream associated with the specified distribution
	 *
	 * @param streamName the stream name
	 * @return the stream for this distribution with the given stream name
	 */
	public M2XStream stream(String streamName)
	{
		return new M2XStream(streamName, null, this);
	}

}
