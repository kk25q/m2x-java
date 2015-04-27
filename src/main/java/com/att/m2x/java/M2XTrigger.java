package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&amp;T M2X Triggers API
 *
 * @see <a href="https://m2x.att.com/developer/documentation/v2/device">M2X Device API Documentation</a>
 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution">M2X Distribution API Documentation</a>
 */
public final class M2XTrigger extends M2XClass
{
	public static final String URL_PATH = "/triggers";

	public final String triggerId;
	public final M2XDevice device;
	public final M2XDistribution distribution;

	M2XTrigger(String triggerId, M2XDevice device, M2XDistribution distribution)
	{
		super(device == null ? distribution.client : device.client);
		assert triggerId != null & triggerId.length() > 0;

		this.triggerId = triggerId;
		this.device = device;
		this.distribution = distribution;
	}

	String buildPath(String path)
	{
		path = concat(M2XTrigger.URL_PATH, "/", this.triggerId, path);
		return this.device == null
			? this.distribution.buildPath(path)
			: this.device.buildPath(path);
	}

	/**
	 * Get details of a specific trigger associated with an existing device or distribution.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#View-Trigger">https://m2x.att.com/developer/documentation/v2/device#View-Trigger</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#View-Trigger">https://m2x.att.com/developer/documentation/v2/distribution#View-Trigger</a>
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Update an existing trigger associated with the specified device or distribution.
	 *
	 * @param jsonContent parameters for the trigger to be updated as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Update-Trigger">https://m2x.att.com/developer/documentation/v2/device#Update-Trigger</a>
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Test the specified trigger by firing it with a fake value.
	 *
	 * This method can be used by developers of java applications
	 * to test the way their apps receive and handle M2X notifications.
	 *
	 * @param jsonContent parameters for the trigger to be tested as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Test-Trigger">https://m2x.att.com/developer/documentation/v2/device#Test-Trigger</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Test-Trigger">https://m2x.att.com/developer/documentation/v2/distribution#Test-Trigger</a>
	 */
	public M2XResponse test(String jsonContent) throws IOException
	{
		return makePost("/test", jsonContent);
	}

	/**
	 * Delete an existing trigger associated with a specific device or distribution.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Delete-Trigger">https://m2x.att.com/developer/documentation/v2/device#Delete-Trigger</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Delete-Trigger">https://m2x.att.com/developer/documentation/v2/distribution#Delete-Trigger</a>
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}
}
