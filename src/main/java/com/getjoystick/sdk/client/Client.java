package com.getjoystick.sdk.client;

import com.getjoystick.sdk.errors.ApiUnknownException;
import com.getjoystick.sdk.models.JoystickData;
import com.getjoystick.sdk.models.JoystickFullContent;
import com.getjoystick.sdk.models.JoystickFullContentJson;
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
     * Get content from Joystick API by contentId and class.
     *
     * @param contentId content id in string format
     * @param clazz Class of object to be returned
     * @param <T> Type of object to be returned
     * @return Joystick configuration as Java object specified by clazz parameter
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    <T> T getContent(String contentId, Class<T> clazz);

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
     * @param clazz Class of config data
     * @return Object representing full Joystick content
     * @param <T> type of config data
     */
    <T> JoystickFullContent<T> getFullContent(String contentId, Class<T> clazz);

    /**
     * Get full content, including meta and hash, from Joystick API by contentId. Config data is serialized.
     *
     * @param contentId content id in String format
     * @return bject representing full Joystick content, where config data is serialized to string
     */
    JoystickFullContent<String> getFullContentSerialized(String contentId);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of configuration data by content id
     */
    Map<String, JoystickData> getContents(Collection<String> contentIds);

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
     * @param contentIds
     * @return map of full configuration data by content id
     */
    Map<String, JoystickFullContentJson> getFullContents(Collection<String> contentIds);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of full content where data config is serialized to string
     */
    Map<String, JoystickFullContent<String>> getFullContentsSerialized(Collection<String> contentIds);

    /**
     * Get content from Joystick API by contentId and class.
     *
     * @param contentId content id in string format
     * @param clazz Class of object to be returned
     * @param <T> Type of object to be returned
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     * @return Joystick configuration as Java object specified by clazz parameter
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    <T> T getContent(String contentId, Class<T> clazz, boolean refresh);

    /**
     * Get serialized content from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     * @return Joystick configuration serialized as String
     */
    String getContentSerialized(String contentId, boolean refresh);

    /**
     * Get full content, including meta and hash, from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @param clazz Class of config data
     * @return Object representing full Joystick content
     * @param <T> type of config data
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     */
    <T> JoystickFullContent<T> getFullContent(String contentId, Class<T> clazz, boolean refresh);

    /**
     * Get full content, including meta and hash, from Joystick API by contentId. Config data is serialized.
     *
     * @param contentId content id in String format
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     * @return bject representing full Joystick content, where config data is serialized to string
     */
    JoystickFullContent<String> getFullContentSerialized(String contentId, boolean refresh);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     * @return map of configuration data by content id
     */
    Map<String, JoystickData> getContents(Collection<String> contentIds, boolean refresh);

    /**
     * Get map of serialized configurations for multiple content ids
     *
     * @param contentIds collection of content id
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     * @return map of serialized to string configuration data by content id
     */
    Map<String, String> getContentsSerialized(Collection<String> contentIds, boolean refresh);

    /**
     * Get map of configurations in full format, including meta and hash, for multiple content ids
     *
     * @param contentIds
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     * @return map of full configuration data by content id
     */
    Map<String, JoystickFullContentJson> getFullContents(Collection<String> contentIds, boolean refresh);

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param refresh if true then content is loaded via Joystick remote call, skipping cache
     * @return map of full content where data config is serialized to string
     */
    Map<String, JoystickFullContent<String>> getFullContentsSerialized(Collection<String> contentIds, boolean refresh);

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
