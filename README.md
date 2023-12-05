# Java Client for [Joystick Remote Config](https://www.getjoystick.com/)

This is a library that simplifies communicating with the [Joystick API](https://docs.getjoystick.com/) for using remote configs with your Java project. Joystick is a modern remote config platform where you manage all of your configurable parameters. We are natively multi-environment, preserve your version history, have advanced work-flow & permissions management, and much more. Have one API to use for any JSON configs.

- [Full Developer Documentation](https://docs.getjoystick.com)
- [Joystick Remote Config](https://getjoystick.com)

Provided client is supporting Java 8+.

## Configuration

Before being able to use Joystick API in your project, you need to add maven dependency:

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

```
// Add imports
import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.Joystick;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.util.JoystickMapper;
.....

// Initialize a client with a Joystick API Key
ClientConfig config = ClientConfig.builder().setApiKey(apiKey).build();
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

```
// Initializing a client with options
ClientConfig config = ClientConfig.builder()
    .setApiKey("API_KEY")
    .setUserId("userId")
    .setSemVer("0.0.1")
    .setParams(ImmutableMap.of("Country", "CN"))
    .build();
```

### Error handling

The client can raise different types of exceptions with the base class of `JoystickException`.

```
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

#### `Refresh` option

To ignore the existing cache when requesting a config, pass this option as `true`.

```
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
