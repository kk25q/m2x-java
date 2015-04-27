package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&amp;T M2X Keys API
 *
 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys">M2X Keys API Documentation</a>
 */
public final class M2XKey extends M2XClass
{
	public static final String URL_PATH = "/keys";

	public final String keyId;

	M2XKey(M2XClient client, String key)
	{
		super(client);
		assert key != null && key.length() > 0;

		this.keyId = key;
	}

	String buildPath(String path)
	{
		return concat(M2XKey.URL_PATH, "/", this.keyId, path);
	}

	/**
	 * Get details of a specific key associated with a developer account.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys#View-Key-Details">https://m2x.att.com/developer/documentation/v2/keys#View-Key-Details</a>
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Update name, stream, permissions, expiration date, origin or device access
	 * of an existing key associated with the specified account.
	 *
	 * @param jsonContent parameters for the key to be updated as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys#Update-Key">https://m2x.att.com/developer/documentation/v2/keys#Update-Key</a>
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Regenerate the specified key.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys#Regenerate-Key">https://m2x.att.com/developer/documentation/v2/keys#Regenerate-Key</a>
	 */
	public M2XResponse regenerate() throws IOException
	{
		return makePost("/regenerate", null);
	}

	/**
	 * Delete an existing key.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys#Delete-Key">https://m2x.att.com/developer/documentation/v2/keys#Delete-Key</a>
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}
}
