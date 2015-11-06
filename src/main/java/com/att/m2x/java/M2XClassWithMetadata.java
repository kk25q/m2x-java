package com.att.m2x.java;

import java.io.IOException;

public abstract class M2XClassWithMetadata extends M2XClass
{
	M2XClassWithMetadata(M2XClient client)
	{
		super(client);
	}

	/**
	 * Get custom metadata of an existing entity.
	 *
	 * @return the API response
	 * @throws java.io.IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Read-Device-Metadata">https://m2x.att.com/developer/documentation/v2/device#Read-Device-Metadata</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Read-Distribution-Metadata">https://m2x.att.com/developer/documentation/v2/distribution#Read-Distribution-Metadata</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/collections#Read-Collection-Metadata">https://m2x.att.com/developer/documentation/v2/collections#Read-Collection-Metadata</a>
	 */
	public M2XResponse metadata() throws IOException { return makeGet("/metadata", null); }

	/**
	 * Update the custom metadata of the specified entity.
	 *
	 * @return the API response
	 * @throws java.io.IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Update-Device-Metadata">https://m2x.att.com/developer/documentation/v2/device#Update-Device-Metadata</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Update-Distribution-Metadata">https://m2x.att.com/developer/documentation/v2/distribution#Update-Distribution-Metadata</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/collections#Update-Collection-Metadata">https://m2x.att.com/developer/documentation/v2/collections#Update-Collection-Metadata</a>
	 */
	public M2XResponse updateMetadata(String jsonContent) throws IOException { return makePut("/metadata", jsonContent); }

	/**
	 * Get the value of a single custom metadata field from an existing entity.
	 *
	 * @return the API response
	 * @throws java.io.IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Read-Device-Metadata-Field">https://m2x.att.com/developer/documentation/v2/device#Read-Device-Metadata-Field</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Read-Distribution-Metadata-Field">https://m2x.att.com/developer/documentation/v2/distribution#Read-Distribution-Metadata-Field</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/collections#Read-Collection-Metadata-Field">https://m2x.att.com/developer/documentation/v2/collections#Read-Collection-Metadata-Field</a>
	 */
	public M2XResponse metadataField(String field) throws IOException { return makeGet("/metadata/" + field, null); }

	/**
	 * Update the custom metadata of the specified entity.
	 *
	 * @return the API response
	 * @throws java.io.IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Update-Device-Metadata-Field">https://m2x.att.com/developer/documentation/v2/device#Update-Device-Metadata-Field</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Update-Distribution-Metadata-Field">https://m2x.att.com/developer/documentation/v2/distribution#Update-Distribution-Metadata-Field</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/collections#Update-Collection-Metadata-Field">https://m2x.att.com/developer/documentation/v2/collections#Update-Collection-Metadata-Field</a>
	 */
	public M2XResponse updateMetadataField(String field, String jsonContent) throws IOException { return makePut("/metadata/" + field, jsonContent); }
}
