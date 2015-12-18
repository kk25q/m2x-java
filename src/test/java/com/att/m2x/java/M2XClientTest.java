package com.att.m2x.java;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static com.att.m2x.java.M2XClient.API_VERSION;

public class M2XClientTest extends M2XTestBase
{
	private M2XDistribution distribution = null;
	private M2XDevice device = null;
	private M2XStream stream = null;
	private M2XKey key = null;
	private M2XCollection collection = null;

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

	public class DeviceCatalogSearchParams
	{
		public DeviceLocationFilter location;
		public DeviceLocationFilter getLocation() { return this.location; }
	}

	public class DeviceLocationFilter
	{
		public WithinCircleFilter within_circle;
		public WithinCircleFilter getWithin_circle() { return this.within_circle; }
	}

	public class WithinCircleFilter
	{
		public LocationPointParams center;
		public RadiusParams radius;
		public LocationPointParams getCenter() { return this.center; }
		public RadiusParams getRadius() { return this.radius; }
	}

	public class LocationPointParams
	{
		public double latitude;
		public double longitude;
		public double getLatitude() { return this.latitude; }
		public double getLongitude() { return this.longitude; }
	}

	public class RadiusParams
	{
		public int km;
		public int getKm() { return this.km; }
	}

	@After
	public void testCleanup()
	{
		if (this.collection != null)
		{
			delete(this.collection);
			this.collection = null;
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
		assertThat(url, is(client.endpoint + API_VERSION));

		url = client.buildUrl("/path", null);
		assertThat(url, is(client.endpoint + API_VERSION + "/path"));

		url = client.buildUrl(null, "query");
		assertThat(url, is(client.endpoint + API_VERSION + "?query"));

		url = client.buildUrl("/path", "foo=bar");
		assertThat(url, is(client.endpoint + API_VERSION + "/path?foo=bar"));

		url = client.buildUrl("/path", "foo=some bar with spaces");
		assertThat(url, is(client.endpoint + API_VERSION + "/path?foo=some%20bar%20with%20spaces"));

		url = client.buildUrl("/path", "foo=some bar with spaces&baz=bar");
		assertThat(url, is(client.endpoint + API_VERSION + "/path?foo=some%20bar%20with%20spaces&baz=bar"));
	}

	@Test
	public void queryTest()
	{
		String query;

		query = M2XClient.mapToQuery(new LinkedHashMap<String, String>()
		{{
			put("val1", "1 1");
			put("val2", "qwerty");
		}});
		assertThat(query, is("val1=1 1&val2=qwerty"));
	}

	@Test
	public void jsonSerializeTest()
	{
		String json;

		json = M2XClient.jsonSerialize(new LinkedHashMap<String, Object>()
		{{
			put("val1", 123);
			put("val2", "qw rty");
		}});
		assertThat(json, is("{\"val1\":123,\"val2\":\"qw rty\"}"));
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
			put("sort", "name");
			put("dir", "asc");
		}}));
		assertThat(response.status, is(200));
		JSONObject devices = response.json();
		assertThat(devices, is(notNullValue()));
		JSONArray deviceList = devices.getJSONArray("devices");
		assertThat(deviceList, is(notNullValue()));
		assertThat(deviceList.length(), greaterThan(0));

		JSONObject deviceLocation = null;
		for (int i=0; i < deviceList.length(); i++)
		{
			deviceLocation = deviceList.getJSONObject(i).getJSONObject("location");
			if (deviceLocation != null && deviceLocation.has("latitude") && deviceLocation.has("longitude"))
				break;
		}
		double latitude = deviceLocation.getDouble("latitude");
		double longitude = deviceLocation.getDouble("longitude");
		assertThat(deviceLocation, is(notNullValue()));
		response = client.deviceCatalogSearch(null, M2XClient.jsonSerialize(new DeviceCatalogSearchParams(){{
			location = new DeviceLocationFilter() {{
				within_circle = new WithinCircleFilter() {{
					center = new LocationPointParams() {{
						latitude = latitude;
						longitude = longitude;
					}};
					radius = new RadiusParams() {{ km = 10; }};
				}};
			}};
		}}));
		assertThat(response.status, is(200));
		devices = response.json();
		assertThat(devices, is(notNullValue()));
		deviceList = devices.getJSONArray("devices");
		assertThat(deviceList, is(notNullValue()));
		//TODO: assertThat(deviceList.length(), greaterThan(0));

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
		response = stream.update("{\"type\":\"numeric\",\"unit\":{\"label\":\"points\",\"symbol\":\"pt\"}}");
		assertThat(response.status, is(201));

		response = device.streams(null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("streams").length(), greaterThan(0));

		response = stream.updateValue(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("value", 10);
		}}));
		assertThat(response.status, is(202));

		Thread.sleep(1500);

		response = stream.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("name"), is(stream.streamName));
		assertThat(response.json().getInt("value"), is(10));

		response = stream.values("start=" + M2XClient.dateTimeToString(now), null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("values").length(), is(1));

		response = stream.updateValue(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("value", 20);
		}}));
		assertThat(response.status, is(202));

		Thread.sleep(2000);

		response = stream.sampling("type=sum&interval=200", null);
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

		response = stream.values(null, null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("values").length(), greaterThan(0));

		response = device.postUpdate("{\"timestamp\":\"" +
			M2XClient.dateTimeToString(new Date(now.getTime())) + "\",\"values\":{\"testdevicestream\":3}}");
		assertThat(response.status, is(202));

		response = device.postUpdates("{\"values\":{\"testdevicestream\":[{\"timestamp\":\"" +
			M2XClient.dateTimeToString(from) + "\",\"value\":5},{\"timestamp\":\"" +
			M2XClient.dateTimeToString(end) + "\",\"value\":4}]}}");
		assertThat(response.status, is(202));

		Thread.sleep(1000);

		response = device.values(null, null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("values").length(), greaterThan(0));

		response = device.searchValues("{\"start\":\"" + M2XClient.dateTimeToString(from) +
			"\",\"end\":\"" + M2XClient.dateTimeToString(end) +
			"\",\"streams\":[\"testdevicestream\"],\"conditions\":{\"testdevicestream\":{\"gte\":4}}}", null);
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("values").length(), greaterThan(0));

		response = device.log(null);
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
		response = stream.update("{\"type\":\"numeric\",\"unit\":{\"label\":\"points\",\"symbol\":\"pt\"}}");
		assertThat(response.status, is(201));

		response = distribution.streams();
		assertThat(response.status, is(200));
		assertThat(response.json().getJSONArray("streams").length(), greaterThan(0));

		Thread.sleep(1500);

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

		Thread.sleep(1500);

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

		Thread.sleep(1500);

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

		Thread.sleep(1500);

		response = key.delete();
		assertThat(response.status, is(204));
		key = null;
	}

	@Test
	public void collectionsApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		final String collectionName = "testCollection-" + testId;
		response = client.createCollection(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", collectionName);
			put("description", "unitTest");
		}}));
		assertThat(response.status, is(201));
		String collectionId = response.json().getString("id");
		assertThat(collectionId, is(notNullValue()));
		assertThat(collectionId.length(), greaterThan(0));
		collection = client.collection(collectionId);

		response = collection.details();
		assertThat(response.status, is(200));
		JSONObject json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("name"), is(collectionName));
		assertThat(json.getString("description"), is("unitTest"));

		response = client.collections(null);
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getJSONArray("collections").length(), greaterThan(0));

		response = collection.update(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", collectionName);
			put("description", "test");
		}}));
		assertThat(response.status, is(204));
		response = collection.details();
		assertThat(response.status, is(200));
		assertThat(response.json().getString("description"), is("test"));

		// metadata

		response = collection.updateMetadata(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("field1", "value1");
			put("field2", "value2");
		}}));
		assertThat(response.status, is(204));

		response = collection.metadata();
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("field1"), is("value1"));

		response = collection.updateMetadataField("field1", M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
				put("value", "value3");
		}}));
		assertThat(response.status, is(204));

		response = collection.metadataField("field2");
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("value"), is("value2"));

		response = collection.metadataField("field1");
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("value"), is("value3"));
	}

	@Test
	public void jobsApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		response = client.jobs(null);
		assertThat(response.status, is(200));
		JSONObject json = response.json();
		assertThat(json, is(notNullValue()));
		JSONArray jobs = json.getJSONArray("jobs");
		assertThat(jobs, is(notNullValue()));

		if (jobs.length() > 0)
		{
			String jobId = jobs.getJSONObject(0).getString("id");
			assertThat(jobId, is(notNullValue()));
			response = client.jobDetails(jobId);
			assertThat(response.status, is(200));
			json = response.json();
			assertThat(json, is(notNullValue()));
			assertThat(json.getString("id"), is(jobId));
		}
	}

	@Test
	public void timeApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		response = client.time(null);
		assertThat(response.status, is(200));
		JSONObject json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getLong("seconds"), is(greaterThan(0L)));
		assertThat(json.getLong("millis"), is(greaterThan(0L)));
		assertThat(json.getString("iso8601"), is(notNullValue()));

		response = client.time("seconds");
		assertThat(response.status, is(200));
		assertThat(response.raw, is(notNullValue()));

		response = client.time("millis");
		assertThat(response.status, is(200));
		assertThat(response.raw, is(notNullValue()));

		response = client.time("iso8601");
		assertThat(response.status, is(200));
		assertThat(response.raw, is(notNullValue()));
	}

	@Test
	public void commandsApiTest() throws IOException, InterruptedException
	{
		M2XResponse response;

		response = client.createDevice(M2XClient.jsonSerialize(new HashMap<String, Object>()
		{{
			put("name", "TestDevice-" + testId);
			put("visibility", "private");
		}}));
		assertThat(response.status, is(201));
		String deviceId = response.json().getString("id");
		assertThat(deviceId, is(notNullValue()));
		device = client.device(deviceId);
		Thread.sleep(1000);

		response = device.details();
		assertThat(response.status, is(200));
		String deviceKey = response.json().getString("key");
		assertThat(deviceKey, is(notNullValue()));

		final String commandName1 = "TestCommand1-" + testId;
		final String commandName2 = "TestCommand2-" + testId;
		response = client.sendCommand("{\"name\":\"" + commandName1 +
			"\",\"targets\":{\"devices\":[\"" + deviceId + "\"]}}");
		assertThat(response.status, is(202));
		response = client.sendCommand("{\"name\":\"" + commandName2 +
			"\",\"targets\":{\"devices\":[\"" + deviceId + "\"]}}");
		assertThat(response.status, is(202));
		Thread.sleep(1000);

		response = client.commands(M2XClient.mapToQuery(new HashMap<String, String>()
		{{
			put("name", commandName1);
		}}));
		assertThat(response.status, is(200));
		JSONObject json = response.json();
		assertThat(json, is(notNullValue()));
		JSONArray commands = json.getJSONArray("commands");
		assertThat(commands, is(notNullValue()));
		assertThat(commands.length(), is(1));
		String commandId1 = commands.getJSONObject(0).getString("id");
		assertThat(commandId1, is(notNullValue()));

		response = client.commandDetails(commandId1);
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("name"), comparesEqualTo(commandName1));

		M2XClient deviceClient = new M2XClient(deviceKey);
		M2XDevice device_ = deviceClient.device(deviceId);

		response = device_.commands(null);
		assertThat(response.status, is(200));
		json = response.json();
		commands = json.getJSONArray("commands");
		assertThat(commands, is(notNullValue()));
		assertThat(commands.length(), is(2));
		assertThat(commands.getJSONObject(1).getString("id"), comparesEqualTo(commandId1));
		String commandId2 = commands.getJSONObject(0).getString("id");
		assertThat(commandId2, is(notNullValue()));

		response = device_.commandDetails(commandId2);
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("name"), comparesEqualTo(commandName2));

		response = device_.processCommand(commandId1, null);
		assertThat(response.status, is(204));
		Thread.sleep(1000);
		response = device_.commandDetails(commandId1);
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("status"), is("processed"));

		response = device_.rejectCommand(commandId2, null);
		assertThat(response.status, is(204));
		Thread.sleep(1000);
		response = device_.commandDetails(commandId2);
		assertThat(response.status, is(200));
		json = response.json();
		assertThat(json, is(notNullValue()));
		assertThat(json.getString("status"), is("rejected"));
	}
}
