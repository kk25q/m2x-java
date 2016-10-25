package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&amp;T M2X Collections API
 *
 * @see <a href="https://m2x.att.com/developer/documentation/v2/collections">M2X Collections API Documentation</a>
 */
public final class M2XCollection extends M2XClassWithMetadata
{
	public static final String URL_PATH = "/collections";

	public final String collectionId;

	M2XCollection(M2XClient client, String collectionId)
	{
		super(client);
		assert collectionId != null && collectionId.length() > 0;

		this.collectionId = collectionId;
	}

	String buildPath(String path)
	{
		return concat(M2XCollection.URL_PATH, "/", this.collectionId, path);
	}
	
	/**
	 * Add an existing device to an existing collection
	 *
	 * @param deviceId the id of the device
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/collections#Add-device-to-collection">https://m2x.att.com/developer/documentation/v2/collections#Add-device-to-collection</a>
	 */
	public M2XResponse addDevice(String deviceId) throws IOException
	{
		return makePut(concat("/devices/", deviceId), null);
	}
	
	/**
	 * Remove an existing device from an existing collection
	 *
	 * @param deviceId the id of the device
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/collections#Remove-device-from-collection">https://m2x.att.com/developer/documentation/v2/collections#Remove-device-from-collection</a>
	 */
	public M2XResponse removeDevice(String deviceId) throws IOException
	{
		return makeDelete(concat("/devices/", deviceId), null);
	}


}
