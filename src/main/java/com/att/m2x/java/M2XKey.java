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
}
