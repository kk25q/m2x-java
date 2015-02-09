package com.att.m2x.client;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Wrapper for AT&T M2X API
 * https://m2x.att.com/developer/documentation/v2/overview
 */
public final class M2XClient
{
	public static final String API_ENDPOINT = "http://api-m2x.att.com/v2";
	public static final String API_ENDPOINT_SECURE = "https://api-m2x.att.com/v2";

	static final String USER_AGENT;

	public final String apiKey;
	public final String endpoint;

	public int connectionTimeout = 30000;
	public int readTimeout = 30000;

	/**
	 * The last API call response
	 */
	public volatile M2XResponse lastResponse = null;

	static
	{
		String version = "2.0.1";
		String langVersion = System.getProperty("java.version");
		String osVersion = System.getProperty("os.arch") + "-" + System.getProperty("os.name") + System.getProperty("os.version");
		USER_AGENT = String.format("M2X-Java/%s java/%s (%s)", version, langVersion, osVersion);
	}

	public M2XClient(String apiKey)
	{
		this(apiKey, API_ENDPOINT);
	}
	public M2XClient(String apiKey, String endpoint)
	{
		assert endpoint != null && endpoint.length() > 0;

		this.apiKey = apiKey;
		this.endpoint = endpoint;
	}

	// Device API

	/**
	 * Search the catalog of public Devices.
	 *
	 * This allows unauthenticated users to search Devices from other users
	 * that have been marked as public, allowing them to read public Device
	 * metadata, locations, streams list, and view each Devices' stream metadata
	 * and its values.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#List-Search-Public-Devices-Catalog
	 */
	public M2XResponse deviceCatalog(String query) throws IOException
	{
		return makeRequest("GET", M2XDevice.URL_PATH + "/catalog", query, null);
	}

	/**
	 * Retrieve the list of devices accessible by the authenticated API key that
	 * meet the search criteria.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#List-Search-Devices
	 */
	public M2XResponse devices(String query) throws IOException
	{
		return makeRequest("GET", M2XDevice.URL_PATH, query, null);
	}

	/**
	 * List Device Groups
	 * Retrieve the list of device groups for the authenticated user.
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#List-Device-Groups
	 */
	public M2XResponse deviceGroups(String query) throws IOException
	{
		return makeRequest("GET", M2XDevice.URL_PATH + "/groups", query, null);
	}

	/**
	 * Create a new device
	 *
	 * https://m2x.att.com/developer/documentation/v2/device#Create-Device
	 */
	public M2XResponse createDevice(String jsonContent) throws IOException
	{
		return makeRequest("POST", M2XDevice.URL_PATH, null, jsonContent);
	}

	/**
	 * Get a wrapper to access an existing Device.
	 */
	public M2XDevice device(String deviceId)
	{
		return new M2XDevice(this, deviceId);
	}

	// Distribution API

	/**
	 * Retrieve list of device distributions accessible by the authenticated API key.
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#List-Distributions
	 */
	public M2XResponse distributions(String query) throws IOException
	{
		return makeRequest("GET", M2XDistribution.URL_PATH, query, null);
	}

	/**
	 * Create a new device distribution
	 *
	 * https://m2x.att.com/developer/documentation/v2/distribution#Create-Distribution
	 */
	public M2XResponse createDistribution(String jsonContent) throws IOException
	{
		return makeRequest("POST", M2XDistribution.URL_PATH, null, jsonContent);
	}

	/**
	 * Get a wrapper to access an existing device distribution.
	 */
	public M2XDistribution distribution(String distributionId)
	{
		return new M2XDistribution(this, distributionId);
	}

	// Keys API

	/**
	 * Retrieve list of keys associated with the specified account.
	 *
	 * https://m2x.att.com/developer/documentation/v2/keys#List-Keys
	 */
	public M2XResponse keys(String query) throws IOException
	{
		return makeRequest("GET", M2XKey.URL_PATH, query, null);
	}

	/**
	 * Create a new key associated with the specified account.
	 *
	 * https://m2x.att.com/developer/documentation/v2/keys#Create-Key
	 */
	public M2XResponse createKey(String jsonContent) throws IOException
	{
		return makeRequest("POST", M2XKey.URL_PATH, null, jsonContent);
	}

	/**
	 * Get a wrapper to access an existing key associated with the specified account.
	 */
	public M2XKey key(String key)
	{
		return new M2XKey(this, key);
	}

	// Charts API

	/**
	 * Retrieve the list of charts that belong to the authenticated user.
	 *
	 * https://m2x.att.com/developer/documentation/v2/charts#List-Charts
	 */
	public M2XResponse charts(String query) throws IOException
	{
		return makeRequest("GET", M2XChart.URL_PATH, query, null);
	}

	/**
	 * Create a new chart associated with the authenticated account.
	 *
	 * https://m2x.att.com/developer/documentation/v2/charts#Create-Chart
	 */
	public M2XResponse createChart(String jsonContent) throws IOException
	{
		return makeRequest("POST", M2XChart.URL_PATH, null, jsonContent);
	}

	/**
	 * Get a wrapper to access an existing chart.
	 */
	public M2XChart chart(String chartId)
	{
		return new M2XChart(this, chartId);
	}

	// Common

	/**
	 * Makes a call to AT&T M2X API
	 * @param method the HTTP method (GET/POST/PUT/DELETE)
	 * @param path the API URL path (optional)
	 * @param query the API URL query parameters (optional)
	 * @param jsonContent the POST/PUT/DELETE content (optional)
	 * @return the API response
	 * @throws IOException
	 */
	public M2XResponse makeRequest(String method, String path, String query, String jsonContent)
		throws IOException
	{
		URL uri = new URL(buildUrl(path, query));
		HttpURLConnection conn = (HttpURLConnection)uri.openConnection();
		conn.setRequestMethod(method);
		if (this.apiKey != null)
			conn.setRequestProperty("X-M2X-KEY", this.apiKey);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept", "application/json");
		conn.setConnectTimeout(this.connectionTimeout);
		conn.setReadTimeout(this.readTimeout);

		M2XResponse response = new M2XResponse(conn, jsonContent);
		this.lastResponse = response;
		return response;
	}

	/**
	 * Builds url to AT&T M2X API
	 * @param path the API URL path (optional)
	 * @param query the API URL query parameters (optional)
	 * @return full url
	 */
	public String buildUrl(String path, String query)
	{
		String result = this.endpoint;
		if (path != null)
			result += path;
		if (query != null)
			result += "?" + query;
		return result;
	}

	/**
	 * Serializes all fields of a given object to query string
	 */
	public static String objectToQuery(Object obj)
	{
		StringBuilder sb = new StringBuilder();
		Class c = obj.getClass();
		for (Field field: c.getFields())
		{
			Object value;
			try
			{
				value = field.get(obj);
			}
			catch (IllegalAccessException ex)
			{
				continue;
			}
			if (sb.length() > 0)
				sb.append('&');
			String encoded;
			try
			{
				encoded = value == null ? "" : java.net.URLEncoder.encode(value.toString(), "UTF-8");
			}
			catch (UnsupportedEncodingException ex)
			{
				continue;
			}
			sb.append(field.getName()).append('=').append(encoded);
		}
		return sb.toString();
	}
	/**
	 * Serializes a given map to query string
	 */
	public static String mapToQuery(Map<String, String> map)
	{
		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<String, String>> set =  map.entrySet();
		for (Map.Entry<String, String> entry: set)
		{
			String value = entry.getValue();
			if (sb.length() > 0)
				sb.append('&');
			try
			{
				value = value == null ? "" : java.net.URLEncoder.encode(value, "UTF-8");
			}
			catch (UnsupportedEncodingException ex)
			{
				continue;
			}
			sb.append(entry.getKey()).append('=').append(value);
		}
		return sb.toString();
	}

	/**
	 * Serializes a given object into json string using JSONObject
	 * http://www.json.org/javadoc/org/json/JSONObject.html#JSONObject(java.lang.Object)
	 */
	public static String jsonSerialize(Object obj)
	{
		JSONObject json = new JSONObject(obj);
		return json.toString();
	}
	/**
	 * Serializes a given map into json string
	 */
	public static String jsonSerialize(Map<String, Object> map)
	{
		JSONStringer writer = new JSONStringer();
		writer.object();
		Set<Map.Entry<String, Object>> set =  map.entrySet();
		for (Map.Entry<String, Object> entry: set)
		{
			writer.key(entry.getKey()).value(entry.getValue());
		}
		writer.endObject();
		return writer.toString();
	}

	/**
	 * Formats a Date value to an ISO8601 timestamp
	 */
	public static String dateTimeToString(Date dateTime)
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(dateTime);
	}
}


