package com.getjoystick.sdk.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.util.JoystickUtil;

import static com.getjoystick.sdk.util.JoystickUtil.removeTrailingQuotes;

public class JoystickFullContent<T> {

    private static final String DATA_FIELD = "data";

    private static final String META_FIELD = "meta";

    private static final String HASH_FIELD = "hash";

    private String dataString;

    private T data;

    private JoystickMeta meta;

    private String hash;

    public JoystickFullContent() {}

    public JoystickFullContent(final T data, final String dataString, final JoystickMeta meta, final String hash) {
        this.data = data;
        this.dataString = dataString;
        this.meta = meta;
        this.hash = hash;
    }

    public JoystickFullContent(final String content, final boolean isSerialized) {
        final JsonNode jsonContent = JoystickUtil.readTree(content);
        if(isSerialized) {
            this.data = (T) removeTrailingQuotes(jsonContent.get(DATA_FIELD).toString());
        } else {
            this.data = (T) jsonContent.get(DATA_FIELD);
        }
        this.dataString = content;
        this.meta = JoystickUtil.treeToValue(jsonContent.get(META_FIELD), JoystickMeta.class);
        this.hash = jsonContent.get(HASH_FIELD).asText();
    }

    public JoystickFullContent(final JsonNode jsonContent, final boolean isSerialized) {
        if(isSerialized) {
            this.data = (T) removeTrailingQuotes(jsonContent.get(DATA_FIELD).toString());
        } else {
            this.data = (T) jsonContent.get(DATA_FIELD);
        }
        this.dataString = removeTrailingQuotes(jsonContent.toString());
        this.meta = JoystickUtil.treeToValue(jsonContent.get(META_FIELD), JoystickMeta.class);
        this.hash = jsonContent.get(HASH_FIELD).asText();
    }

    public T getData() {
        return this.data;
    }

    public JoystickMeta getMeta() {
        return this.meta;
    }

    public String getHash() {
        return this.hash;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setMeta(final JoystickMeta meta) {
        this.meta = meta;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof JoystickFullContent)) {
            return false;
        }
        final JoystickFullContent<?> other = (JoystickFullContent)object;
        final Object thisData = this.getData();
        final Object otherData = other.getData();
        if (thisData == null) {
            if (otherData != null) {
                return false;
            }
        } else if (!thisData.equals(otherData)) {
            return false;
        }


        final Object thisMeta = this.getMeta();
        final Object otherMeta = other.getMeta();
        if (thisMeta == null) {
            if (otherMeta != null) {
                return false;
            }
        } else if (!thisMeta.equals(otherMeta)) {
            return false;
        }

        final Object thisHash = this.getHash();
        final Object otherHash = other.getHash();
        if (thisHash == null) {
            return otherHash == null;
        }
        return thisHash.equals(otherHash);
    }

    @Override
    public int hashCode() {
        int result = 1;
        final Object thisData = this.getData();
        result = result * 59 + (thisData == null ? 43 : thisData.hashCode());
        final Object thisMeta = this.getMeta();
        result = result * 59 + (thisMeta == null ? 43 : thisMeta.hashCode());
        final Object thisHash = this.getHash();
        result = result * 59 + (thisHash == null ? 43 : thisHash.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return dataString;
    }

}
