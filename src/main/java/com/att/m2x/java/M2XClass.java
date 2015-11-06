package com.att.m2x.java;

import java.io.IOException;

public abstract class M2XClass
{
	public final M2XClient client;

	M2XClass(M2XClient client)
	{
		this.client = client;
	}

	abstract String buildPath(String path);
	String concat(String... strings)
	{
		StringBuilder sb = new StringBuilder();
		for (String str : strings)
			if (str != null)
				sb.append(str);
		return sb.toString();
	}

	public M2XResponse makeGet(String path, String query) throws IOException
	{
		return makeRequest("GET", path, query, null);
	}
	public M2XResponse makePost(String path, String jsonContent) throws IOException
	{
		return makeRequest("POST", path, null, jsonContent);
	}
	public M2XResponse makePut(String path, String jsonContent) throws IOException
	{
		return makeRequest("PUT", path, null, jsonContent);
	}
	public M2XResponse makeDelete(String path, String query) throws IOException
	{
		return makeRequest("DELETE", path, query, null);
	}

	public M2XResponse makeRequest(String method, String path, String query, String jsonContent)
		throws IOException
	{
		return this.client.makeRequest(method, buildPath(path), query, jsonContent);
	}

	/**
	 * Get details of an existing entity.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#View-Device-Details">https://m2x.att.com/developer/documentation/v2/device#View-Device-Details</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#View-Distribution-Details">https://m2x.att.com/developer/documentation/v2/distribution#View-Distribution-Details</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys#View-Key-Details">https://m2x.att.com/developer/documentation/v2/keys#View-Key-Details</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#View-Data-Stream">https://m2x.att.com/developer/documentation/v2/device#View-Data-Stream</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#View-Data-Stream">https://m2x.att.com/developer/documentation/v2/distribution#View-Data-Stream</a>
	 */
	public M2XResponse details() throws IOException { return makeGet(null, null); }

	/**
	 * Update an existing entity.
	 *
	 * @param jsonContent parameters for the device to be updated as JSON formatted string
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Update-Device-Details">https://m2x.att.com/developer/documentation/v2/device#Update-Device-Details</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Update-Distribution-Details">https://m2x.att.com/developer/documentation/v2/distribution#Update-Distribution-Details</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys#Update-Key">https://m2x.att.com/developer/documentation/v2/keys#Update-Key</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Create-Update-Data-Stream">https://m2x.att.com/developer/documentation/v2/device#Create-Update-Data-Stream</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Create-Update-Data-Stream">https://m2x.att.com/developer/documentation/v2/distribution#Create-Update-Data-Stream</a>
	 */
	public M2XResponse update(String jsonContent) throws IOException { return makePut(null, jsonContent); }

	/**
	 * Delete an existing entity.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Delete-Device">https://m2x.att.com/developer/documentation/v2/device#Delete-Device</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Delete-Distribution">https://m2x.att.com/developer/documentation/v2/distribution#Delete-Distribution</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/keys#Delete-Key">https://m2x.att.com/developer/documentation/v2/keys#Delete-Key</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/device#Delete-Data-Stream">https://m2x.att.com/developer/documentation/v2/device#Delete-Data-Stream</a>
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/distribution#Delete-Data-Stream">https://m2x.att.com/developer/documentation/v2/distribution#Delete-Data-Stream</a>
	 */
	public M2XResponse delete() throws IOException { return makeDelete(null, null); }
}
