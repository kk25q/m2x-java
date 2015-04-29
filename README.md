AT&T's M2X Java Client
==========================

[AT&T M2X](http://m2x.att.com) is a cloud-based fully managed time-series data storage service for network connected machine-to-machine (M2M) devices and the Internet of Things (IoT).

The [AT&T M2X API](https://m2x.att.com/developer/documentation/overview) provides all the needed operations and methods to connect your devices to AT&T's M2X service. This library aims to provide a simple wrapper to interact with the AT&T M2X API for [Java](http://www.java.com/en/). Refer to the [Glossary of Terms](https://m2x.att.com/developer/documentation/glossary) to understand the nomenclature used throughout this documentation.

Getting Started
==========================

1. Signup for an [M2X Account](https://m2x.att.com/signup)
2. Obtain your _Master Key_ from the [Master Keys](https://m2x.att.com/account#master-keys) tab of your M2X Account Settings.
3. Review the [M2X API Documentation](https://m2x.att.com/developer/documentation/overview)

Setup
==========================

### Via Maven Central Repository (Recommended)
We've added the M2X Java Client Library to the [Maven Central Repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%20com.att.m2x%20a%3A%20java) to make it easy for you to add it as a dependency to your Java based project.

To include the M2X Java Client Library in your project, add the following to your project's `pom.xml` :

```xml
    <dependencies>
        <dependency>
            <groupId>com.att.m2x</groupId>
            <artifactId>java</artifactId>
            <version>2.2.0</version>
        </dependency>
    </dependencies>
```

### Manual Installation

1. Obtain the `com.att.m2x.java-v.v.v-one-jar.jar` for the [latest version](https://github.com/attm2x/m2x-java/releases/latest) of the M2X Java Client Library. Note: you must use the `one-jar` jar file, as this includes all M2X Java Client Library dependancies
2. Add the `com.att.m2x.java-v.v.v-one-jar.jar` as a dependency for your project via your IDE of choice

Requirements and Dependencies
==========================

The M2X Java client requires **Java version 1.5 or greater**.

The client has the following library dependencies, though if you followed the Setup instructions from above all dependencies will be included automatically:
* JSON in Java, 20140107, http://www.JSON.org/


Example
==========================

Currently, the client supports API v2 and all M2X API documents can be found at [M2X API Documentation](https://m2x.att.com/developer/documentation/overview).

To create a client instance only one parameter, the API Key, is required.
Read more about M2X API keys in the [API Keys](https://m2x.att.com/developer/documentation/overview#API-Keys) section of [M2X API Documentation](https://m2x.att.com/developer/documentation/overview).
To create a client instance, do the following:

```java
	import com.att.m2x.java.M2XClient;

	M2XClient client = new M2XClient("your api key here");
```

There is another method that has endpoint parameter. You don't need to pass it unless you want to connect to a different API endpoint.

The client class provides access to API calls returning lists of the following API objects: devices, distributions, keys, charts.

All API responses are wrapped in M2XResponse object.

- Get the list of all your keys:

```java
	M2XResponse keys = client.keys(null);
```

There are also a number of methods allowing you to get an instance of individual API object by providing its id or name as a parameter.

- Get an instance of a device:

```java
	M2XDevice device = client.device("your device id here");
```

Refer to the documentation on each class for further usage instructions.

- Create a new device, stream and put current value into it:

```java
 	M2XResponse response = client.createDevice(M2XClient.jsonSerialize(new HashMap<String, Object>()
	{{
		put("name", "My Device");
		put("visibility", "private");
	}}));
	String deviceId = response.json().getString("id");
	M2XDevice device = client.device(deviceId);

	M2XStream stream = device.stream("mystream");
	stream.createOrUpdate("{\"type\":\"numeric\",\"unit\":{\"label\":\"points\",\"symbol\":\"pt\"}}");

	stream.updateValue(M2XClient.jsonSerialize(new HashMap<String, Object>()
	{{
		put("value", 10);
	}}));
```

You can find this code in [M2XClientTests.java](src/test/java/com/att/m2x/client/M2XClientTests.java).
These tests have a lot of examples for the most of M2X API methods.
To run the tests you should specify your Master API Key in the m2x.test.keys.xml resource file.

Versioning
==========================

This library aims to adhere to [Semantic Versioning 2.0.0](http://semver.org/). As a summary, given a version number MAJOR.MINOR.PATCH:

MAJOR will increment when backwards-incompatible changes are introduced to the client.
MINOR will increment when backwards-compatible functionality is added.
PATCH will increment with backwards-compatible bug fixes.
Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.

Note: the client version does not necessarily reflect the version used in the AT&T M2X API.

License
==========================

This library is provided under the MIT license. See [LICENSE](https://raw.github.com/attm2x/m2x-java/master/LICENSE) for applicable terms.
