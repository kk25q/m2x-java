package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&amp;T M2X Device API
 *
 * @see <a href="https://m2x.att.com/developer/documentation/v2/device">M2X Device API Documentation</a>
 */
public final class M2XDevice extends M2XClassWithMetadata
{
	public static final String URL_PATH = "/devices";

	public final String deviceId;
	public final String serial;

	M2XDevice(M2XClient client, String deviceId, String serial)
	{
		super(client);
		assert (deviceId != null && deviceId.length() > 0) || (serial != null && serial.length() > 0);

		this.deviceId = deviceId;
		this.serial = serial;
	}

	String buildPath(String path)
	{
		return (deviceId != null && deviceId.length() > 0)
			? concat(M2XDevice.URL_PATH, "/", this.deviceId, path)
			: concat(M2XDevice.URL_PATH, "/serial/", this.serial, path);
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
	 * @param query query parameters (optional)
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#List-Data-Streams">https://m2x.att.com/developer/documentation/v2/device#List-Data-Streams</a>
	 */
	public M2XResponse streams(String query) throws IOException
	{
		return makeGet(M2XStream.URL_PATH, query);
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
	 * List values from all data streams associated with a specific device, sorted in reverse chronological order
	 * (most recent values first).
	 *
	 * @param query query parameters (optional)
	 * @param format the desired response format (optional)
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#List-Values-from-all-Data-Streams-of-a-Device">https://m2x.att.com/developer/documentation/v2/device#List-Values-from-all-Data-Streams-of-a-Device</a>
	 */
	public M2XResponse values(String query, String format) throws IOException
	{
		String path = "/values";
		if (format != null && format.length() > 0)
			path += "." + format;
		return makeGet(path, query);
	}

	/**
	 * Search and list values from all data streams associated with a specific device, sorted in reverse chronological order.
	 *
	 * @param jsonContent search parameters as JSON formatted string
	 * @param format the desired response format (optional)
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Search-Values-from-all-Data-Streams-of-a-Device">https://m2x.att.com/developer/documentation/v2/device#Search-Values-from-all-Data-Streams-of-a-Device</a>
	 */
	public M2XResponse searchValues(String jsonContent, String format) throws IOException
	{
		String path = "/values/search";
		if (format != null && format.length() > 0)
			path += "." + format;
		return makePost(path, jsonContent);
	}

	/**
	 * Export all values from all or selected data streams associated with a specific device, sorted in reverse chronological order
	 * (most recent values first).
	 *
	 * @param query query parameters (optional)
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Export-Values-from-all-Data-Streams-of-a-Device">https://m2x.att.com/developer/documentation/v2/device#Export-Values-from-all-Data-Streams-of-a-Device</a>
	 */
	public M2XResponse exportValues(String query) throws IOException
	{
		return makeGet("/values/export.csv", query);
	}

	/**
	 * Posts single values to multiple streams at once.
	 *
	 * @param jsonContent parameters for the request as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Post-Device-Update--Single-Values-to-Multiple-Streams-">https://m2x.att.com/developer/documentation/v2/device#Post-Device-Update--Single-Values-to-Multiple-Streams-</a>
	 */
	public M2XResponse postUpdate(String jsonContent) throws IOException
	{
		return makePost("/update", jsonContent);
	}

	/**
	 * Post values to multiple streams at once.
	 *
	 * @param jsonContent parameters for the request as JSON formatted string
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
	 * @param query query parameters (optional)
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#View-Request-Log">https://m2x.att.com/developer/documentation/v2/device#View-Request-Log</a>
	 */
	public M2XResponse log(String query) throws IOException
	{
		return makeGet("/log", query);
	}
}
