package com.att.m2x.java;

import java.io.IOException;

/**
 * Wrapper for AT&amp;T M2X Charts API
 *
 * @see <a href="https://m2x.att.com/developer/documentation/v2/charts">M2X Charts API Documentation</a>
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
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/charts#View-Chart-Details">https://m2x.att.com/developer/documentation/v2/charts#View-Chart-Details</a>
	 */
	public M2XResponse details() throws IOException
	{
		return makeGet(null, null);
	}

	/**
	 * Update an existing chart.
	 *
	 * @param jsonContent parameters for the chart to be updated as JSON formatted String
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/charts#Update-Chart">https://m2x.att.com/developer/documentation/v2/charts#Update-Chart</a>
	 */
	public M2XResponse update(String jsonContent) throws IOException
	{
		return makePut(null, jsonContent);
	}

	/**
	 * Delete an existing chart.
	 *
	 * @return the API response
	 * @throws IOException if an input or output exception occurred
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/charts#Delete-Chart">https://m2x.att.com/developer/documentation/v2/charts#Delete-Chart</a>
	 */
	public M2XResponse delete() throws IOException
	{
		return makeDelete(null, null);
	}

	/**
	 * Generally used in the src attribute of an html img tag.
	 *
	 * This method is public and therefore it does not require
	 * the user to authenticate himself using an API key.
	 *
	 * @param format the format of the chart image being returned
	 * @param query query parameters (optional)
	 * @return image of the chart in the given format
	 * @see <a href="https://m2x.att.com/developer/documentation/v2/charts#Render-Chart">https://m2x.att.com/developer/documentation/v2/charts#Render-Chart</a>
	 */
	public String renderUrl(String format, String query)
	{
		return this.client.buildUrl(buildPath("." + format), query);
	}
}

