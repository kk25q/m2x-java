package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&T M2X Charts API
 * https://m2x.att.com/developer/documentation/v2/charts
 */
public final class M2XChart extends M2XClass
{
	public static final String URL_PATH = "/charts";

	public final String chartId;

	M2XChart(M2XClient client, String chartId)
	{
		super(client);
		assert chartId != null && chartId.length() > 0;

		this.chartId = chartId;
	}

	String buildPath(String path)
	{
		return concat(M2XChart.URL_PATH, "/", this.chartId, path);
	}

	/**
	 * Get details of a specific chart.
	 *
	 * This method is public and therefore it does not require
	 * the user to authenticate himself using an API key.
	 *
	 * https://m2x.att.com/developer/documentation/v2/charts#View-Chart-Details
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Update an existing chart.
	 *
	 * https://m2x.att.com/developer/documentation/v2/charts#Update-Chart
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Delete an existing chart.
	 *
	 * https://m2x.att.com/developer/documentation/v2/charts#Delete-Chart
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}

	/**
	 * Generally used in the src attribute of an <img> html tag.
	 *
	 * This method is public and therefore it does not require
	 * the user to authenticate himself using an API key.
	 *
	 * https://m2x.att.com/developer/documentation/v2/charts#Render-Chart
	 */
	public String renderUrl(String format, String query)
	{
		return this.client.buildUrl(buildPath("." + format), query);
	}
}

