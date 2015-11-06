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
}
