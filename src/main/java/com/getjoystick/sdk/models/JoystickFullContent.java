package com.getjoystick.sdk.models;

public class JoystickFullContent<T> {

    private T data;

    private JoystickMeta meta;

    private String hash;

    public JoystickFullContent(T data, JoystickMeta meta, String hash) {
        this.data = data;
        this.meta = meta;
        this.hash = hash;
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

    public void setData(T data) {
        this.data = data;
    }

    public void setMeta(JoystickMeta meta) {
        this.meta = meta;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JoystickFullContent)) {
            return false;
        } else {
            JoystickFullContent<?> other = (JoystickFullContent)o;
            Object thisData = this.getData();
            Object otherData = other.getData();
            if (thisData == null) {
                if (otherData != null) {
                    return false;
                }
            } else if (!thisData.equals(otherData)) {
                return false;
            }


            Object thisMeta = this.getMeta();
            Object otherMeta = other.getMeta();
            if (thisMeta == null) {
                if (otherMeta != null) {
                    return false;
                }
            } else if (!thisMeta.equals(otherMeta)) {
                return false;
            }

            Object thisHash = this.getHash();
            Object otherHash = other.getHash();
            if (thisHash == null) {
                if (otherHash != null) {
                    return false;
                }
            } else if (!thisHash.equals(otherHash)) {
                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        int result = 1;
        Object thisData = this.getData();
        result = result * 59 + (thisData == null ? 43 : thisData.hashCode());
        Object thisMeta = this.getMeta();
        result = result * 59 + (thisMeta == null ? 43 : thisMeta.hashCode());
        Object thisHash = this.getHash();
        result = result * 59 + (thisHash == null ? 43 : thisHash.hashCode());
        return result;
    }

    public String toString() {
        return "JoystickFullContent(data=" + this.getData() + ", meta=" + this.getMeta() + ", hash=" + this.getHash() + ")";
    }

}
