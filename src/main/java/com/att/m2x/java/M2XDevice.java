package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&T M2X Device API
 * https://m2x.att.com/developer/documentation/v2/device
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
	 * https://m2x.att.com/developer/documentation/v2/device#Update-Device-Details
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Get details of an existing Device.
	 *
	 *  https://m2x.att.com/developer/documentation/v2/device#View-Device-Details
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Get location details of an existing Device.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Read-Device-Location
	 */
	public M2XResponse location() throws IOException
	{
		return makeGet("/location", null);
	}

	/**
	 * Update the current location of the specified device.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Read-Device-Location
	 */
	public M2XResponse updateLocation(String jsonContent) throws IOException
	{
		return makePut("/location", jsonContent);
	}

	/**
	 * Retrieve list of data streams associated with the device.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#List-Data-Streams
	 */
	public M2XResponse streams(String query) throws IOException
	{
		return makeGet(M2XStream.URL_PATH, query);
	}

	/**
	 * Get a wrapper to access a data stream associated with the specified Device.
	 */
	public M2XStream stream(String streamName)
	{
		return new M2XStream(streamName, this, null);
	}

	/**
	 * Post values to multiple streams at once.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Post-Device-Updates--Multiple-Values-to-Multiple-Streams-
	 */
	public M2XResponse postUpdates(String jsonContent) throws IOException
	{
		return makePost("/updates", jsonContent);
	}

	/**
	 * Retrieve list of triggers associated with the specified device.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#List-Triggers
	 */
	public M2XResponse triggers(String query) throws IOException
	{
		return makeGet(M2XTrigger.URL_PATH, query);
	}

	/**
	 * Create a new trigger associated with the specified device.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Create-Trigger
	 */
	public M2XResponse createTrigger(String jsonContent) throws IOException
	{
		return makePost(M2XTrigger.URL_PATH, jsonContent);
	}

	/**
	 * Get a wrapper to access a trigger associated with the specified Device.
	 */
	public M2XTrigger trigger(String triggerId)
	{
		return new M2XTrigger(triggerId, this, null);
	}

	/**
	 * Retrieve list of HTTP requests received lately by the specified device (up to 100 entries).
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#View-Request-Log
	 */
	public M2XResponse log(String query) throws IOException
	{
		return makeGet("/log", query);
	}

	/**
	 * Delete an existing device.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Delete-Device
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}
}
