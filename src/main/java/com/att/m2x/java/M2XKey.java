package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&T M2X Keys API
 * https://m2x.att.com/developer/documentation/v2/keys
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
	 * https://m2x.att.com/developer/documentation/v2/keys#View-Key-Details
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Update name, stream, permissions, expiration date, origin or device access
	 * of an existing key associated with the specified account.
	 *
	 * https://m2x.att.com/developer/documentation/v2/keys#Update-Key
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Regenerate the specified key.
	 *
	 * https://m2x.att.com/developer/documentation/v2/keys#Regenerate-Key
	 */
	public M2XResponse regenerate() throws IOException
	{
		return makePost("/regenerate", null);
	}

	/**
	 * Delete an existing key.
	 *
	 * https://m2x.att.com/developer/documentation/v2/keys#Delete-Key
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}
}
