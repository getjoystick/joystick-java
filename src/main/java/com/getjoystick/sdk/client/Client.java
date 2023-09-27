package com.getjoystick.sdk.client;

import com.getjoystick.sdk.errors.ApiUnknownException;
import com.getjoystick.sdk.models.ResponseType;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

/**
 * HTTP client for API communication.
 * For more info check the <a href="https://docs.getjoystick.com/api-reference/">Joystick API reference</a>.
 */
public interface Client extends Closeable {

    /**
     * Get content from Joystick API by contentIds.
     *
     * @param contentIds collection of content ids in string format
     * @return configurations stored in Joystick in string format
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    String getContents(Collection<String> contentIds);

    /**
     * Get content from Joystick API by contentIds and return response considering response type.
     *
     * @param contentIds collection of content ids in string format
     * @param responseType declares if response should be serialized or not serialized
     * @return configurations stored in Joystick in string format
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    String getContents(Collection<String> contentIds, ResponseType responseType);

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
    String getContents(Collection<String> contentIds, ResponseType responseType, boolean fullResponse);

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
    String getContents(Collection<String> contentIds, ResponseType responseType, boolean fullResponse, boolean refresh);

}
