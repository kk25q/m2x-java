AT&T's M2X Java Client
==========================

[AT&T M2X](http://m2x.att.com) is a cloud-based fully managed time-series data storage service for network connected machine-to-machine (M2M) devices and the Internet of Things (IoT).

The [AT&T M2X API](https://m2x.att.com/developer/documentation/overview) provides all the needed operations and methods to connect your devices to AT&T's M2X service. This library aims to provide a simple wrapper to interact with the AT&T M2X API for [Java](http://www.java.com/en/). Refer to the [Glossary of Terms](https://m2x.att.com/developer/documentation/glossary) to understand the nomenclature used throughout this documentation.

Getting Started
==========================

1. Signup for an [M2X Account](https://m2x.att.com/signup).
2. Obtain your _Master Key_ from the Master Keys tab of your [Account Settings](https://m2x.att.com/account) screen.
2. Create your first [Device](https://m2x.att.com/devices) and copy its _Device ID_.
3. Review the [M2X API Documentation](https://m2x.att.com/developer/documentation/overview).

Setup
==========================

The M2X Java client is not submitted to any of the existing Maven repositories. To start using it now, please build and install it locally.

[Maven 3](http://maven.apache.org/) is required for client building. After [Maven installation](http://maven.apache.org/download.cgi) please execute
the following command from root directory of client source code (folder where `pom.xml` file is placed) to build:

```bash
    mvn package install
```

`install` can be omitted in case the client is used in a non-Maven environment.

Afterwards, the client's jar will be created in the `target` directory. By default, Maven builds jar files without dependencies.
Please see required libraries and their versions in the *Requirements and Dependencies* section.

To build client with all dependencies included please run the following command:

```bash
    mvn package install -P one-jar
```

See `target` directory for jar with `one-jar` suffix in the name.


Requirements and Dependencies
==========================

The M2X Java client requires **Java version 1.5 or greater**.

Add the following to pom.xml to start using M2XClient.
```xml
<dependency>
	<groupId>com.att.m2x</groupId>
    <artifactId>m2x-java-client</artifactId>
	<version>2.1.0</version>
</dependency>
```

The client has the following library dependencies:
* JSON in Java, 20140107, http://www.JSON.org/


Example
==========================

Currently, the client supports API v2 and all M2X API documents can be found at [M2X API Documentation](https://m2x.att.com/developer/documentation/overview).

To create a client instance only one parameter, the API Key, is required.
Read more about M2X API keys in the [API Keys](https://m2x.att.com/developer/documentation/overview#API-Keys) section of [M2X API Documentation](https://m2x.att.com/developer/documentation/overview).
To create a client instance, do the following:

```java
	import com.att.m2x.client.M2XClient;

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
