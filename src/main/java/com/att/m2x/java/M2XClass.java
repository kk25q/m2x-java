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

	public abstract M2XResponse details() throws IOException;
	public abstract M2XResponse delete() throws IOException;
}
