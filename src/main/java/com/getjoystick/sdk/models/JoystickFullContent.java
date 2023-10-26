package com.getjoystick.sdk.models;

public class JoystickFullContent<T> {

    private T data;

    private JoystickMeta meta;

    private String hash;

    public JoystickFullContent(final T data, final JoystickMeta meta, final String hash) {
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
        return "JoystickFullContent(data=" + this.getData() + ", meta=" + this.getMeta() + ", hash=" + this.getHash() + ")";
    }

}
