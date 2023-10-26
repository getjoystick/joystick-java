package com.getjoystick.sdk.models;

import java.util.List;

public class JoystickMeta {
    private Long uid;

    private Long mod;

    private List<Object> variants;

    private List<Object> seg;

    public JoystickMeta(final Long uid, final Long mod, final List<Object> variants, final List<Object> seg) {
        this.uid = uid;
        this.mod = mod;
        this.variants = variants;
        this.seg = seg;
    }

    public Long getUid() {
        return this.uid;
    }

    public Long getMod() {
        return this.mod;
    }

    public List<Object> getVariants() {
        return this.variants;
    }

    public List<Object> getSeg() {
        return this.seg;
    }

    public void setUid(final Long uid) {
        this.uid = uid;
    }

    public void setMod(final Long mod) {
        this.mod = mod;
    }

    public void setVariants(final List<Object> variants) {
        this.variants = variants;
    }

    public void setSeg(final List<Object> seg) {
        this.seg = seg;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof JoystickMeta)) {
            return false;
        }
        final JoystickMeta other = (JoystickMeta)object;

        final Object thisUid = this.getUid();
        final Object otherUid = other.getUid();
        if (thisUid == null) {
            if (otherUid != null) {
                return false;
            }
        } else if (!thisUid.equals(otherUid)) {
            return false;
        }

        final Object thisMod = this.getMod();
        final Object otherMod = other.getMod();
        if (thisMod == null) {
            if (otherMod != null) {
                return false;
            }
        } else if (!thisMod.equals(otherMod)) {
            return false;
        }

        final Object thisVariants = this.getVariants();
        final Object otherVariants = other.getVariants();
        if (thisVariants == null) {
            if (otherVariants != null) {
                return false;
            }
        } else if (!thisVariants.equals(otherVariants)) {
            return false;
        }

        final Object thisSeg = this.getSeg();
        final Object otherSeg = other.getSeg();
        if (thisSeg == null) {
            return otherSeg == null;
        }
        return thisSeg.equals(otherSeg);
    }

    @Override
    public int hashCode() {
        int result = 1;
        final Object thisUid = this.getUid();
        result = result * 59 + (thisUid == null ? 43 : thisUid.hashCode());
        final Object thisMod = this.getMod();
        result = result * 59 + (thisMod == null ? 43 : thisMod.hashCode());
        final Object thisVariants = this.getVariants();
        result = result * 59 + (thisVariants == null ? 43 : thisVariants.hashCode());
        final Object thisSeg = this.getSeg();
        result = result * 59 + (thisSeg == null ? 43 : thisSeg.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "JoystickMeta(uid=" + this.getUid() + ", mod=" + this.getMod() + ", variants=" + this.getVariants() + ", seg=" + this.getSeg() + ")";
    }

}
