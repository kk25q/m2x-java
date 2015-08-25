package com.att.m2x.java;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class M2XClientTests extends M2XTestBase
{
	private M2XDistribution distribution = null;
	private M2XDevice device = null;
	private M2XStream stream = null;
	private M2XKey key = null;
	private M2XChart chart = null;

	public class TestClass
	{
		public int intval;
		public String strval;

		public int getIntval() { return this.intval; }
		public String getStrval() { return this.strval; }
	}
	public class StreamValues
	{
		public StreamValue[] values;
		public StreamValue[] getValues() { return this.values; }
	}
	public class StreamValue
	{
		public Date timestamp;
		public int value;
		public String getTimestamp() { return M2XClient.dateTimeToString(this.timestamp); }
		public int getValue() { return this.value; }
	}
	public class ChartSeries
	{
		public String device;
		public String stream;
		public String getDevice() { return this.device; }
		public String getStream() { return this.stream; }
	}

	@After
	public void testCleanup()
	{
		if (this.chart != null)
		{
			delete(this.chart);
			this.chart = null;
		}
		if (this.key != null)
		{
			delete(this.key);
			this.key = null;
		}
		if (this.stream != null)
		{
			delete(this.stream);
			this.stream = null;
		}
		if (this.device != null)
		{
			delete(this.device);
			this.device = null;
		}
		if (this.distribution != null)
		{
			delete(this.distribution);
			this.distribution = null;
		}
	}
	private void delete(M2XClass entity)
	{
		try
		{
			entity.delete();
		}
		catch (IOException ex)
		{
		}
	}

	@Test
	public void buildUrlTest()
	{
		String url;

		url = client.buildUrl(null, null);
		assertThat(url, is(client.endpoint));

		url = client.buildUrl("/path", null);
		assertThat(url, is(client.endpoint + "/path"));

		url = client.buildUrl(null, "query");
		assertThat(url, is(client.endpoint + "?query"));

		url = client.buildUrl("/path", "query");
		assertThat(url, is(client.endpoint + "/path?query"));
	}

	@Test
	public void queryTest()
	{
		String query;

		query = M2XClient.mapToQuery(new HashMap<String, String>()
		{{
			put("val1", "1 1");
			put("val2", "qwerty");
		}});
		assertThat(query, is("val1=1+1&val2=qwerty"));

		query = M2XClient.objectToQuery(new TestClass()
		{{
			intval = 123;
			strval = "qw rty";
		}});
		assertThat(query, is("intval=123&strval=qw+rty"));
	}

	@Test
	public void jsonSerializeTest()
	{
		String json;

		json = M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("val1", 123);
			put("val2", "qw rty");
		}});
		assertThat(json, is("{\"val1\":123,\"val2\":\"qw rty\"}"));

		json = M2XClient.jsonSerialize(new TestClass()
		{{
			intval = 123;
			strval = "qw rty";
		}});
		assertThat(json, is("{\"strval\":\"qw rty\",\"intval\":123}"));
	}

	@Test
	public void deviceApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		// device

		response = client.deviceTags();
		assertThat(response.status, is(200));
		assertThat(response.success(), is(true));
		assertThat(response.error(), is(false));
		assertThat(response.raw, is(notNullValue()));
		assertThat(response.json(), is(notNullValue()));

		response = client.deviceCatalog(M2XClient.mapToQuery(new HashMap<String, String>()
		{{
			put("page", "1");
			put("limit", "10");
		}}));
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("devices").length(), greaterThan(0));

		response = client.createDevice(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "TestDevice-" + testId);
			put("visibility", "private");
		}}));
		assertThat(response.status, is(201));
		String deviceId = response.json().getString("id");
		assertThat(deviceId, is(notNullValue()));
		assertThat(deviceId.length(), greaterThan(0));
		device = client.device(deviceId);

		response = device.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("status"), is("enabled"));

		response = client.devices("visibility=private");
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("devices").length(), greaterThan(0));

		response = device.update(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("visibility", "private");
			put("description", "test");
		}}));
		assertThat(response.status, is(422));
		assertThat(response.json().getJSONObject("errors"), is(notNullValue()));

		response = device.update(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "TestDevice-" + testId);
			put("visibility", "private");
			put("description", "test");
		}}));
		assertThat(response.status, is(204));
		response = device.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("description"), is("test"));

		response = device.updateLocation(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "Test Location");
			put("latitude", 12);
			put("longitude", -34);
		}}));
		assertThat(response.status, is(202));

		response = device.location();
		if (response.status != 204)
		{
			assertThat(response.status, is(200));
			assertThat(response.json().getString("name"), is("Test Location"));
		}

		// stream

		stream = device.stream("testdevicestream");
		response = stream.createOrUpdate("{\"type\":\"numeric\",\"unit\":{\"label\":\"points\",\"symbol\":\"pt\"}}");
		assertThat(response.status, is(201));

		response = device.streams();
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("streams").length(), greaterThan(0));

		response = stream.updateValue(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("value", 10);
		}}));
		assertThat(response.status, is(202));

		Thread.sleep(1000);

		response = stream.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("name"), is(stream.streamName));
		assertThat(response.json().getInt("value"), is(10));

		response = stream.values("start=" + M2XClient.dateTimeToString(now));
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("values").length(), is(1));

		response = stream.updateValue(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("value", 20);
		}}));
		assertThat(response.status, is(202));

		Thread.sleep(1000);

		response = stream.sampling("type=sum&interval=100");
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("values").length(), is(1));
		assertThat(response.json().getJSONArray("values").getJSONObject(0).getInt("value"), is(30));

		response = stream.stats(null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONObject("stats").getInt("avg"), is(15));

		final Date from = new Date(now.getTime() - 120000);
		final Date end = new Date(now.getTime() - 60000);
		StreamValues values = new StreamValues();
		values.values = new StreamValue[]
		{
			new StreamValue() {{ timestamp = from; value = 1; }},
			new StreamValue() {{ timestamp = end; value = 2; }},
		};
		response = stream.postValues(M2XClient.jsonSerialize(values));
		assertThat(response.status, is(202));

		response = stream.deleteValues(M2XClient.mapToQuery(new HashMap<String, String>()
		{{
			put("from", M2XClient.dateTimeToString(from));
			put("end", M2XClient.dateTimeToString(end));
		}}));
		assertThat(response.status, is(204));

		response = stream.values(null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("values").length(), greaterThan(0));

		response = device.log();
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("requests").length(), greaterThan(0));

		response = stream.delete();
		assertThat(response.status, is(204));
		stream = null;

		response = device.delete();
		assertThat(response.status, is(204));
		device = null;
	}

	@Test
	public void distributionApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		// destribution

		response = client.createDistribution(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "TestDestribution-" + testId);
			put("visibility", "public");
		}}));
		assertThat(response.status, is(201));
		String distributionId = response.json().getString("id");
		assertThat(distributionId, is(notNullValue()));
		assertThat(distributionId.length(), greaterThan(0));
		distribution = client.distribution(distributionId);

		response = client.distributions();
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("distributions").length(), greaterThan(0));

		response = distribution.update(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "TestDestribution-" + testId);
			put("visibility", "private");
			put("description", "test");
		}}));
		assertThat(response.status, is(204));

		// stream

		stream = distribution.stream("testdevicestream");
		response = stream.createOrUpdate("{\"type\":\"numeric\",\"unit\":{\"label\":\"points\",\"symbol\":\"pt\"}}");
		assertThat(response.status, is(201));

		response = distribution.streams();
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("streams").length(), greaterThan(0));

		Thread.sleep(1000);

		response = stream.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("name"), is(stream.streamName));

		// device

		response = distribution.addDevice(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("serial", testId);
		}}));
		assertThat(response.status, is(201));
		String deviceId = response.json().getString("id");
		assertThat(deviceId, is(notNullValue()));
		assertThat(deviceId.length(), greaterThan(0));
		device = client.device(deviceId);

		response = distribution.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("status"), is("enabled"));
		assertThat(response.json().getJSONObject("devices").getInt("total"), is(1));

		response = distribution.devices();
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("devices").length(), is(1));

		Thread.sleep(1000);

		response = device.delete();
		assertThat(response.status, is(204));
		device = null;

		response = stream.delete();
		assertThat(response.status, is(204));
		stream = null;

		response = distribution.delete();
		assertThat(response.status, is(204));
		distribution = null;
	}

	@Test
	public void keyApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		// keys

		response = client.keys(null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("keys").length(), greaterThan(0));

		response = client.createKey(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "testkey" + testId);
			put("permissions", new String[] { "POST" });
		}}));
		assertThat(response.status, is(201));
		String keystr = response.json().getString("key");
		assertThat(keystr, is(notNullValue()));
		assertThat(keystr.length(), greaterThan(0));
		key = client.key(keystr);

		Thread.sleep(1000);

		response = key.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getBoolean("master"), is(true));

		response = key.update(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "testkey" + testId);
			put("permissions", new String[] { "GET" });
		}}));
		assertThat(response.status, is(204));

		response = key.regenerate();
		assertThat(response.status, is(201));
		keystr = response.json().getString("key");
		assertThat(keystr, is(notNullValue()));
		assertThat(keystr.length(), greaterThan(0));
		key = client.key(keystr);

		Thread.sleep(1000);

		response = key.delete();
		assertThat(response.status, is(204));
		key = null;
	}

	@Test
	public void chartApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		// device & stream

		response = client.createDevice(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "TestDevice-" + testId);
			put("visibility", "private");
		}}));
		assertThat(response.status, is(201));
		final String deviceId = response.json().getString("id");
		assertThat(deviceId, is(notNullValue()));
		assertThat(deviceId.length(), greaterThan(0));
		device = client.device(deviceId);

		Thread.sleep(1000);

		stream = device.stream("testdevicestream");
		response = stream.createOrUpdate("{\"type\":\"numeric\",\"unit\":{\"label\":\"points\",\"symbol\":\"pt\"}}");
		assertThat(response.status, is(201));

		Thread.sleep(1000);

		StreamValues values = new StreamValues();
		values.values = new StreamValue[]
		{
			new StreamValue() {{ timestamp = new Date(now.getTime() - 120000); value = 10; }},
			new StreamValue() {{ timestamp = new Date(now.getTime() - 60000); value = 30; }},
			new StreamValue() {{ timestamp = now; value = 20; }},
		};
		response = stream.postValues(M2XClient.jsonSerialize(values));
		assertThat(response.status, is(202));

		// charts

		response = client.createChart(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "testchart" + testId);
			put("series", new ChartSeries [] { new ChartSeries() {{ device = deviceId; stream = "testdevicestream"; }} });
		}}));
		assertThat(response.status, is(201));
		String id = response.json().getString("id");
		assertThat(id, is(notNullValue()));
		assertThat(id.length(), greaterThan(0));
		chart = client.chart(id);

		Thread.sleep(1000);

		response = chart.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("name"), is(notNullValue()));

		response = client.charts(null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("charts").length(), greaterThan(0));

		String url = chart.renderUrl("png", M2XClient.mapToQuery(new HashMap<String, String>()
		{{
			put("width", "100");
			put("height", "50");
		}}));
		assertThat(url, is(notNullValue()));
		assertThat(url.length(), greaterThan(0));

		response = chart.delete();
		assertThat(response.status, is(204));
		chart = null;

		response = stream.delete();
		assertThat(response.status, is(204));
		stream = null;

		response = device.delete();
		assertThat(response.status, is(204));
		device = null;
	}
}
