package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&amp;T M2X Device API
 *
 * @see <a href="https://m2x.att.com/developer/documentation/v2/device">M2X Device API Documentation</a>
 */
public final class M2XDevice extends M2XClass
{
	public static final String URL_PATH = "/devices";

	public final String deviceId;

	M2XDevice(M2XClient client, String deviceId)
	{
		super(client);
		assert deviceId != null && deviceId.length() > 0;

		this.deviceId = deviceId;
	}

	String buildPath(String path)
	{
		return concat(M2XDevice.URL_PATH, "/", this.deviceId, path);
	}

	/**
	 * Update an existing Device's information.
	 *
	 * @param jsonContent parameters for the device to be updated as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Update-Device-Details">https://m2x.att.com/developer/documentation/v2/device#Update-Device-Details</a>
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Get details of an existing Device.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#View-Device-Details">https://m2x.att.com/developer/documentation/v2/device#View-Device-Details</a>
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Get location details of an existing Device.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Read-Device-Location"></a>
	 */
	public M2XResponse location() throws IOException
	{
		return makeGet("/location", null);
	}

	/**
	 * Update the current location of the specified device.
	 *
	 * @param jsonContent parameters for the device to be updated as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Update-Device-Location">https://m2x.att.com/developer/documentation/v2/device#Read-Device-Location</a>
	 */
	public M2XResponse updateLocation(String jsonContent) throws IOException
	{
		return makePut("/location", jsonContent);
	}

	/**
	 * Retrieve list of data streams associated with the device.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#List-Data-Streams">https://m2x.att.com/developer/documentation/v2/device#List-Data-Streams</a>
	 */
	public M2XResponse streams() throws IOException
	{
		return makeGet(M2XStream.URL_PATH, null);
	}

	/**
	 * Get a wrapper to access a data stream associated with the specified Device.
	 *
	 * @param streamName the name of the stream
	 * @return the stream for this device with the given stream name
	 */
	public M2XStream stream(String streamName)
	{
		return new M2XStream(streamName, this, null);
	}

	/**
	 * Post values to multiple streams at once.
	 *
	 * @param jsonContent parameters for the device to be updated as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Post-Device-Updates--Multiple-Values-to-Multiple-Streams-">https://m2x.att.com/developer/documentation/v2/device#Post-Device-Updates--Multiple-Values-to-Multiple-Streams-</a>
	 */
	public M2XResponse postUpdates(String jsonContent) throws IOException
	{
		return makePost("/updates", jsonContent);
	}

	/**
	 * Retrieve list of HTTP requests received lately by the specified device (up to 100 entries).
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#View-Request-Log">https://m2x.att.com/developer/documentation/v2/device#View-Request-Log</a>
	 */
	public M2XResponse log() throws IOException
	{
		return makeGet("/log", null);
	}

	/**
	 * Delete an existing device.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Delete-Device">https://m2x.att.com/developer/documentation/v2/device#Delete-Device</a>
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}
}
