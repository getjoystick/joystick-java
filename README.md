# Java Client for [Joystick Remote Config](https://www.getjoystick.com/)

This is a library that simplifies communicating with the [Joystick API](https://docs.getjoystick.com/) for using remote configurations with your Java project. With remote configurations, you can instantly change the behavior and appearance of your application without code changes.

Joystick is a modern remote config platform where you manage all of your configurable parameters. We are natively multi-environment, preserve your version history, have advanced work-flow & permissions management, and much more. Have one API to use for any JSON configs.

- [Full Developer Documentation](https://docs.getjoystick.com)
- [Joystick Remote Config](https://getjoystick.com)
- [Get a Free Sandbox Account Today](https://app.getjoystick.com/onboarding)

Provided client is supporting Java 8+.

## Setup

Before using the Joystick Java SDK in your project, you need to add maven dependency:

```sh
<repositories>
    <repository>
        <id>nexus</id>
        <name>nexus-snapshot</name>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<!-- Joystick sdk -->
<dependency>
    <groupId>com.getjoystick</groupId>
    <artifactId>joystick-sdk</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

Using Joystick to get remote configurations in your Java project is a breeze.

```java
// Add imports
import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.Joystick;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.util.JoystickMapper;
.....

// Get apiKey from environment variables java
String joystickApiKey = System.getenv("JOYSTICK_API_KEY");

// Initialize a client with a Joystick API Key
ClientConfig config = ClientConfig.builder().setApiKey(joystickApiKey).build();
Client client = Joystick.create(config);

// Request a single configuration as com.fasterxml.jackson.databind.JsonNode object
JsonNode content = client.getContent("idOfMyContent");

// Transform joystick response to custom class
MyCustomClass myObj = JoystickMapper.toObject(content, MyCustomClass.class);

// Request multiple configurations at the same time
List<String> contentIds = ImmutableList.of("contentId1", "contentId2");
Map<String, JsonNode> contentsMap = client.getContents(contentIds);

```

### Specifying Additional Parameters

When creating the `Joystick` client, you can specify additional parameters which will be used by all API calls to the Joystick API. These additional parameters are used for AB Testing (`userId`), segmentation (`params`), and backward-compatible version delivery (`semVer`).

For more details see [API documentation](https://docs.getjoystick.com/api-reference/).

```java
// Initializing a client with options
ClientConfig config = ClientConfig.builder()
    .setApiKey("API_KEY")
    .setUserId("userId") // Any string
    .setSemVer("0.0.1") // String in the format of semantic versioning
    .setParams(ImmutableMap.of("Location", "Earth")) // Key-value pairs of strings, numbers, or booleans
    .build();
```

### Various Ways of Getting Configuration Content

With our Joystick java SDK, you can easily get the configuration response in different ways.

#### Single Configuration
| Response | Config as JSON | Config as Serialized String |
|----------|----------|----------|
| Config content only |   `getContent`   |   `getContentSerialized`   |
| Config content with additional meta data |   `getFullContent`   |   `getFullContentSerialized`   |


#### Multiple Configurations
| Response | Config as JSON | Config as Serialized String |
|----------|----------|----------|
| Multiple configs content only |   `getContents`   |   `getContentsSerialized`   |
| Multiple configs content with additional meta data |   `getFullContents`   |   `getFullContentsSerialized`   |

### Error handling

The client can raise different types of exceptions with the base class of `JoystickException`.

```java
try {
    Map<String, JsonNode> contentsMap = client.getContents(
        ImmutableList.of("contentId1", "contentId2")
    );
}
catch (ApiHttpException e) {
    // Handle HTTP error (i.e. timeout, or invalid HTTP code)
}
catch (MultipleContentsApiException e) {
    // Handle API exception (i.e. content is not found, or some of the keys can't be retrieved)
}
```

### Caching

By default, the client uses [ApiCacheLRU](./src/main/java/com/getjoystick/sdk/cache/impl/ApiCacheLRU.java), based on [guava in memory Cache](https://guava.dev/releases/21.0/api/docs/com/google/common/cache/Cache.html).

You can specify your own cache implementation by implementing the interface [ApiCache](./src/main/java/com/getjoystick/sdk/cache/ApiCache.java).
Sample code snippet could be found in [examples](./examples/src/main/java/com/getjoystick/examples/controller/CustomCacheController.java)

```java
public class CaffeineCustomCache<K, V> implements ApiCache<K, V> {
    //implementation of base methods
}

// Setting custom cache
ClientConfig config = ClientConfig.builder()
.setApiKey("API_KEY")
.setCache(new CaffeineCustomCache<>())
.build();
```

Default cache expiration is set to 300 seconds. It can be changed during ClientConfig build

```java
// Setting CacheExpirationSeconds
ClientConfig config = ClientConfig.builder()
    .setApiKey("API_KEY")
    .setCacheExpirationSeconds(600)
    .build();
```

#### `Refresh` option

To ignore the existing cache when requesting a config, pass this option as `true`.

```java
JoystickContentOptions options = new JoystickContentOptions(true);

JsonNode content = client.getContent("idOfMyContent", options);

// OR

Map<String, JsonNode> contentsMap = client.getContents(
        ImmutableList.of("contentId1", "contentId2"),
        options
    );
```

## License

The MIT. Please see [License File](LICENSE.md) for more information.
