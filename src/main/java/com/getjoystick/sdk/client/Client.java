package com.getjoystick.sdk.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.errors.ApiUnknownException;
import com.getjoystick.sdk.models.JoystickContentOptions;
import com.getjoystick.sdk.models.JoystickFullContent;
import com.getjoystick.sdk.models.PublishData;
import com.getjoystick.sdk.models.ResponseType;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * HTTP client for API communication.
 * For more info check the <a href="https://docs.getjoystick.com/api-reference/">Joystick API reference</a>.
 */
public interface Client extends Closeable {

    /**
     * Get content from Joystick API by contentId
     *
     * @param contentId content id in string format
     * @return configuration content from Joystick
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    JsonNode getContent(String contentId);

    /**
     * Get serialized content from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @return Joystick configuration serialized as String
     */
    String getContentSerialized(String contentId);

    /**
     * Get full content, including meta and hash, from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @return Object representing full Joystick content
     */
    JoystickFullContent<JsonNode> getFullContent(String contentId);

    /**
     * Get full content, including meta and hash, from Joystick API by contentId. Config data is serialized.
     *
     * @param contentId content id in String format
     * @return object representing full Joystick content, where config data is serialized to string
     */
    JoystickFullContent<String> getFullContentSerialized(String contentId);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of configuration data by content id
     */
    Map<String, JsonNode> getContents(Collection<String> contentIds);

    /**
     * Get map of serialized configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of serialized to string configuration data by content id
     */
    Map<String, String> getContentsSerialized(Collection<String> contentIds);

    /**
     * Get map of configurations in full format, including meta and hash, for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of full configuration data by content id
     */
    Map<String, JoystickFullContent<JsonNode>> getFullContents(Collection<String> contentIds);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of full content where data config is serialized to string
     */
    Map<String, JoystickFullContent<String>> getFullContentsSerialized(Collection<String> contentIds);

    /**
     * Get content from Joystick API by contentId.
     *
     * @param contentId content id in string format
     * @param contentOptions optional parameters for getting Joystick content
     * @return configuration content from Joystick
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    JsonNode getContent(String contentId, JoystickContentOptions contentOptions);

    /**
     * Get serialized content from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @param contentOptions optional parameters for getting Joystick content
     * @return Joystick configuration serialized as String
     */
    String getContentSerialized(String contentId, JoystickContentOptions contentOptions);

    /**
     * Get full content, including meta and hash, from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @return Object representing full Joystick content
     * @param contentOptions optional parameters for getting Joystick content
     */
    JoystickFullContent<JsonNode> getFullContent(String contentId, JoystickContentOptions contentOptions);

    /**
     * Get full content, including meta and hash, from Joystick API by contentId. Config data is serialized.
     *
     * @param contentId content id in String format
     * @param contentOptions optional parameters for getting Joystick content
     * @return object representing full Joystick content, where config data is serialized to string
     */
    JoystickFullContent<String> getFullContentSerialized(String contentId, JoystickContentOptions contentOptions);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of configuration data by content id
     */
    Map<String, JsonNode> getContents(Collection<String> contentIds, JoystickContentOptions contentOptions);

    /**
     * Get map of serialized configurations for multiple content ids
     *
     * @param contentIds collection of content id
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of serialized to string configuration data by content id
     */
    Map<String, String> getContentsSerialized(Collection<String> contentIds, JoystickContentOptions contentOptions);

    /**
     * Get map of configurations in full format, including meta and hash, for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of full configuration data by content id
     */
    Map<String, JoystickFullContent<JsonNode>> getFullContents(Collection<String> contentIds,
                                                         JoystickContentOptions contentOptions);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of full content where data config is serialized to string
     */
    Map<String, JoystickFullContent<String>> getFullContentsSerialized(Collection<String> contentIds,
                                                                       JoystickContentOptions contentOptions);

    /**
     * Get content from Joystick API by contentIds.
     *
     * @param contentIds collection of content ids in string format
     * @return configurations stored in Joystick in string format
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    String getContentsAsString(Collection<String> contentIds);

    /**
     * Get content from Joystick API by contentIds and return response considering response type.
     *
     * @param contentIds collection of content ids in string format
     * @param responseType declares if response should be serialized or not serialized
     * @return configurations stored in Joystick in string format
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    String getContentsAsString(Collection<String> contentIds, ResponseType responseType);

    /**
     * Get content from Joystick API by parameters.
     *
     * @param contentIds collection of content ids in string format
     * @param responseType declares if response should be serialized or not serialized
     * @param fullResponse if response should be in full format
     * @return configurations stored in Joystick in string format
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    String getContentsAsString(Collection<String> contentIds, ResponseType responseType, boolean fullResponse);

    /**
     * Get content from Joystick API by parameters.
     *
     * @param contentIds collection of content ids in string format
     * @param responseType declares if response should be serialized or not serialized
     * @param fullResponse if response should be in full format
     * @param refresh if true direct call to Joystick will be made skipping cache
     * @return configurations stored in Joystick in string format
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    String getContentsAsString(Collection<String> contentIds, ResponseType responseType, boolean fullResponse, boolean refresh);

    /**
     * Publish update for already existing content in Joystick
     *
     * @param contentId id of existing content
     * @param data includes data to be updated in Joystick
     */
    void publishContentUpdate(String contentId, PublishData data);

}
