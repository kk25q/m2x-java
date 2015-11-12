package com.att.m2x.java;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wrapper for AT&amp;T M2X API response
 *
 */
public final class M2XResponse
{
	/**
	 * The HTTP method of API call
	 */
	public final String requestMethod;
	/**
	 * The URL of API call
	 */
	public final URL requestUrl;
	/**
	 * The content of API call
	 */
	public final String requestContent;

	/**
	 * The status code of the response.
	 */
	public final int status;
	/**
	 * The headers included on the response.
	 */
	public final Map<String, List<String>> headers;
	/**
	 * The raw response body.
	 */
	public final String raw;

	private JSONObject _json = null;

	/**
	 * Returns the parsed response body.
	 *
	 * @return the parsed response body as JSON objecet
	 */
	public JSONObject json()
	{
		if (this._json != null)
			return this._json;
		if (this.raw == null || this.raw.isEmpty())
			return null;
		else
		{
			this._json = new JSONObject(this.raw);
			return this._json;
		}
	}

	/**
	 * Returns whether status is a success (status code 2xx)
	 *
	 * @return true if response is success (status code 2xx)
	 */
	public boolean success() { return this.status >= 200 && this.status < 300; }

	/**
	 * Returns whether status is one of 4xx
	 *
	 * @return true if response is 4xx
	 */
	public boolean clientError() { return this.status >= 400 && this.status < 500; }

	/**
	 * Returns whether status is one of 5xx
	 *
	 * @return true if response is 5xx
	 */
	public boolean serverError() { return this.status >= 500 && this.status < 600; }

	/**
	 * Returns whether clientError or serverError is true
	 *
	 * @return true if response is client or server error
	 */
	public boolean error() { return this.clientError() || this.serverError(); }

	M2XResponse(HttpURLConnection conn, String jsonContent)
		throws IOException
	{
		this.requestMethod = conn.getRequestMethod();
		this.requestUrl = conn.getURL();
		this.requestContent = jsonContent;

		if (this.requestContent != null)
		{
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			try
			{
				writer.write(jsonContent);
			}
			finally
			{
				writer.close();
			}
		}

		this.status = conn.getResponseCode();
		this.headers = conn.getHeaderFields();

		InputStream in = conn.getDoInput() ?
			(this.success() ? conn.getInputStream() : conn.getErrorStream()) : null;
		if (in == null)
			this.raw = null;
		else
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder result = new StringBuilder();
			try
			{
				String line;
				while ((line = reader.readLine()) != null)
				{
					result.append(line);
				}
			}
			finally
			{
				reader.close();
			}
			this.raw = result.toString();
		}
	}
}
